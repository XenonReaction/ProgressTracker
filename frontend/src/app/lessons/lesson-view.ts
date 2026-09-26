import { DatePipe } from '@angular/common';
import { Component, effect, inject, input, signal, untracked } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { Lesson } from '../core/api.models';
import { LessonApi } from '../core/lesson-api';
import { errorMessage, problemOf } from '../core/problem';
import { renderMarkdown } from '../core/markdown';

/**
 * A lesson's page (`/lessons/:id`), for reading it. Opening it records a review ("last
 * reviewed" is when the user last opened the lesson). Shows the sections as rendered
 * Markdown, and the progress the user entered, with a form to update it.
 */
@Component({
  selector: 'app-lesson-view',
  imports: [RouterLink, DatePipe, ReactiveFormsModule],
  templateUrl: './lesson-view.html',
})
export class LessonView {
  private readonly lessonApi = inject(LessonApi);
  private readonly router = inject(Router);

  /** Route param. */
  readonly id = input.required<string>();

  protected readonly lesson = signal<Lesson | null>(null);
  protected readonly error = signal<string | null>(null);
  protected readonly saving = signal(false);
  /** Nodes that blocked the last delete. */
  protected readonly blockingNodes = signal<{ id: number; title: string }[]>([]);

  protected readonly progressForm = new FormGroup({
    progress: new FormControl(0, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0), Validators.max(100)],
    }),
  });

  protected readonly renderMarkdown = renderMarkdown;

  constructor() {
    effect(() => {
      const id = Number(this.id());
      untracked(() => this.open(id));
    });
  }

  protected recordProgress(lesson: Lesson): void {
    if (this.progressForm.invalid) {
      this.progressForm.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    this.lessonApi.recordProgress(lesson.id, this.progressForm.getRawValue().progress).subscribe({
      next: (updated) => {
        this.saving.set(false);
        this.show(updated);
      },
      error: (error) => {
        this.saving.set(false);
        this.error.set(errorMessage(error));
      },
    });
  }

  protected delete(lesson: Lesson): void {
    if (
      !confirm(
        `Delete the lesson "${lesson.title}"? Its sections and your progress on it are deleted too.`,
      )
    ) {
      return;
    }
    this.blockingNodes.set([]);
    this.lessonApi.delete(lesson.id).subscribe({
      next: () => this.router.navigateByUrl('/lessons'),
      error: (error) => {
        this.error.set(errorMessage(error));
        this.blockingNodes.set(problemOf(error)?.nodes ?? []);
      },
    });
  }

  /** Opening the page to read the lesson is the review; the response is the lesson itself. */
  private open(id: number): void {
    this.error.set(null);
    this.lessonApi.recordOpen(id).subscribe({
      next: (lesson) => this.show(lesson),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }

  private show(lesson: Lesson): void {
    this.lesson.set(lesson);
    this.progressForm.reset({ progress: lesson.progress });
  }
}
