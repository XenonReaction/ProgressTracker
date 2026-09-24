import { Component, ElementRef, computed, effect, inject, input, signal, untracked, viewChild } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Observable, concat, firstValueFrom, forkJoin, last, map, of, switchMap } from 'rxjs';

import { EdgeRoute, Node, Prerequisite, Tree, TreeNode } from '../core/api.models';
import { NodeApi } from '../core/node-api';
import { errorMessage } from '../core/problem';
import { ReadinessEditor } from '../nodes/readiness-editor';
import { TreeApi } from '../core/tree-api';
import { AddNodePanel } from './add-node-panel';
import { autoLayout } from './auto-layout';
import { confirmTreeDelete } from './confirm-delete';
import { RoutedEdge, SegmentHandle, pointsAttr, routeEdges, routeOutgrown } from './edge-routes';
import * as commands from './edit-commands';
import { EdgeEnds, EditContext, NodeState } from './edit-commands';
import { READINESS_LEVELS, ReadinessState, readinessLabel, readinessState } from './readiness';
import { NODE_HEIGHT, NODE_WIDTH, Point, ViewBox, viewBoxFor } from './tree-layout';
import { EditCommand, UndoHistory } from './undo-history';

/**
 * What a click on the canvas does. The toolbar switches between them, so new tools can be
 * added later without changing how the others behave.
 */
export type Tool = 'select' | 'add' | 'connect' | 'delete';

/** Pointer movement (in canvas units) below which a press counts as a click, not a drag. */
const DRAG_THRESHOLD = 3;

interface Drag {
  treeNodeId: number;
  startPointer: Point;
  startPosition: Point;
  moved: boolean;
}

/** Dragging one segment of an edge's route (5.4). */
interface SegmentDrag {
  edgeId: number;
  handle: SegmentHandle;
  segments: 3 | 5;
  startPointer: Point;
  startOffsets: number[];
  moved: boolean;
}

/**
 * A tree drawn as SVG. It opens in view mode, where nodes can be explored and hand-entered
 * readiness updated. "Edit" starts an edit session on the server (a restore point) and
 * shows the toolbar; every change is still saved as soon as it's made. "Done" keeps the
 * changes and "Discard changes" puts the restore point back. Leaving the page while editing
 * asks first and discards; an unfinished session found on load (e.g. after a crash) is
 * resolved with "Keep" or "Discard" before anything else.
 */
@Component({
  selector: 'app-tree-view',
  imports: [RouterLink, ReactiveFormsModule, AddNodePanel, ReadinessEditor],
  templateUrl: './tree-view.html',
  styleUrl: './tree-view.css',
  host: {
    '(document:keydown.escape)': 'cancelPending()',
    '(window:beforeunload)': 'onBeforeUnload($event)',
    '(document:keydown)': 'onKeydown($event)',
  },
})
export class TreeView {
  private readonly treeApi = inject(TreeApi);
  private readonly nodeApi = inject(NodeApi);
  private readonly router = inject(Router);

  /** Route param. */
  readonly id = input.required<string>();
  /** Query param set by the tree details form, to come back into the edit session it was opened from. */
  readonly resumeEdit = input<string>();

  protected readonly readinessLevels = READINESS_LEVELS;
  protected readonly nodeWidth = NODE_WIDTH;
  protected readonly nodeHeight = NODE_HEIGHT;
  protected readonly tools: { id: Tool; label: string }[] = [
    { id: 'select', label: 'Select / move' },
    { id: 'add', label: 'Add node' },
    { id: 'connect', label: 'Connect' },
    { id: 'delete', label: 'Delete' },
  ];

  private readonly canvas = viewChild<ElementRef<SVGSVGElement>>('canvas');

  protected readonly tree = signal<Tree | null>(null);
  protected readonly treeNodes = signal<TreeNode[]>([]);
  protected readonly edges = signal<Prerequisite[]>([]);
  protected readonly libraryNodes = signal<Node[]>([]);
  protected readonly error = signal<string | null>(null);

