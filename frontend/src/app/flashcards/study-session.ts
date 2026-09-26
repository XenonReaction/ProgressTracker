import {
  Component,
  computed,
  effect,
  inject,
  input,
  output,
  signal,
  untracked,
} from '@angular/core';

import { Card } from '../core/api.models';
import { FlashcardApi } from '../core/flashcard-api';
import { errorMessage } from '../core/problem';
import { cardStatus } from './card-status';

/**
 * Goes through `cards` once, in order: shows the front, reveals the back, and records the
 * user's own verdict (correct or wrong) before moving on. Ends with a summary and a
 * "Study again" button, which asks the page for a fresh list.
 */
@Component({
  selector: 'app-study-session',
  templateUrl: './study-session.html',
})
export class StudySession {
  private readonly flashcardApi = inject(FlashcardApi);

  readonly cards = input.required<Card[]>();
  /** Show each card's deck, for lists that mix decks. */
  readonly showDeck = input(false);

  /** "Study again" was clicked at the end. */
  readonly again = output<void>();

  protected readonly index = signal(0);
  protected readonly revealed = signal(false);
  /** Each answered card as the server returned it, with its new standing. */
  protected readonly answered = signal<{ card: Card; correct: boolean; wasPassed: boolean }[]>([]);
  protected readonly saving = signal(false);
  protected readonly error = signal<string | null>(null);

  protected readonly current = computed(() => this.cards()[this.index()] ?? null);
  protected readonly correctCount = computed(
    () => this.answered().filter((answer) => answer.correct).length,
  );
  /** Cards that passed during this session (a due card that was already passed doesn't count). */
  protected readonly passedCount = computed(
    () => this.answered().filter((answer) => answer.card.passed && !answer.wasPassed).length,
  );
  /** "Card 2 of 5 · CSS Flexbox · 1 of 3 correct in a row" */
  protected readonly position = computed(() => {
    const card = this.current();
    if (!card) {
      return '';
    }
    const deck = this.showDeck() ? [card.deckTitle] : [];
    return [`Card ${this.index() + 1} of ${this.cards().length}`, ...deck, cardStatus(card)].join(
      ' · ',
    );
  });

  constructor() {
    // A new list starts a new session
    effect(() => {
      this.cards();
      untracked(() => {
        this.index.set(0);
        this.revealed.set(false);
        this.answered.set([]);
        this.error.set(null);
      });
    });
  }

  protected answer(correct: boolean): void {
    const card = this.current();
    if (!card || this.saving()) {
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    this.flashcardApi.review(card, correct).subscribe({
      next: (updated) => {
        this.saving.set(false);
        this.answered.update((answers) => [
          ...answers,
          { card: updated, correct, wasPassed: card.passed },
        ]);
        this.revealed.set(false);
        this.index.update((index) => index + 1);
      },
      error: (error) => {
        this.saving.set(false);
        this.error.set(errorMessage(error));
      },
    });
  }
}
