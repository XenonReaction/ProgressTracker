import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Card } from '../core/api.models';
import { FlashcardApi } from '../core/flashcard-api';
import { errorMessage } from '../core/problem';
import { StudySession } from './study-session';

/**
 * The review page (`/review`), in the style of Anki: every card not yet passed, from all
 * decks, never-answered first, then the least recently answered. Passed cards drop off.
 */
@Component({
  selector: 'app-review-page',
  imports: [RouterLink, StudySession],
  template: `
    <h1>Review</h1>
    <p class="muted">
      Every card that hasn't passed yet, from all your decks: never-answered cards first, then the
      least recently answered.
    </p>
    @if (error(); as message) {
      <p class="error" role="alert">{{ message }}</p>
    }
    @if (cards(); as cards) {
      @if (cards.length) {
        <app-study-session [cards]="cards" [showDeck]="true" (again)="load()" />
      } @else {
        <p>
          Nothing to review: every card has passed. <a routerLink="/decks">Back to your decks</a>
        </p>
      }
    } @else if (!error()) {
      <p class="muted">Loading…</p>
    }
  `,
})
export class ReviewPage {
  private readonly flashcardApi = inject(FlashcardApi);

  /** null while loading. */
  protected readonly cards = signal<Card[] | null>(null);
  protected readonly error = signal<string | null>(null);

  constructor() {
    this.load();
  }

  protected load(): void {
    this.error.set(null);
    this.cards.set(null);
    this.flashcardApi.reviewQueue().subscribe({
      next: (cards) => this.cards.set(cards),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }
}