  protected readonly mode = signal<'view' | 'edit'>('view');
  /** The tree has an edit session that this page didn't start or resume (V9). */
  protected readonly unfinishedSession = signal(false);
  protected readonly tool = signal<Tool>('select');
  /** Undo and redo for the current edit session (5.3b). */
  protected readonly history = new UndoHistory();
  /** An undo or redo is being saved; the buttons wait for it. */
  protected readonly historyBusy = signal(false);
  /** Library nodes deleted by undo and recreated by redo: original id → current id. */
  private readonly nodeAliases = new Map<number, number>();
  private readonly editContext: EditContext = {
    treeApi: this.treeApi,
    nodeApi: this.nodeApi,
    treeId: () => this.treeId(),
    resolve: (nodeId) => this.resolveNode(nodeId),
    alias: (oldNodeId, newNodeId) => this.nodeAliases.set(oldNodeId, newNodeId),
    placed: (nodeId) => {
      const current = this.resolveNode(nodeId);
      const found = this.treeNodes().find((n) => n.nodeId === current);
      if (!found) {
        throw new Error('That node is no longer in the tree');
      }
      return found;
    },
    edgeBetween: (prerequisiteNodeId, dependentNodeId) => {
      const byId = this.byId();
      return this.edges().find(
        (e) =>
          byId.get(e.prerequisiteTreeNodeId)?.nodeId === this.resolveNode(prerequisiteNodeId) &&
          byId.get(e.dependentTreeNodeId)?.nodeId === this.resolveNode(dependentNodeId),
      );
    },
  };
  protected readonly selectedId = signal<number | null>(null);
  /** Connect tool: the prerequisite picked by the first click. */
  protected readonly connectFromId = signal<number | null>(null);
  /** Add tool: where the user clicked, waiting for them to choose a node. */
  protected readonly pendingPoint = signal<Point | null>(null);

  private drag: Drag | null = null;
  private segmentDrag: SegmentDrag | null = null;
  /** Routes of edges being dragged right now, shown instead of their stored routes. */
  private readonly routeOverrides = signal<ReadonlyMap<number, EdgeRoute>>(new Map());
  protected readonly pointsAttr = pointsAttr;
  /** The view box is held still while dragging, so the canvas doesn't rescale under the pointer. */
  private readonly frozenViewBox = signal<ViewBox | null>(null);

