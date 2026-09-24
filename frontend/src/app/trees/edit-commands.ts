import { Observable, concat, defer, forkJoin, map, of, switchMap, toArray } from 'rxjs';

import { EdgeRoute, Prerequisite, TreeNode } from '../core/api.models';
import { NodeApi } from '../core/node-api';
import { TreeApi } from '../core/tree-api';
import { EditCommand } from './undo-history';

/**
 * What the commands need from the tree editor. Commands name nodes by library node id,
 * never by tree node id: undoing a removal places the node again under a new tree node id,
 * and later commands must still find it. A library node deleted by undo and recreated by
 * redo gets a new id too, which {@link alias} records.
 */
export interface EditContext {
  readonly treeApi: TreeApi;
  readonly nodeApi: NodeApi;
  treeId(): number;
  /** The tree node that currently places this library node; throws if it isn't placed. */
  placed(nodeId: number): TreeNode;
  /** The current edge between two library nodes' placements, if there is one. */
  edgeBetween(prerequisiteNodeId: number, dependentNodeId: number): Prerequisite | undefined;
  /** The library node's current id, following any recreations. */
  resolve(nodeId: number): number;
  /** Records that a library node was recreated under a new id. */
  alias(oldNodeId: number, newNodeId: number): void;
}

/** The editable state of a placed node. */
export interface NodeState {
  positionX: number;
  positionY: number;
  aggregateThreshold: number;
  individualThreshold: number;
}

export interface NodePosition {
  nodeId: number;
  positionX: number;
  positionY: number;
}

/** An edge, by the library nodes at its ends, with its route when it matters for undo. */
export interface EdgeEnds {
  prerequisiteNodeId: number;
  dependentNodeId: number;
  route?: EdgeRoute | null;
}

export function stateOf(treeNode: TreeNode): NodeState {
  const { positionX, positionY, aggregateThreshold, individualThreshold } = treeNode;
  return { positionX, positionY, aggregateThreshold, individualThreshold };
}

/**
 * A move or a threshold change: sets the node's whole state back or forward. `resetRoutes`
 * are the edges whose routes the move reset (it changed their number of segments, 5.4-E6b);
 * undo puts those routes back and redo resets them again.
 */
export function updateNode(
  ctx: EditContext,
  label: string,
  nodeId: number,
  before: NodeState,
  after: NodeState,
  resetRoutes: EdgeEnds[] = [],
): EditCommand {
  const set = (state: NodeState) => defer(() => ctx.treeApi.updateNode(ctx.treeId(), ctx.placed(nodeId).id, state));
  return {
    label,
    undo: () => concat(set(before), setRoutes(ctx, resetRoutes)),
    redo: () => concat(set(after), setRoutes(ctx, resetRoutes.map((ends) => ({ ...ends, route: null })))),
  };
}

/** Auto-layout: every position at once, and every edge back to its default route (5.4-E3b). */
export function layout(ctx: EditContext, before: NodePosition[], after: NodePosition[], routesBefore: EdgeEnds[]): EditCommand {
  const set = (positions: NodePosition[]) =>
    defer(() =>
      ctx.treeApi.updatePositions(
        ctx.treeId(),
        positions.map((p) => ({ treeNodeId: ctx.placed(p.nodeId).id, positionX: p.positionX, positionY: p.positionY })),
      ),
    );
  return {
    label: 'auto-layout',
    undo: () => concat(set(before), setRoutes(ctx, routesBefore)),
    redo: () => concat(set(after), defer(() => ctx.treeApi.resetRoutes(ctx.treeId()))),
  };
}

/** Dragging an edge's segment: its route before and after. */
export function reroute(ctx: EditContext, label: string, ends: EdgeEnds, before: EdgeRoute | null, after: EdgeRoute): EditCommand {
  return {
    label,
    undo: () => setRoutes(ctx, [{ ...ends, route: before }]),
    redo: () => setRoutes(ctx, [{ ...ends, route: after }]),
  };
}

export function addEdge(ctx: EditContext, label: string, ends: EdgeEnds): EditCommand {
  return { label, undo: () => removeEdgeBetween(ctx, ends), redo: () => addEdgeBetween(ctx, ends) };
}

