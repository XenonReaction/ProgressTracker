import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Node } from '../core/api.models';
import { NodeApi } from '../core/node-api';
import { errorMessage, problemOf } from '../core/problem';

@Component({
  selector: 'app-node-list',
  imports: [RouterLink],
  templateUrl: './node-list.html',
})
export class NodeList {
  private readonly nodeApi = inject(NodeApi);

  /** null until the first load finishes. */
  protected readonly nodes = signal<Node[] | null>(null);
  protected readonly error = signal<string | null>(null);
  /** Trees that blocked the last delete, so the user can go and remove the node from them. */
  protected readonly blockingTrees = signal<{ id: number; title: string }[]>([]);

  constructor() {
    this.load();
  }

  protected delete(node: Node): void {
    if (!confirm(`Delete "${node.title}" from your library?`)) {
      return;
    }
    this.error.set(null);
    this.blockingTrees.set([]);
    this.nodeApi.delete(node.id).subscribe({
      next: () => this.load(),
      error: (error) => {
        this.error.set(errorMessage(error));
        this.blockingTrees.set(problemOf(error)?.trees ?? []);
      },
    });
  }

  private load(): void {
    this.nodeApi.list().subscribe({
      next: (nodes) => this.nodes.set(nodes),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }
}