  protected readonly thresholdForm = new FormGroup({
    aggregateThreshold: new FormControl(80, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0), Validators.max(100)],
    }),
    individualThreshold: new FormControl(70, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0), Validators.max(100)],
    }),
  });

  protected readonly byId = computed(() => new Map(this.treeNodes().map((n) => [n.id, n])));
  protected readonly states = computed(() => {
    const byId = this.byId();
    return new Map<number, ReadinessState>(this.treeNodes().map((n) => [n.id, readinessState(n, byId)]));
  });
  protected readonly viewBox = computed(() => this.frozenViewBox() ?? viewBoxFor(this.treeNodes()));
  protected readonly routedEdges = computed(() => routeEdges(this.edges(), this.byId(), this.routeOverrides()));
  protected readonly selected = computed(() => {
    const id = this.selectedId();
    return id === null ? null : (this.byId().get(id) ?? null);
  });
  /** Library nodes that can still be added (a node can appear in a tree only once). */
  protected readonly availableNodes = computed(() => {
    const used = new Set(this.treeNodes().map((n) => n.nodeId));
    return this.libraryNodes().filter((n) => !used.has(n.id));
  });
  protected readonly hint = computed(() => {
    if (this.mode() === 'view') {
      return 'Click a node to see its details and update its readiness. Click "Edit" to change the tree.';
    }
    switch (this.tool()) {
      case 'select':
        return 'Click a node to see and edit its details. Drag a node to move it, or drag a piece of an arrow to reroute it.';
      case 'add':
        return 'Click an empty spot on the canvas to add a node there.';
      case 'connect':
        return this.connectFromId() === null
          ? 'Click the prerequisite node first.'
          : `Now click the node that "${this.byId().get(this.connectFromId()!)?.title}" unlocks. Esc cancels.`;
      case 'delete':
        return 'Click a node to remove it from this tree, or click an arrow to remove that prerequisite.';
    }
  });

  constructor() {
    // "Open linked tree" reuses this component for another tree, so load on every id change
    effect(() => {
      const id = Number(this.id());
      untracked(() => this.load(id));
    });
  }

  private load(id: number): void {
    this.tree.set(null);
    this.error.set(null);
    this.mode.set('view');
    this.unfinishedSession.set(false);
    this.clearHistory();
    this.tool.set('select');
    this.selectedId.set(null);
    this.cancelPending();
    forkJoin({
      tree: this.treeApi.get(id),
      treeNodes: this.treeApi.nodes(id),
      edges: this.treeApi.prerequisites(id),
    }).subscribe({
      next: ({ tree, treeNodes, edges }) => {
        this.tree.set(tree);
        this.treeNodes.set(treeNodes);
        this.edges.set(edges);
        if (tree.editSessionStartedAt) {
          if (untracked(this.resumeEdit)) {
            // Back from the details form, inside the same session
            this.mode.set('edit');
            this.router.navigate([], { queryParams: {}, replaceUrl: true });
          } else {
            this.unfinishedSession.set(true);
          }
        }
      },
      error: (error) => this.error.set(errorMessage(error)),
    });
  }

  // ---- Edit mode ----

  protected startEditing(): void {
    this.run(this.treeApi.startEditSession(this.treeId()), () => {
      this.clearHistory();
      this.mode.set('edit');
      this.selectedId.set(null);
    });
  }

  /** "Done": keep every change. */
  protected finishEditing(): void {
    this.run(this.treeApi.finishEditSession(this.treeId()), () => this.leaveEditMode());
  }

  protected discardChanges(): void {
    if (!confirm('Discard changes?\n\nThe tree goes back to how it was when you clicked Edit.')) {
      return;
    }
    this.run(this.treeApi.discardEditSession(this.treeId()), () => this.load(this.treeId()));
  }

  protected keepUnfinished(): void {
    this.run(this.treeApi.finishEditSession(this.treeId()), () => this.unfinishedSession.set(false));
  }

  protected discardUnfinished(): void {
    this.run(this.treeApi.discardEditSession(this.treeId()), () => this.load(this.treeId()));
  }

  /**
   * The route's canDeactivate guard. Leaving edit mode for anywhere but this tree's details
   * form asks first; going anyway discards the changes, since only "Done" keeps them.
   */
  canLeave(nextUrl: string): boolean | Promise<boolean> {
    if (this.mode() !== 'edit' || nextUrl.split('?')[0] === `/trees/${this.treeId()}/edit`) {
      return true;
    }
    if (
      !confirm(
        'Leave without saving?\n\nYou are editing this tree. If you leave now, the changes made since you clicked ' +
          'Edit are discarded.\n\nOK leaves without saving. Cancel stays so you can keep editing.',
      )
    ) {
      return false;
    }
    return firstValueFrom(this.treeApi.discardEditSession(this.treeId())).then(
      () => {
        this.mode.set('view');
        return true;
      },
      (error) => {
        this.error.set(errorMessage(error));
        return false;
      },
    );
  }

  /** Closing or reloading the tab mid-edit: the browser shows its own "Leave site?" box. */
  protected onBeforeUnload(event: BeforeUnloadEvent): void {
    if (this.mode() === 'edit') {
      event.preventDefault();
    }
  }

  private leaveEditMode(): void {
    this.clearHistory();
    this.mode.set('view');
    this.tool.set('select');
    this.selectedId.set(null);
    this.cancelPending();
    this.tree.update((tree) => (tree ? { ...tree, editSessionStartedAt: null } : tree));
  }

  // ---- Undo and redo ----

  protected undo(): void {
    const command = this.history.nextUndo();
    if (command && !this.historyBusy()) {
      this.runHistory('undo', command.undo(), () => this.history.undid(command));
    }
  }

  protected redo(): void {
    const command = this.history.nextRedo();
    if (command && !this.historyBusy()) {
      this.runHistory('redo', command.redo(), () => this.history.redid(command));
    }
  }

  /** Ctrl+Z undoes; Ctrl+Y or Ctrl+Shift+Z redoes. Text fields keep their own undo. */
  protected onKeydown(event: KeyboardEvent): void {
    if (this.mode() !== 'edit' || !(event.ctrlKey || event.metaKey)) {
      return;
    }
    if (event.target instanceof Element && event.target.closest('input, textarea, select')) {
      return;
    }
    const key = event.key.toLowerCase();
    if (key === 'z' && !event.shiftKey) {
      event.preventDefault();
      this.undo();
    } else if (key === 'y' || (key === 'z' && event.shiftKey)) {
      event.preventDefault();
      this.redo();
    }
  }

  /**
   * Saves an undo or redo, then re-reads the tree. If it fails (the tree has changed in a way
   * the step can't follow), the history is cleared so it can't drift from what's saved.
   */
  private runHistory(action: 'undo' | 'redo', step: Observable<unknown>, onDone: () => void): void {
    this.historyBusy.set(true);
    this.error.set(null);
    this.cancelPending();
    step.subscribe({
      error: (error) => {
        this.historyBusy.set(false);
        const reason = error instanceof Error ? error.message : errorMessage(error);
        this.error.set(`Couldn't ${action}: ${reason}. The undo history has been cleared.`);
        this.history.clear();
        this.reloadContents();
      },
      complete: () => {
        onDone();
        this.historyBusy.set(false);
        this.reloadContents();
      },
    });
  }

  private record(command: EditCommand): void {
    this.history.record(command);
  }

  private clearHistory(): void {
    this.history.clear();
    this.nodeAliases.clear();
  }

  private resolveNode(nodeId: number): number {
    let current = nodeId;
    while (this.nodeAliases.has(current)) {
      current = this.nodeAliases.get(current)!;
    }
    return current;
  }

  // ---- Toolbar ----

  protected setTool(tool: Tool): void {
    this.tool.set(tool);
    this.cancelPending();
    if (tool !== 'select') {
      this.selectedId.set(null);
    }
    if (tool === 'add') {
      this.nodeApi.list().subscribe({
        next: (nodes) => this.libraryNodes.set(nodes),
        error: (error) => this.error.set(errorMessage(error)),
      });
    }
  }

  protected cancelPending(): void {
    this.connectFromId.set(null);
    this.pendingPoint.set(null);
  }

  protected resetLayout(): void {
    if (!confirm('Reset to auto-layout? Every node will be moved, replacing the positions you set by hand.')) {
      return;
    }
    const positionsOf = (nodes: TreeNode[]) =>
      nodes.map((n) => ({ nodeId: n.nodeId, positionX: n.positionX, positionY: n.positionY }));
    const before = positionsOf(this.treeNodes());
    const routesBefore = this.edges()
      .filter((e) => e.route)
      .map((e) => this.endsOf(e.id)!.ends);
    // Every edge goes back to its default route too (5.4-E3b)
    const laidOut = this.treeApi
      .updatePositions(this.treeId(), autoLayout(this.treeNodes()))
      .pipe(switchMap((treeNodes) => this.treeApi.resetRoutes(this.treeId()).pipe(map(() => treeNodes))));
    this.run(laidOut, (treeNodes) => {
      this.treeNodes.set(treeNodes);
      this.edges.update((edges) => edges.map((e) => ({ ...e, route: null })));
      this.record(commands.layout(this.editContext, before, positionsOf(treeNodes), routesBefore));
    });
  }

  protected delete(tree: Tree): void {
    if (!confirmTreeDelete(tree.title)) {
      return;
    }
    this.run(this.treeApi.delete(tree.id), () => {
      this.mode.set('view'); // the edit session went with the tree
      this.router.navigateByUrl('/trees');
    });
  }

  // ---- Canvas events ----

  protected onCanvasClick(event: MouseEvent): void {
    if (this.mode() === 'view') {
      this.selectedId.set(null);
    } else if (this.tool() === 'add') {
      const point = this.toCanvasPoint(event);
      this.pendingPoint.set({ x: Math.round(point.x), y: Math.round(point.y) });
    } else if (this.tool() === 'select') {
      this.selectedId.set(null);
    } else {
      this.cancelPending();
    }
  }

  protected onNodeMouseDown(event: MouseEvent, treeNode: TreeNode): void {
    if (this.mode() !== 'edit' || this.tool() !== 'select' || event.button !== 0) {
      return;
    }
    event.preventDefault();
    this.drag = {
      treeNodeId: treeNode.id,
      startPointer: this.toCanvasPoint(event),
      startPosition: { x: treeNode.positionX, y: treeNode.positionY },
      moved: false,
    };
    this.frozenViewBox.set(this.viewBox());
  }

  /** Press on a draggable segment of an edge: select tool, edit mode only. */
  protected onSegmentMouseDown(event: MouseEvent, edge: RoutedEdge, handle: SegmentHandle): void {
    if (this.mode() !== 'edit' || this.tool() !== 'select' || event.button !== 0) {
      return;
    }
    event.preventDefault();
    event.stopPropagation();
    this.segmentDrag = {
      edgeId: edge.id,
      handle,
      segments: edge.segments,
      startPointer: this.toCanvasPoint(event),
      startOffsets: edge.offsets,
      moved: false,
    };
    this.frozenViewBox.set(this.viewBox());
  }

  protected onCanvasMouseMove(event: MouseEvent): void {
    const segmentDrag = this.segmentDrag;
    if (segmentDrag) {
      const pointer = this.toCanvasPoint(event);
      const axis = segmentDrag.handle.axis;
      const delta = Math.round(pointer[axis] - segmentDrag.startPointer[axis]);
      if (!segmentDrag.moved && Math.abs(delta) < DRAG_THRESHOLD) {
        return;
      }
      segmentDrag.moved = true;
      const offsets = [...segmentDrag.startOffsets];
      offsets[segmentDrag.handle.offsetIndex] += delta;
      this.routeOverrides.set(new Map([[segmentDrag.edgeId, { segments: segmentDrag.segments, offsets }]]));
      return;
    }
    const drag = this.drag;
    if (!drag) {
      return;
    }
    const pointer = this.toCanvasPoint(event);
    const dx = pointer.x - drag.startPointer.x;
    const dy = pointer.y - drag.startPointer.y;
    if (!drag.moved && Math.hypot(dx, dy) < DRAG_THRESHOLD) {
      return;
    }
    drag.moved = true;
    const positionX = Math.round(drag.startPosition.x + dx);
    const positionY = Math.round(drag.startPosition.y + dy);
    this.treeNodes.update((nodes) =>
      nodes.map((n) => (n.id === drag.treeNodeId ? { ...n, positionX, positionY } : n)),
    );
  }

  /** Ends a press on a node or segment: saves a drag, or selects a node that was only clicked. */
  protected onCanvasMouseUp(): void {
    if (this.segmentDrag) {
      this.endSegmentDrag(this.segmentDrag);
      return;
    }
    const drag = this.drag;
    if (!drag) {
      return;
    }
    this.drag = null;
    this.frozenViewBox.set(null);
    const treeNode = this.byId().get(drag.treeNodeId);
    if (!treeNode) {
      return;
    }
    if (drag.moved) {
      const before = { ...commands.stateOf(treeNode), positionX: drag.startPosition.x, positionY: drag.startPosition.y };
      this.saveTreeNode(treeNode, `move "${treeNode.title}"`, before);
    } else {
      this.toggleSelected(treeNode);
    }
  }

  /** Click or Enter on a node, for every tool except the press/drag handling of "select". */
  protected onNodeActivate(event: Event, treeNode: TreeNode): void {
    event.stopPropagation();
    if (this.mode() === 'view') {
      this.toggleSelected(treeNode);
      return;
    }
    switch (this.tool()) {
      case 'select':
        // Mouse selection happens on mouseup; this path is for the keyboard
        if (event.type === 'keydown') {
          this.toggleSelected(treeNode);
        }
        break;
      case 'connect':
        this.connect(treeNode);
        break;
      case 'delete':
        this.removeTreeNode(treeNode);
        break;
      case 'add':
        this.pendingPoint.set(null);
        break;
    }
  }

  protected onEdgeClick(event: Event, edgeId: number): void {
    event.stopPropagation();
    if (this.mode() === 'edit' && this.tool() === 'delete') {
      const ends = this.endsOf(edgeId);
      this.run(this.treeApi.removePrerequisite(this.treeId(), edgeId), () => {
        if (ends) {
          this.record(commands.removeEdge(this.editContext, `delete arrow ${ends.label}`, ends.ends));
        }
        this.reloadContents();
      });
    }
  }

  // ---- Actions ----

  /** A hand-entered readiness changed in view mode; other nodes' states may follow it. */
  protected readinessSaved(): void {
    this.reloadContents();
  }

  protected saveThresholds(treeNode: TreeNode): void {
    if (this.thresholdForm.invalid) {
      this.thresholdForm.markAllAsTouched();
      return;
    }
    this.saveTreeNode(
      { ...treeNode, ...this.thresholdForm.getRawValue() },
      `thresholds of "${treeNode.title}"`,
      commands.stateOf(treeNode),
    );
  }

  protected removeTreeNode(treeNode: TreeNode): void {
    if (
      !confirm(
        `Remove "${treeNode.title}" from this tree?\n\n` +
          'Its prerequisite arrows in this tree are removed too. The node stays in your library.',
      )
    ) {
      return;
    }
    const edges: EdgeEnds[] = this.edges()
      .filter((e) => e.prerequisiteTreeNodeId === treeNode.id || e.dependentTreeNodeId === treeNode.id)
      .map((e) => this.endsOf(e.id)!.ends);
    this.run(this.treeApi.removeNode(this.treeId(), treeNode.id), () => {
      if (this.selectedId() === treeNode.id) {
        this.selectedId.set(null);
      }
      this.record(
        commands.removeNode(this.editContext, `remove "${treeNode.title}"`, treeNode.nodeId, commands.stateOf(treeNode), edges),
      );
      this.reloadContents();
    });
  }

  protected placeExisting(nodeId: number): void {
    const point = this.pendingPoint();
    if (!point) {
      return;
    }
    this.run(this.treeApi.addNode(this.treeId(), { nodeId, positionX: point.x, positionY: point.y }), (added) => {
      this.pendingPoint.set(null);
      this.record(commands.placeNode(this.editContext, `add "${added.title}"`, nodeId, commands.stateOf(added)));
      this.reloadContents();
    });
  }

  protected createAndPlace(title: string): void {
    const point = this.pendingPoint();
    if (!point) {
      return;
    }
    const placed = this.nodeApi
      .create({ title, description: null, readiness: 0, links: [] })
      .pipe(
        switchMap((node) => {
          this.libraryNodes.update((nodes) => [...nodes, node]);
          return this.treeApi.addNode(this.treeId(), { nodeId: node.id, positionX: point.x, positionY: point.y });
        }),
      );
    this.run(placed, (added) => {
      this.pendingPoint.set(null);
      this.record(commands.createNode(this.editContext, `create "${title}"`, added.nodeId, title, commands.stateOf(added)));
      this.reloadContents();
    });
  }

  // ---- Template helpers ----

  protected stateOf(treeNode: TreeNode): ReadinessState {
    return this.states().get(treeNode.id) ?? 'not-started';
  }

  /** The node's readiness level as words, e.g. "not started". */
  protected levelOf(treeNode: TreeNode): string {
    return readinessLabel(this.stateOf(treeNode));
  }

  protected titlesOf(ids: number[]): TreeNode[] {
    return ids.map((id) => this.byId().get(id)).filter((n): n is TreeNode => n !== undefined);
  }

  protected ariaLabel(treeNode: TreeNode): string {
    const linked = treeNode.linkedTree ? `, from linked tree ${treeNode.linkedTree.title}` : '';
    return `${treeNode.title}, ${treeNode.readiness}% ready${linked}, ${this.levelOf(treeNode)}`;
  }

  /** SVG text doesn't wrap, so long titles are shortened to fit the box. */
  protected shortTitle(title: string): string {
    return title.length > 20 ? `${title.slice(0, 19)}…` : title;
  }

  // ---- Internals ----

  private treeId(): number {
    return Number(this.id());
  }

  private toggleSelected(treeNode: TreeNode): void {
    if (this.selectedId() === treeNode.id) {
      this.selectedId.set(null);
      return;
    }
    this.selectedId.set(treeNode.id);
    this.thresholdForm.reset({
      aggregateThreshold: treeNode.aggregateThreshold,
      individualThreshold: treeNode.individualThreshold,
    });
  }

  private connect(treeNode: TreeNode): void {
    const fromId = this.connectFromId();
    if (fromId === null) {
      this.connectFromId.set(treeNode.id);
      return;
    }
    this.connectFromId.set(null);
    if (fromId === treeNode.id) {
      return;
    }
    const from = this.byId().get(fromId)!;
    this.run(this.treeApi.addPrerequisite(this.treeId(), fromId, treeNode.id), () => {
      this.record(
        commands.addEdge(this.editContext, `connect "${from.title}" to "${treeNode.title}"`, {
          prerequisiteNodeId: from.nodeId,
          dependentNodeId: treeNode.nodeId,
        }),
      );
      this.reloadContents();
    });
  }

  /**
   * Saves a move or a threshold change, and records it for undo with the state it replaced.
   * A move that changes an edge's number of segments resets that edge's route (5.4-E6b).
   */
  private saveTreeNode(treeNode: TreeNode, label: string, before: NodeState): void {
    const request = commands.stateOf(treeNode);
    const byId = new Map(this.byId()).set(treeNode.id, treeNode);
    const outgrown = this.edges().filter(
      (e) => (e.prerequisiteTreeNodeId === treeNode.id || e.dependentTreeNodeId === treeNode.id) && routeOutgrown(e, byId),
    );
    const resetRoutes = outgrown.map((e) => this.endsOf(e.id)!.ends);
    const saved$ = this.treeApi.updateNode(this.treeId(), treeNode.id, request).pipe(
      switchMap((saved) =>
        outgrown.length
          ? concat(...outgrown.map((e) => this.treeApi.updateRoute(this.treeId(), e.id, null))).pipe(
              last(),
              map(() => saved),
            )
          : of(saved),
      ),
    );
    this.error.set(null);
    saved$.subscribe({
      next: (saved) => {
        this.treeNodes.update((nodes) => nodes.map((n) => (n.id === saved.id ? saved : n)));
        const reset = new Set(outgrown.map((e) => e.id));
        this.edges.update((edges) => edges.map((e) => (reset.has(e.id) ? { ...e, route: null } : e)));
        this.record(commands.updateNode(this.editContext, label, treeNode.nodeId, before, request, resetRoutes));
      },
      error: (error) => {
        this.error.set(errorMessage(error));
        this.reloadContents(); // put the node back where the server has it
      },
    });
  }

  /** An edge by the library nodes at its ends (with its route), for undo, and a label like `"A" → "B"`. */
  private endsOf(edgeId: number): { ends: EdgeEnds; label: string } | null {
    const edge = this.edges().find((e) => e.id === edgeId);
    const from = edge && this.byId().get(edge.prerequisiteTreeNodeId);
    const to = edge && this.byId().get(edge.dependentTreeNodeId);
    if (!edge || !from || !to) {
      return null;
    }
    return {
      ends: { prerequisiteNodeId: from.nodeId, dependentNodeId: to.nodeId, route: edge.route },
      label: `"${from.title}" → "${to.title}"`,
    };
  }

  /** Saves a dragged segment as the edge's new route, and records it for undo. */
  private endSegmentDrag(segmentDrag: SegmentDrag): void {
    this.segmentDrag = null;
    this.frozenViewBox.set(null);
    const routed = this.routedEdges().find((e) => e.id === segmentDrag.edgeId);
    const ends = this.endsOf(segmentDrag.edgeId);
    if (!segmentDrag.moved || !routed || !ends) {
      this.routeOverrides.set(new Map());
      return;
    }
    // Store what's shown, so a drag past the ends doesn't keep an offset that's clamped away
    const route: EdgeRoute = { segments: routed.segments, offsets: routed.offsets };
    this.error.set(null);
    this.treeApi.updateRoute(this.treeId(), segmentDrag.edgeId, route).subscribe({
      next: (saved) => {
        this.edges.update((edges) => edges.map((e) => (e.id === saved.id ? saved : e)));
        this.routeOverrides.set(new Map());
        this.record(commands.reroute(this.editContext, `reshape arrow ${ends.label}`, ends.ends, ends.ends.route ?? null, route));
      },
      error: (error) => {
        this.routeOverrides.set(new Map());
        this.error.set(errorMessage(error));
      },
    });
  }

  /** Re-reads the tree's nodes and edges after a change to its structure. */
  private reloadContents(): void {
    const id = this.treeId();
    forkJoin({ treeNodes: this.treeApi.nodes(id), edges: this.treeApi.prerequisites(id) }).subscribe({
      next: ({ treeNodes, edges }) => {
        this.treeNodes.set(treeNodes);
        this.edges.set(edges);
      },
      error: (error) => this.error.set(errorMessage(error)),
    });
  }

  /** Runs a save, clearing the previous error first and showing any new one. */
  private run<T>(request: Observable<T>, onSuccess: (result: T) => void): void {
    this.error.set(null);
    request.subscribe({ next: onSuccess, error: (error) => this.error.set(errorMessage(error)) });
  }

  /** Converts a mouse position to canvas (tree position) coordinates. */
  private toCanvasPoint(event: MouseEvent): Point {
    const svg = this.canvas()?.nativeElement;
    const matrix = svg?.getScreenCTM?.();
    if (matrix) {
      const point = new DOMPoint(event.clientX, event.clientY).matrixTransform(matrix.inverse());
      return { x: point.x, y: point.y };
    }
    // No layout information (e.g. in unit tests): treat the view box as unscaled
    const box = this.viewBox();
    return { x: box.x + event.clientX, y: box.y + event.clientY };
  }
}
