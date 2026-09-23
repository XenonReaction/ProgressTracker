import { Component, OnInit, computed, inject, input, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { Tree, TreeNode } from '../core/api.models';
import { errorMessage } from '../core/problem';
import { TreeApi } from '../core/tree-api';
import { confirmTreeDelete } from './confirm-delete';
import { ReadinessState, readinessState } from './readiness';
import { NODE_HEIGHT, NODE_WIDTH, edgeLines, viewBoxFor } from './tree-layout';

/**
 * Read-only tree: nodes at their stored positions, prerequisite edges as arrows, and
 * ready/locked styling. Clicking a node shows what it needs and what it unlocks.
 */
@Component({
  selector: 'app-tree-view',
  imports: [RouterLink],
  templateUrl: './tree-view.html',
  styleUrl: './tree-view.css',
})
export class TreeView implements OnInit {
  private readonly treeApi = inject(TreeApi);
  private readonly router = inject(Router);

  /** Route param. */
  readonly id = input.required<string>();

  protected readonly nodeWidth = NODE_WIDTH;
  protected readonly nodeHeight = NODE_HEIGHT;

  protected readonly tree = signal<Tree | null>(null);
  protected readonly treeNodes = signal<TreeNode[]>([]);
  protected readonly error = signal<string | null>(null);
  protected readonly selectedId = signal<number | null>(null);

  protected readonly byId = computed(() => new Map(this.treeNodes().map((n) => [n.id, n])));
  protected readonly states = computed(() => {
    const byId = this.byId();
    return new Map<number, ReadinessState>(this.treeNodes().map((n) => [n.id, readinessState(n, byId)]));
  });
  protected readonly viewBox = computed(() => viewBoxFor(this.treeNodes()));
  protected readonly edges = computed(() => edgeLines(this.treeNodes(), this.byId()));
  protected readonly selected = computed(() => {
    const id = this.selectedId();
    return id === null ? null : (this.byId().get(id) ?? null);
  });

  ngOnInit(): void {
    const id = Number(this.id());
    forkJoin({ tree: this.treeApi.get(id), treeNodes: this.treeApi.nodes(id) }).subscribe({
      next: ({ tree, treeNodes }) => {
        this.tree.set(tree);
        this.treeNodes.set(treeNodes);
      },
      error: (error) => this.error.set(errorMessage(error)),
    });
  }

  protected delete(tree: Tree): void {
    if (!confirmTreeDelete(tree.title)) {
      return;
    }
    this.error.set(null);
    this.treeApi.delete(tree.id).subscribe({
      next: () => this.router.navigateByUrl('/trees'),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }

  protected select(treeNode: TreeNode): void {
    this.selectedId.set(this.selectedId() === treeNode.id ? null : treeNode.id);
  }

  protected stateOf(treeNode: TreeNode): ReadinessState {
    return this.states().get(treeNode.id) ?? 'locked';
  }

  protected titlesOf(ids: number[]): TreeNode[] {
    return ids.map((id) => this.byId().get(id)).filter((n): n is TreeNode => n !== undefined);
  }

  /** SVG text doesn't wrap, so long titles are shortened to fit the box. */
  protected shortTitle(title: string): string {
    return title.length > 22 ? `${title.slice(0, 21)}…` : title;
  }
}
