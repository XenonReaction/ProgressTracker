import { DatePipe } from '@angular/common';
import { Component, effect, inject, input, signal, untracked } from '@angular/core';
import { RouterLink } from '@angular/router';

import { CodingQuestion } from '../core/api.models';
import { CodingApi } from '../core/coding-api';
import { renderMarkdown } from '../core/markdown';
import { errorMessage } from '../core/problem';
import { LANGUAGE_NAMES, questionStatus } from './question-status';

/**
 * A coding question's page (`/question-sets/:id/questions/:questionId`): the problem and
 * worked examples, to solve on your own machine. The solution stays hidden until asked for,
 * which is recorded; "Mark solved" records that it's done.
 */
@Component({
  selector: 'app-question-view',
  imports: [RouterLink, DatePipe],
  templateUrl: './question-view.html',
})
export class QuestionView {
  private readonly codingApi = inject(CodingApi);

  /** Route params. */
  readonly id = input.required<string>();
  readonly questionId = input.required<string>();

  protected readonly question = signal<CodingQuestion | null>(null);
  protected readonly error = signal<string | null>(null);
  protected readonly busy = signal(false);

  protected readonly renderMarkdown = renderMarkdown;
  protected readonly languageNames = LANGUAGE_NAMES;
  protected readonly questionStatus = questionStatus;

  constructor() {
    effect(() => {
      const setId = Number(this.id());
      const questionId = Number(this.questionId());
      untracked(() => this.load(setId, questionId));
    });
  }

  protected reveal(question: CodingQuestion): void {
    if (
      !question.solved &&
      !confirm('Show the solution? Seeing it before solving the question is recorded.')
    ) {
      return;
    }
    this.update(this.codingApi.reveal(question.setId, question.id));
  }

  protected markSolved(question: CodingQuestion): void {
    this.update(this.codingApi.markSolved(question.setId, question.id));
  }

  private update(request: ReturnType<CodingApi['reveal']>): void {
    this.busy.set(true);
    this.error.set(null);
    request.subscribe({
      next: (question) => {
        this.busy.set(false);
        this.question.set(question);
      },
      error: (error) => {
        this.busy.set(false);
        this.error.set(errorMessage(error));
      },
    });
  }

  private load(setId: number, questionId: number): void {
    this.error.set(null);
    this.codingApi.question(setId, questionId).subscribe({
      next: (question) => this.question.set(question),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }
}
