import { Component, OnInit, inject, input, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { QuestionSetRequest } from '../core/api.models';
import { CodingApi } from '../core/coding-api';
import { errorMessage } from '../core/problem';

/** Create (`/question-sets/new`) or edit (`/question-sets/:id/edit`) a set's details. */
@Component({
  selector: 'app-question-set-form',
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <h1>{{ id() ? 'Edit question set' : 'New question set' }}</h1>
    <form [formGroup]="form" (ngSubmit)="save()">
      <p>
        <label>
          Title<br />
          <input formControlName="title" size="50" />
        </label>
        @if (form.controls.title.touched && form.controls.title.invalid) {
          <br /><span class="error">Title is required (max 200 characters).</span>
        }
      </p>
      <p>
        <label>
          Description<br />
          <textarea formControlName="description" rows="3" cols="50"></textarea>
        </label>
      </p>
      @if (error(); as message) {
        <p class="error" role="alert">{{ message }}</p>
      }
      <p>
        <button type="submit" [disabled]="saving()">Save</button>
        &nbsp;
        <a [routerLink]="id() ? ['/question-sets', id()] : ['/question-sets']">Cancel</a>
      </p>
    </form>
  `,
})
export class QuestionSetForm implements OnInit {
  private readonly codingApi = inject(CodingApi);
  private readonly router = inject(Router);

  /** Route param; absent when creating. */
  readonly id = input<string>();

  protected readonly form = new FormGroup({
    title: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(200)],
    }),
    description: new FormControl('', { nonNullable: true }),
  });

  protected readonly error = signal<string | null>(null);
  protected readonly saving = signal(false);

  ngOnInit(): void {
    const id = this.id();
    if (id) {
      this.codingApi.set(Number(id)).subscribe({
        next: (set) => this.form.setValue({ title: set.title, description: set.description ?? '' }),
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
    const request: QuestionSetRequest = {
      title: value.title.trim(),
      description: value.description.trim() || null,
    };
    const id = this.id();
    const save$ = id
      ? this.codingApi.updateSet(Number(id), request)
      : this.codingApi.createSet(request);
    this.saving.set(true);
    this.error.set(null);
    save$.subscribe({
      next: (set) => this.router.navigateByUrl(`/question-sets/${set.id}`),
      error: (error) => {
        this.saving.set(false);
        this.error.set(errorMessage(error));
      },
    });
  }
}
