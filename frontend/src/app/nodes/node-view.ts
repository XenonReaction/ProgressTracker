import { Component, effect, inject, input, signal, untracked } from '@angular/core';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { Node, NodeResource, NodeResourceType, TreeRef } from '../core/api.models';
import { NodeApi } from '../core/node-api';
import { errorMessage } from '../core/problem';
import { ReadinessEditor } from './readiness-editor';
import {
  RESOURCE_GROUPS,
  countingTrees,
  hasCountingResource,
  resourceTitle,
  titleList,
} from './resources';

/**
 * A library node's page (`/nodes/:id`), in view mode: its fields read-only, its resources
 * grouped by type, the trees using it and where its readiness comes from. Hand-entered readiness can be updated
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

  protected readonly resourceGroups = RESOURCE_GROUPS;
  protected readonly countingTrees = countingTrees;
  protected readonly hasCountingResource = hasCountingResource;
  protected readonly resourceTitle = resourceTitle;
  protected readonly titleList = titleList;

  constructor() {
    // Links between node pages reuse this component, so load on every id change
    effect(() => {
      const id = Number(this.id());
      untracked(() => this.load(id));
    });
  }

  /** The node's resources of one type, in the node's order. */
  protected ofType(node: Node, type: NodeResourceType): NodeResource[] {
    return node.resources.filter((resource) => resource.type === type);
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
