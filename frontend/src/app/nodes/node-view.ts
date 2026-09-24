import { Component, effect, inject, input, signal, untracked } from '@angular/core';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { Node, TreeRef } from '../core/api.models';
import { NodeApi } from '../core/node-api';
import { errorMessage } from '../core/problem';
import { ReadinessEditor } from './readiness-editor';

/**
 * A library node's page (`/nodes/:id`), in view mode: its fields read-only, its links, the
 * trees using it and where its readiness comes from. Hand-entered readiness can be updated
 * here; everything else is changed through "Edit".
 */
@Component({
  selector: 'app-node-view',
  imports: [RouterLink, ReadinessEditor],
  templateUrl: './node-view.html',
})
export class NodeView {
  private readonly nodeApi = inject(NodeApi);

  /** Route param. */
  readonly id = input.required<string>();

  protected readonly node = signal<Node | null>(null);
  protected readonly trees = signal<TreeRef[]>([]);
  protected readonly error = signal<string | null>(null);

  constructor() {
    // Links between node pages reuse this component, so load on every id change
    effect(() => {
      const id = Number(this.id());
      untracked(() => this.load(id));
    });
  }

  protected readinessSaved(node: Node): void {
    this.node.set(node);
  }

  private load(id: number): void {
    this.node.set(null);
    this.error.set(null);
    forkJoin({ node: this.nodeApi.get(id), trees: this.nodeApi.treesUsing(id) }).subscribe({
      next: ({ node, trees }) => {
        this.node.set(node);
        this.trees.set(trees);
      },
      error: (error) => this.error.set(errorMessage(error)),
    });
  }
}
