import { Component, OnInit, inject, input, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { TreeRequest } from '../core/api.models';
import { errorMessage } from '../core/problem';
import { TreeApi } from '../core/tree-api';
import { MAX_TAG_LENGTH, parseTags, tagsValidator } from './tags';

/** Create (`/trees/new`) or edit (`/trees/:id/edit`) a tree's metadata. Contents are edited in Phase 4. */
@Component({
  selector: 'app-tree-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './tree-form.html',
})
export class TreeForm implements OnInit {
  private readonly treeApi = inject(TreeApi);
  private readonly router = inject(Router);

  /** Route param; absent when creating. */
  readonly id = input<string>();

  protected readonly maxTagLength = MAX_TAG_LENGTH;

  protected readonly form = new FormGroup({
    title: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(200)],
    }),
    description: new FormControl('', { nonNullable: true }),
    category: new FormControl('', { nonNullable: true, validators: [Validators.maxLength(100)] }),
    /** Comma-separated in the form; sent to the API as a list. */
    tags: new FormControl('', { nonNullable: true, validators: [tagsValidator] }),
  });

  protected readonly error = signal<string | null>(null);
  protected readonly saving = signal(false);

  ngOnInit(): void {
    const id = this.id();
    if (id) {
      this.treeApi.get(Number(id)).subscribe({
        next: (tree) =>
          this.form.setValue({
            title: tree.title,
            description: tree.description ?? '',
            category: tree.category ?? '',
            tags: tree.tags.join(', '),
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
    const request: TreeRequest = {
      title: value.title.trim(),
      description: value.description.trim() || null,
      category: value.category.trim() || null,
      tags: parseTags(value.tags),
    };
    const id = this.id();
    const save$ = id ? this.treeApi.update(Number(id), request) : this.treeApi.create(request);
    this.saving.set(true);
    this.error.set(null);
    save$.subscribe({
      next: (tree) => this.router.navigateByUrl(`/trees/${tree.id}`),
      error: (error) => {
        this.saving.set(false);
        this.error.set(errorMessage(error));
      },
    });
  }

  protected cancelUrl(): string {
    const id = this.id();
    return id ? `/trees/${id}` : '/trees';
  }
}
