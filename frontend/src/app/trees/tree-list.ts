import { DatePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Tree } from '../core/api.models';
import { errorMessage } from '../core/problem';
import { TreeApi } from '../core/tree-api';
import { confirmTreeDelete } from './confirm-delete';

@Component({
  selector: 'app-tree-list',
  imports: [RouterLink, DatePipe],
  templateUrl: './tree-list.html',
})
export class TreeList {
  private readonly treeApi = inject(TreeApi);

  protected readonly trees = signal<Tree[] | null>(null);
  protected readonly error = signal<string | null>(null);

  constructor() {
    this.load();
  }

  protected delete(tree: Tree): void {
    if (!confirmTreeDelete(tree.title)) {
      return;
    }
    this.error.set(null);
    this.treeApi.delete(tree.id).subscribe({
      next: () => this.load(),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }

  private load(): void {
    this.treeApi.list().subscribe({
      next: (trees) => this.trees.set(trees),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }
}
