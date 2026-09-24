import { EdgeRoute, Prerequisite, TreeNode } from '../core/api.models';
import { NODE_HEIGHT, NODE_WIDTH, Point } from './tree-layout';

/** The shortest straight run out of a node's bottom port or into its top port. */
export const STUB = 20;
/** How far to the side of the nodes a 5-segment route passes by default. */
export const SIDE_GAP = 30;

/** A segment that can be dragged: it moves along `axis`, changing `offsets[offsetIndex]`. */
export interface SegmentHandle {
  /** The segment runs from `points[segment]` to `points[segment + 1]`. */
  segment: number;
  /** `y` for a horizontal segment (dragged up and down), `x` for a vertical one. */
  axis: 'x' | 'y';
  offsetIndex: number;
}

export interface RoutedEdge {
  /** The prerequisite edge's id. */
  id: number;
  /** From the prerequisite's bottom-middle port to the dependent's top-middle port. */
  points: Point[];
  segments: 3 | 5;
  /** Each draggable segment's offset from its default place, after clamping. */
  offsets: number[];
  handles: SegmentHandle[];
}

/**
 * Edges leave from the bottom-middle of the prerequisite and enter the top-middle of the
 * dependent (5.4-E1, option A). A dependent comfortably below its prerequisite gets 3
 * segments (down, across, down). One level with it or above gets 5 (down, out to the right,
 * up, across, down into the top).
 */
export function segmentsNeeded(from: Point, to: Point): 3 | 5 {
  return to.y - from.y >= 2 * STUB ? 3 : 5;
}

/** The ports of an edge between two placed nodes. */
export function portsOf(prerequisite: TreeNode, dependent: TreeNode): { from: Point; to: Point } {
  return {
    from: { x: prerequisite.positionX, y: prerequisite.positionY + NODE_HEIGHT / 2 },
    to: { x: dependent.positionX, y: dependent.positionY - NODE_HEIGHT / 2 },
  };
}

/**
 * The right-angle route between two nodes. A stored route is applied only if it was made for
 * the same number of segments; otherwise the edge is back to its default route (5.4-E6b).
 * Offsets are clamped so a segment stays between the nodes' ports, so a move that would push
 * one past the edge's ends leaves it at the end instead.
 */
export function routeEdge(
  id: number,
  prerequisite: TreeNode,
  dependent: TreeNode,
  stored: EdgeRoute | null,
): RoutedEdge {
  const { from, to } = portsOf(prerequisite, dependent);
  const segments = segmentsNeeded(from, to);
  const requested = stored?.segments === segments ? stored.offsets : segments === 3 ? [0] : [0, 0, 0];

  if (segments === 3) {
    const middle = (from.y + to.y) / 2;
    const y = clamp(middle + requested[0], from.y + STUB, to.y - STUB);
    return {
      id,
      segments,
      points: [from, { x: from.x, y }, { x: to.x, y }, to],
      offsets: [y - middle],
      handles: [{ segment: 1, axis: 'y', offsetIndex: 0 }],
    };
  }

  const firstDefault = from.y + STUB;
  const sideDefault = Math.max(prerequisite.positionX, dependent.positionX) + NODE_WIDTH / 2 + SIDE_GAP;
  const lastDefault = to.y - STUB;
  const first = Math.max(firstDefault + requested[0], from.y + STUB);
  const side = sideDefault + requested[1];
  const last = Math.min(lastDefault + requested[2], to.y - STUB);
  return {
    id,
    segments,
    points: [from, { x: from.x, y: first }, { x: side, y: first }, { x: side, y: last }, { x: to.x, y: last }, to],
    offsets: [first - firstDefault, side - sideDefault, last - lastDefault],
    handles: [
      { segment: 1, axis: 'y', offsetIndex: 0 },
      { segment: 2, axis: 'x', offsetIndex: 1 },
      { segment: 3, axis: 'y', offsetIndex: 2 },
    ],
  };
}

/**
 * Routes every edge. `overrides` holds offsets for edges being dragged right now, which take
 * the place of their stored routes.
 */
export function routeEdges(
  edges: Prerequisite[],
  byId: Map<number, TreeNode>,
  overrides: ReadonlyMap<number, EdgeRoute> = new Map(),
): RoutedEdge[] {
  return edges.flatMap((edge) => {
    const prerequisite = byId.get(edge.prerequisiteTreeNodeId);
    const dependent = byId.get(edge.dependentTreeNodeId);
    if (!prerequisite || !dependent) {
      return [];
    }
    return [routeEdge(edge.id, prerequisite, dependent, overrides.get(edge.id) ?? edge.route)];
  });
}

/** Points in SVG `points` attribute form. */
export function pointsAttr(points: Point[]): string {
  return points.map((p) => `${p.x},${p.y}`).join(' ');
}

/**
 * Whether a stored route no longer fits after a node moved, because the edge now needs a
 * different number of segments. Such routes are reset when the move is saved.
 */
export function routeOutgrown(edge: Prerequisite, byId: Map<number, TreeNode>): boolean {
  const prerequisite = byId.get(edge.prerequisiteTreeNodeId);
  const dependent = byId.get(edge.dependentTreeNodeId);
  if (!edge.route || !prerequisite || !dependent) {
    return false;
  }
  const { from, to } = portsOf(prerequisite, dependent);
  return segmentsNeeded(from, to) !== edge.route.segments;
}

function clamp(value: number, min: number, max: number): number {
  return Math.min(Math.max(value, min), max);
}
