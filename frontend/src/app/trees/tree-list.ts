import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Tree } from '../core/api.models';
import { errorMessage } from '../core/problem';
import { TreeApi } from '../core/tree-api';

@Component({
  selector: 'app-tree-list',
  imports: [RouterLink],
  templateUrl: './tree-list.html',
})
export class TreeList {
  private readonly treeApi = inject(TreeApi);

  protected readonly trees = signal<Tree[] | null>(null);
  protected readonly error = signal<string | null>(null);

  constructor() {
    this.treeApi.list().subscribe({
      next: (trees) => this.trees.set(trees),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }
}
