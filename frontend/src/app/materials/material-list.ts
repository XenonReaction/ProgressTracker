import { DatePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Material } from '../core/api.models';
import { MaterialApi } from '../core/material-api';
import { errorMessage, problemOf } from '../core/problem';

/** The user's external materials (`/materials`), each with the progress they last reported. */
@Component({
  selector: 'app-material-list',
  imports: [RouterLink, DatePipe],
  templateUrl: './material-list.html',
})
export class MaterialList {
  private readonly materialApi = inject(MaterialApi);

  /** null until the first load finishes. */
  protected readonly materials = signal<Material[] | null>(null);
  protected readonly error = signal<string | null>(null);
  /** Nodes that blocked the last delete, so the user can go and remove the material from them. */
  protected readonly blockingNodes = signal<{ id: number; title: string }[]>([]);

  constructor() {
    this.load();
  }

  protected delete(material: Material): void {
    if (!confirm(`Delete "${material.title}"? Its progress history is deleted too.`)) {
      return;
    }
    this.error.set(null);
    this.blockingNodes.set([]);
    this.materialApi.delete(material.id).subscribe({
      next: () => this.load(),
      error: (error) => {
        this.error.set(errorMessage(error));
        this.blockingNodes.set(problemOf(error)?.nodes ?? []);
      },
    });
  }

  private load(): void {
    this.materialApi.list().subscribe({
      next: (materials) => this.materials.set(materials),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }
}
