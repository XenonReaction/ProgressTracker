import { DatePipe } from '@angular/common';
import { Component, effect, inject, input, signal, untracked } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { Card, Deck } from '../core/api.models';
import { FlashcardApi } from '../core/flashcard-api';
import { errorMessage, problemOf } from '../core/problem';
import { CardForm } from './card-form';
import { COMPLETE_PERCENT, cardStatus } from './card-status';

/** A deck's page (`/decks/:id`): where it stands, and its cards, which are added and edited here. */
@Component({
  selector: 'app-deck-view',
  imports: [RouterLink, DatePipe, CardForm],
  templateUrl: './deck-view.html',
})
export class DeckView {
  private readonly flashcardApi = inject(FlashcardApi);
  private readonly router = inject(Router);

  /** Route param. */
  readonly id = input.required<string>();

  protected readonly deck = signal<Deck | null>(null);
  protected readonly cards = signal<Card[]>([]);
  /** The card being edited in place, if any. */
  protected readonly editingId = signal<number | null>(null);
  protected readonly error = signal<string | null>(null);
  /** Nodes that blocked the last delete of this deck. */
  protected readonly blockingNodes = signal<{ id: number; title: string }[]>([]);

  protected readonly completePercent = COMPLETE_PERCENT;
  protected readonly cardStatus = cardStatus;

  constructor() {
    effect(() => {
      const id = Number(this.id());
      untracked(() => this.load(id));
    });
  }

  /** A card was added or edited: its deck's counts may have changed too. */
  protected cardSaved(): void {
    this.editingId.set(null);
    this.load(Number(this.id()));
  }

  protected deleteCard(card: Card): void {
    if (!confirm(`Delete the card "${card.front}"? Its answers are deleted too.`)) {
      return;
    }
    this.error.set(null);
    this.flashcardApi.deleteCard(card.deckId, card.id).subscribe({
      next: () => this.load(card.deckId),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }

  protected deleteDeck(deck: Deck): void {
    if (
      !confirm(
        `Delete the deck "${deck.title}"? Its cards and every answer to them are deleted too.`,
      )
    ) {
      return;
    }
    this.blockingNodes.set([]);
    this.flashcardApi.deleteDeck(deck.id).subscribe({
      next: () => this.router.navigateByUrl('/decks'),
      error: (error) => {
        this.error.set(errorMessage(error));
        this.blockingNodes.set(problemOf(error)?.nodes ?? []);
      },
    });
  }

  private load(id: number): void {
    this.error.set(null);
    forkJoin({ deck: this.flashcardApi.deck(id), cards: this.flashcardApi.cards(id) }).subscribe({
      next: ({ deck, cards }) => {
        this.deck.set(deck);
        this.cards.set(cards);
      },
      error: (error) => this.error.set(errorMessage(error)),
    });
  }
}
