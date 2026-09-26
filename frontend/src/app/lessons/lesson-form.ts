import { Component, OnInit, inject, input, signal } from '@angular/core';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { LessonRequest, LessonSection } from '../core/api.models';
import { LessonApi } from '../core/lesson-api';
import { errorMessage } from '../core/problem';

type SectionGroup = FormGroup<{ title: FormControl<string>; body: FormControl<string> }>;

/**
 * Create (`/lessons/new`) or edit (`/lessons/:id/edit`) a lesson: its title, summary and
 * Markdown sections in order. Editing isn't opening the lesson, so it doesn't count as a review.
 */
@Component({
  selector: 'app-lesson-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './lesson-form.html',
})
export class LessonForm implements OnInit {
  private readonly lessonApi = inject(LessonApi);
  private readonly router = inject(Router);

  /** Route param; absent when creating. */
  readonly id = input<string>();

  protected readonly form = new FormGroup({
    title: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(200)],
    }),
    summary: new FormControl('', { nonNullable: true }),
    sections: new FormArray<SectionGroup>([]),
  });

  protected readonly error = signal<string | null>(null);
  protected readonly saving = signal(false);

  ngOnInit(): void {
    const id = this.id();
    if (id) {
      this.lessonApi.get(Number(id)).subscribe({
        next: (lesson) => {
          this.form.patchValue({ title: lesson.title, summary: lesson.summary ?? '' });
          lesson.sections.forEach((section) => this.addSection(section));
        },
        error: (error) => this.error.set(errorMessage(error)),
      });
    } else {
      this.addSection();
    }
  }

  protected get sections(): FormArray<SectionGroup> {
    return this.form.controls.sections;
  }

  protected addSection(section?: LessonSection): void {
    this.sections.push(
      new FormGroup({
        title: new FormControl(section?.title ?? '', {
          nonNullable: true,
          validators: [Validators.required, Validators.maxLength(200)],
        }),
        body: new FormControl(section?.body ?? '', {
          nonNullable: true,
          validators: [Validators.required],
        }),
      }),
    );
  }

  protected removeSection(index: number): void {
    this.sections.removeAt(index);
  }

  /** Swaps the section with its neighbour, `by` -1 (up) or +1 (down). */
  protected move(index: number, by: -1 | 1): void {
    const other = index + by;
    if (other < 0 || other >= this.sections.length) {
      return;
    }
    const control = this.sections.at(index);
    this.sections.removeAt(index);
    this.sections.insert(other, control);
  }

  protected save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const value = this.form.getRawValue();
    const request: LessonRequest = {
      title: value.title.trim(),
      summary: value.summary.trim() || null,
      sections: value.sections.map((section) => ({
        title: section.title.trim(),
        body: section.body,
      })),
    };
    const id = this.id();
    const save$ = id ? this.lessonApi.update(Number(id), request) : this.lessonApi.create(request);
    this.saving.set(true);
    this.error.set(null);
    save$.subscribe({
      next: (lesson) => this.router.navigateByUrl(`/lessons/${lesson.id}`),
      error: (error) => {
        this.saving.set(false);
        this.error.set(errorMessage(error));
      },
    });
  }

  protected cancelUrl(): string {
    const id = this.id();
    return id ? `/lessons/${id}` : '/lessons';
  }
}