export function removeEdge(ctx: EditContext, label: string, ends: EdgeEnds): EditCommand {
  return { label, undo: () => addEdgeBetween(ctx, ends), redo: () => removeEdgeBetween(ctx, ends) };
}

/** Placing an existing library node. */
export function placeNode(ctx: EditContext, label: string, nodeId: number, state: NodeState): EditCommand {
  return {
    label,
    undo: () => defer(() => ctx.treeApi.removeNode(ctx.treeId(), ctx.placed(nodeId).id)),
    redo: () => defer(() => ctx.treeApi.addNode(ctx.treeId(), { nodeId: ctx.resolve(nodeId), ...state })),
  };
}

/** Removing a node from the tree, which took its edges with it; undo brings both back. */
export function removeNode(ctx: EditContext, label: string, nodeId: number, state: NodeState, edges: EdgeEnds[]): EditCommand {
  return {
    label,
    undo: () =>
      defer(() => ctx.treeApi.addNode(ctx.treeId(), { nodeId: ctx.resolve(nodeId), ...state })).pipe(
        switchMap((added) => {
          // The re-placed node has a new tree node id; the other ends haven't changed
          const treeNodeIdOf = (id: number) => (ctx.resolve(id) === ctx.resolve(nodeId) ? added.id : ctx.placed(id).id);
          return edges.length
            ? forkJoin(
                edges.map((e) =>
                  ctx.treeApi.addPrerequisite(
                    ctx.treeId(),
                    treeNodeIdOf(e.prerequisiteNodeId),
                    treeNodeIdOf(e.dependentNodeId),
                    e.route ?? null,
                  ),
                ),
              )
            : of(null);
        }),
      ),
    redo: () => defer(() => ctx.treeApi.removeNode(ctx.treeId(), ctx.placed(nodeId).id)),
  };
}

/**
 * Creating a library node and placing it (5.3-V14). Undo removes it from the tree and
 * deletes the library node, unless it's been placed in another tree since; redo creates it
 * again (under a new id) and places it.
 */
export function createNode(ctx: EditContext, label: string, nodeId: number, title: string, state: NodeState): EditCommand {
  let deleted = false;
  return {
    label,
    undo: () =>
      defer(() => ctx.treeApi.removeNode(ctx.treeId(), ctx.placed(nodeId).id)).pipe(
        switchMap(() => ctx.nodeApi.treesUsing(ctx.resolve(nodeId))),
        switchMap((trees) => {
          deleted = trees.length === 0;
          return deleted ? ctx.nodeApi.delete(ctx.resolve(nodeId)) : of(null);
        }),
      ),
    redo: () => {
      const recreated: Observable<number> = deleted
        ? ctx.nodeApi.create({ title, description: null, readiness: 0, links: [] }).pipe(
            map((node) => {
              ctx.alias(ctx.resolve(nodeId), node.id);
              deleted = false;
              return node.id;
            }),
          )
        : defer(() => of(ctx.resolve(nodeId)));
      return recreated.pipe(switchMap((id) => ctx.treeApi.addNode(ctx.treeId(), { nodeId: id, ...state })));
    },
  };
}

function addEdgeBetween(ctx: EditContext, ends: EdgeEnds): Observable<unknown> {
  return defer(() =>
    ctx.treeApi.addPrerequisite(
      ctx.treeId(),
      ctx.placed(ends.prerequisiteNodeId).id,
      ctx.placed(ends.dependentNodeId).id,
      ends.route ?? null,
    ),
  );
}

function removeEdgeBetween(ctx: EditContext, ends: EdgeEnds): Observable<unknown> {
  return defer(() => ctx.treeApi.removePrerequisite(ctx.treeId(), findEdge(ctx, ends).id));
}

/** Sets each edge's route (null resets it), one after another. */
function setRoutes(ctx: EditContext, edges: EdgeEnds[]): Observable<unknown> {
  return concat(
    ...edges.map((ends) => defer(() => ctx.treeApi.updateRoute(ctx.treeId(), findEdge(ctx, ends).id, ends.route ?? null))),
  ).pipe(toArray());
}

function findEdge(ctx: EditContext, ends: EdgeEnds): Prerequisite {
  const edge = ctx.edgeBetween(ends.prerequisiteNodeId, ends.dependentNodeId);
  if (!edge) {
    throw new Error('That arrow is no longer in the tree');
  }
  return edge;
}
