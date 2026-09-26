import { DatePipe } from '@angular/common';
import { Component, effect, inject, input, signal, untracked } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { Material, ProgressUpdate } from '../core/api.models';
import { MaterialApi } from '../core/material-api';
import { errorMessage, problemOf } from '../core/problem';

/**
 * A material's page (`/materials/:id`): its link and notes, the progress the user last
 * reported, a form to report new progress, and every earlier report.
 */
@Component({
  selector: 'app-material-view',
  imports: [RouterLink, DatePipe, ReactiveFormsModule],
  templateUrl: './material-view.html',
})
export class MaterialView {
  private readonly materialApi = inject(MaterialApi);
  private readonly router = inject(Router);

  /** Route param. */
  readonly id = input.required<string>();

  protected readonly material = signal<Material | null>(null);
  protected readonly history = signal<ProgressUpdate[]>([]);
  protected readonly error = signal<string | null>(null);
  protected readonly saving = signal(false);
  /** Nodes that blocked the last delete. */
  protected readonly blockingNodes = signal<{ id: number; title: string }[]>([]);

  protected readonly progressForm = new FormGroup({
    progress: new FormControl(0, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0), Validators.max(100)],
    }),
    note: new FormControl('', { nonNullable: true, validators: [Validators.maxLength(2000)] }),
  });

  constructor() {
    effect(() => {
      const id = Number(this.id());
      untracked(() => this.load(id));
    });
  }

  protected recordProgress(material: Material): void {
    if (this.progressForm.invalid) {
      this.progressForm.markAllAsTouched();
      return;
    }
    const { progress, note } = this.progressForm.getRawValue();
    this.saving.set(true);
    this.error.set(null);
    this.materialApi
      .recordProgress(material.id, { progress, note: note.trim() || null })
      .subscribe({
        next: () => {
          this.saving.set(false);
          this.load(material.id);
        },
        error: (error) => {
          this.saving.set(false);
          this.error.set(errorMessage(error));
        },
      });
  }

  protected delete(material: Material): void {
    if (!confirm(`Delete "${material.title}"? Its progress history is deleted too.`)) {
      return;
    }
    this.blockingNodes.set([]);
    this.materialApi.delete(material.id).subscribe({
      next: () => this.router.navigateByUrl('/materials'),
      error: (error) => {
        this.error.set(errorMessage(error));
        this.blockingNodes.set(problemOf(error)?.nodes ?? []);
      },
    });
  }

  private load(id: number): void {
    forkJoin({
      material: this.materialApi.get(id),
      history: this.materialApi.history(id),
    }).subscribe({
      next: ({ material, history }) => {
        this.material.set(material);
        this.history.set(history);
        // Start the next report from where the last one left off
        this.progressForm.reset({ progress: material.progress, note: '' });
      },
      error: (error) => this.error.set(errorMessage(error)),
    });
  }
}
