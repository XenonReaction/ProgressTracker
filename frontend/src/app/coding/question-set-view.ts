import { DatePipe } from '@angular/common';
import { Component, effect, inject, input, signal, untracked } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { CodingQuestion, QuestionSet } from '../core/api.models';
import { CodingApi } from '../core/coding-api';
import { errorMessage, problemOf } from '../core/problem';
import { LANGUAGE_NAMES, questionStatus } from './question-status';

/** A question set's page (`/question-sets/:id`): its readiness and its questions, each with where it stands. */
@Component({
  selector: 'app-question-set-view',
  imports: [RouterLink, DatePipe],
  templateUrl: './question-set-view.html',
})
export class QuestionSetView {
  private readonly codingApi = inject(CodingApi);
  private readonly router = inject(Router);

  /** Route param. */
  readonly id = input.required<string>();

  protected readonly set = signal<QuestionSet | null>(null);
  protected readonly questions = signal<CodingQuestion[]>([]);
  protected readonly error = signal<string | null>(null);
  /** Nodes that blocked the last delete of this set. */
  protected readonly blockingNodes = signal<{ id: number; title: string }[]>([]);

  protected readonly languageNames = LANGUAGE_NAMES;
  protected readonly questionStatus = questionStatus;

  constructor() {
    effect(() => {
      const id = Number(this.id());
      untracked(() => this.load(id));
    });
  }

  protected deleteQuestion(question: CodingQuestion): void {
    if (!confirm(`Delete the question "${question.title}"? Your attempts on it are deleted too.`)) {
      return;
    }
    this.error.set(null);
    this.codingApi.deleteQuestion(question.setId, question.id).subscribe({
      next: () => this.load(question.setId),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }

  protected deleteSet(set: QuestionSet): void {
    if (
      !confirm(`Delete "${set.title}"? Its questions and your attempts on them are deleted too.`)
    ) {
      return;
    }
    this.blockingNodes.set([]);
    this.codingApi.deleteSet(set.id).subscribe({
      next: () => this.router.navigateByUrl('/question-sets'),
      error: (error) => {
        this.error.set(errorMessage(error));
        this.blockingNodes.set(problemOf(error)?.nodes ?? []);
      },
    });
  }

  private load(id: number): void {
    this.error.set(null);
    forkJoin({ set: this.codingApi.set(id), questions: this.codingApi.questions(id) }).subscribe({
      next: ({ set, questions }) => {
        this.set.set(set);
        this.questions.set(questions);
      },
      error: (error) => this.error.set(errorMessage(error)),
    });
  }
}
