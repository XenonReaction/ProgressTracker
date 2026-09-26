import { Component, effect, inject, input, signal, untracked } from '@angular/core';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { Card, Deck } from '../core/api.models';
import { FlashcardApi } from '../core/flashcard-api';
import { errorMessage } from '../core/problem';
import { StudySession } from './study-session';

/**
 * Studies one deck (`/decks/:id/study`): its cards not yet passed, never-answered first, then
 * the least recently answered. When every card has passed, all of them can be studied anyway.
 */
@Component({
  selector: 'app-deck-study',
  imports: [RouterLink, StudySession],
  templateUrl: './deck-study.html',
})
export class DeckStudy {
  private readonly flashcardApi = inject(FlashcardApi);

  /** Route param. */
  readonly id = input.required<string>();

  protected readonly deck = signal<Deck | null>(null);
  /** null while loading. */
  protected readonly cards = signal<Card[] | null>(null);
  protected readonly error = signal<string | null>(null);

  constructor() {
    effect(() => {
      const id = Number(this.id());
      untracked(() => this.load(id));
    });
  }

  protected reload(): void {
    this.load(Number(this.id()));
  }

  protected studyAll(): void {
    this.flashcardApi.cards(Number(this.id())).subscribe({
      next: (cards) => this.cards.set(cards),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }

  private load(id: number): void {
    this.error.set(null);
    this.cards.set(null);
    forkJoin({
      deck: this.flashcardApi.deck(id),
      cards: this.flashcardApi.reviewQueue(id),
    }).subscribe({
      next: ({ deck, cards }) => {
        this.deck.set(deck);
        this.cards.set(cards);
      },
      error: (error) => this.error.set(errorMessage(error)),
    });
  }
}
