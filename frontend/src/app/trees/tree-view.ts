import { Component, ElementRef, OnInit, computed, inject, input, signal, viewChild } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Observable, forkJoin, switchMap } from 'rxjs';

import { Node, Prerequisite, Tree, TreeNode } from '../core/api.models';
import { NodeApi } from '../core/node-api';
import { errorMessage } from '../core/problem';
import { TreeApi } from '../core/tree-api';
import { AddNodePanel } from './add-node-panel';
import { autoLayout } from './auto-layout';
import { confirmTreeDelete } from './confirm-delete';
import { ReadinessState, readinessState } from './readiness';
import { NODE_HEIGHT, NODE_WIDTH, Point, ViewBox, edgeLines, viewBoxFor } from './tree-layout';

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

/**
 * A tree drawn as SVG, with a toolbar for editing it. Every change is saved to the backend
 * as soon as it's made (there's no undo yet; that's Milestone 2).
 */
@Component({
  selector: 'app-tree-view',
  imports: [RouterLink, ReactiveFormsModule, AddNodePanel],
  templateUrl: './tree-view.html',
  styleUrl: './tree-view.css',
  host: { '(document:keydown.escape)': 'cancelPending()' },
})
export class TreeView implements OnInit {
  private readonly treeApi = inject(TreeApi);
  private readonly nodeApi = inject(NodeApi);
  private readonly router = inject(Router);

  /** Route param. */
  readonly id = input.required<string>();

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

  protected readonly tool = signal<Tool>('select');
  protected readonly selectedId = signal<number | null>(null);
  /** Connect tool: the prerequisite picked by the first click. */
  protected readonly connectFromId = signal<number | null>(null);
  /** Add tool: where the user clicked, waiting for them to choose a node. */
  protected readonly pendingPoint = signal<Point | null>(null);

  private drag: Drag | null = null;
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
  protected readonly edgeLines = computed(() => edgeLines(this.edges(), this.byId()));
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
    switch (this.tool()) {
      case 'select':
        return 'Click a node to see and edit its details. Drag a node to move it.';
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

  ngOnInit(): void {
    const id = this.treeId();
    forkJoin({
      tree: this.treeApi.get(id),
      treeNodes: this.treeApi.nodes(id),
      edges: this.treeApi.prerequisites(id),
    }).subscribe({
      next: ({ tree, treeNodes, edges }) => {
        this.tree.set(tree);
        this.treeNodes.set(treeNodes);
        this.edges.set(edges);
      },
      error: (error) => this.error.set(errorMessage(error)),
    });
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
    this.run(this.treeApi.updatePositions(this.treeId(), autoLayout(this.treeNodes())), (treeNodes) =>
      this.treeNodes.set(treeNodes),
    );
  }

  protected delete(tree: Tree): void {
    if (!confirmTreeDelete(tree.title)) {
      return;
    }
    this.run(this.treeApi.delete(tree.id), () => this.router.navigateByUrl('/trees'));
  }

  // ---- Canvas events ----

  protected onCanvasClick(event: MouseEvent): void {
    if (this.tool() === 'add') {
      const point = this.toCanvasPoint(event);
      this.pendingPoint.set({ x: Math.round(point.x), y: Math.round(point.y) });
    } else if (this.tool() === 'select') {
      this.selectedId.set(null);
    } else {
      this.cancelPending();
    }
  }

  protected onNodeMouseDown(event: MouseEvent, treeNode: TreeNode): void {
    if (this.tool() !== 'select' || event.button !== 0) {
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

  protected onCanvasMouseMove(event: MouseEvent): void {
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

  /** Ends a press on a node: saves the move if it was dragged, otherwise selects it. */
  protected onCanvasMouseUp(): void {
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
      this.saveTreeNode(treeNode);
    } else {
      this.toggleSelected(treeNode);
    }
  }

  /** Click or Enter on a node, for every tool except the press/drag handling of "select". */
  protected onNodeActivate(event: Event, treeNode: TreeNode): void {
    event.stopPropagation();
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
    if (this.tool() === 'delete') {
      this.run(this.treeApi.removePrerequisite(this.treeId(), edgeId), () => this.reloadContents());
    }
  }

  // ---- Actions ----

  protected saveThresholds(treeNode: TreeNode): void {
    if (this.thresholdForm.invalid) {
      this.thresholdForm.markAllAsTouched();
      return;
    }
    this.saveTreeNode({ ...treeNode, ...this.thresholdForm.getRawValue() });
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
    this.run(this.treeApi.removeNode(this.treeId(), treeNode.id), () => {
      if (this.selectedId() === treeNode.id) {
        this.selectedId.set(null);
      }
      this.reloadContents();
    });
  }

  protected placeExisting(nodeId: number): void {
    const point = this.pendingPoint();
    if (!point) {
      return;
    }
    this.run(this.treeApi.addNode(this.treeId(), { nodeId, positionX: point.x, positionY: point.y }), () => {
      this.pendingPoint.set(null);
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
    this.run(placed, () => {
      this.pendingPoint.set(null);
      this.reloadContents();
    });
  }

  // ---- Template helpers ----

  protected stateOf(treeNode: TreeNode): ReadinessState {
    return this.states().get(treeNode.id) ?? 'locked';
  }

  protected titlesOf(ids: number[]): TreeNode[] {
    return ids.map((id) => this.byId().get(id)).filter((n): n is TreeNode => n !== undefined);
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
    this.run(this.treeApi.addPrerequisite(this.treeId(), fromId, treeNode.id), () => this.reloadContents());
  }

  private saveTreeNode(treeNode: TreeNode): void {
    const request = {
      positionX: treeNode.positionX,
      positionY: treeNode.positionY,
      aggregateThreshold: treeNode.aggregateThreshold,
      individualThreshold: treeNode.individualThreshold,
    };
    this.error.set(null);
    this.treeApi.updateNode(this.treeId(), treeNode.id, request).subscribe({
      next: (saved) => this.treeNodes.update((nodes) => nodes.map((n) => (n.id === saved.id ? saved : n))),
      error: (error) => {
        this.error.set(errorMessage(error));
        this.reloadContents(); // put the node back where the server has it
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
