import { DatePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { QuestionSet } from '../core/api.models';
import { CodingApi } from '../core/coding-api';
import { errorMessage, problemOf } from '../core/problem';

/** The user's coding question sets (`/question-sets`), each with how many questions are solved. */
@Component({
  selector: 'app-question-set-list',
  imports: [RouterLink, DatePipe],
  templateUrl: './question-set-list.html',
})
export class QuestionSetList {
  private readonly codingApi = inject(CodingApi);

  /** null until the first load finishes. */
  protected readonly sets = signal<QuestionSet[] | null>(null);
  protected readonly error = signal<string | null>(null);
  /** Nodes that blocked the last delete, so the user can go and remove the set from them. */
  protected readonly blockingNodes = signal<{ id: number; title: string }[]>([]);

  constructor() {
    this.load();
  }

  protected delete(set: QuestionSet): void {
    if (
      !confirm(`Delete "${set.title}"? Its questions and your attempts on them are deleted too.`)
    ) {
      return;
    }
    this.error.set(null);
    this.blockingNodes.set([]);
    this.codingApi.deleteSet(set.id).subscribe({
      next: () => this.load(),
      error: (error) => {
        this.error.set(errorMessage(error));
        this.blockingNodes.set(problemOf(error)?.nodes ?? []);
      },
    });
  }

  private load(): void {
    this.codingApi.sets().subscribe({
      next: (sets) => this.sets.set(sets),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }
}
