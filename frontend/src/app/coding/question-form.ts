import { Component, OnInit, inject, input, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { CodingLanguage, CodingQuestionRequest } from '../core/api.models';
import { CodingApi } from '../core/coding-api';
import { errorMessage } from '../core/problem';
import { LANGUAGE_NAMES } from './question-status';

/**
 * Create (`/question-sets/:id/questions/new`) or edit (`.../questions/:questionId/edit`) a
 * coding question. Editing loads the solution without it counting as revealed.
 */
@Component({
  selector: 'app-question-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './question-form.html',
})
export class QuestionForm implements OnInit {
  private readonly codingApi = inject(CodingApi);
  private readonly router = inject(Router);

  /** Route params: the set, and the question when editing. */
  readonly id = input.required<string>();
  readonly questionId = input<string>();

  protected readonly languages = Object.entries(LANGUAGE_NAMES) as [CodingLanguage, string][];

  protected readonly form = new FormGroup({
    title: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(200)],
    }),
    language: new FormControl<CodingLanguage>('css', { nonNullable: true }),
    problem: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    examples: new FormControl('', { nonNullable: true }),
    solution: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
  });

  protected readonly error = signal<string | null>(null);
  protected readonly saving = signal(false);

  ngOnInit(): void {
    const questionId = this.questionId();
    if (questionId) {
      this.codingApi.question(Number(this.id()), Number(questionId), true).subscribe({
        next: (question) =>
          this.form.setValue({
            title: question.title,
            language: question.language,
            problem: question.problem,
            examples: question.examples ?? '',
            solution: question.solution ?? '',
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
    const request: CodingQuestionRequest = {
      title: value.title.trim(),
      language: value.language,
      problem: value.problem,
      examples: value.examples.trim() ? value.examples : null,
      solution: value.solution,
    };
    const setId = Number(this.id());
    const questionId = this.questionId();
    const save$ = questionId
      ? this.codingApi.updateQuestion(setId, Number(questionId), request)
      : this.codingApi.createQuestion(setId, request);
    this.saving.set(true);
    this.error.set(null);
    save$.subscribe({
      next: () => this.router.navigateByUrl(`/question-sets/${setId}`),
      error: (error) => {
        this.saving.set(false);
        this.error.set(errorMessage(error));
      },
    });
  }
}
