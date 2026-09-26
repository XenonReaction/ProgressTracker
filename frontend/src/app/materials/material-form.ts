import { Component, OnInit, inject, input, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { MaterialRequest } from '../core/api.models';
import { MaterialApi } from '../core/material-api';
import { errorMessage } from '../core/problem';

/**
 * Create (`/materials/new`) or edit (`/materials/:id/edit`) a material's details. Progress is
 * reported on the material's page; editing the details doesn't count as a review.
 */
@Component({
  selector: 'app-material-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './material-form.html',
})
export class MaterialForm implements OnInit {
  private readonly materialApi = inject(MaterialApi);
  private readonly router = inject(Router);

  /** Route param; absent when creating. */
  readonly id = input<string>();

  protected readonly form = new FormGroup({
    title: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(200)],
    }),
    url: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.maxLength(2048),
        Validators.pattern(/^https?:\/\/\S+$/i),
      ],
    }),
    notes: new FormControl('', { nonNullable: true }),
  });

  protected readonly error = signal<string | null>(null);
  protected readonly saving = signal(false);

  ngOnInit(): void {
    const id = this.id();
    if (id) {
      this.materialApi.get(Number(id)).subscribe({
        next: (material) =>
          this.form.setValue({
            title: material.title,
            url: material.url,
            notes: material.notes ?? '',
          }),
        error: (error) => this.error.set(errorMessage(error)),
      });
    }
  }

  protected save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const value = this.form.getRawValue();
    const request: MaterialRequest = {
      title: value.title.trim(),
      url: value.url.trim(),
      notes: value.notes.trim() || null,
    };
    const id = this.id();
    const save$ = id
      ? this.materialApi.update(Number(id), request)
      : this.materialApi.create(request);
    this.saving.set(true);
    this.error.set(null);
    save$.subscribe({
      next: (material) => this.router.navigateByUrl(`/materials/${material.id}`),
      error: (error) => {
        this.saving.set(false);
        this.error.set(errorMessage(error));
      },
    });
  }

  protected cancelUrl(): string {
    const id = this.id();
    return id ? `/materials/${id}` : '/materials';
  }
}
