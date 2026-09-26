import { DatePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Deck } from '../core/api.models';
import { FlashcardApi } from '../core/flashcard-api';
import { errorMessage } from '../core/problem';

/** The user's flashcard decks (`/decks`), each with how many of its cards have passed. */
@Component({
  selector: 'app-deck-list',
  imports: [RouterLink, DatePipe],
  templateUrl: './deck-list.html',
})
export class DeckList {
  private readonly flashcardApi = inject(FlashcardApi);

  /** null until the first load finishes. */
  protected readonly decks = signal<Deck[] | null>(null);
  protected readonly error = signal<string | null>(null);

  constructor() {
    this.load();
  }

  protected delete(deck: Deck): void {
    if (
      !confirm(
        `Delete the deck "${deck.title}"? Its cards and every answer to them are deleted too.`,
      )
    ) {
      return;
    }
    this.error.set(null);
    this.flashcardApi.deleteDeck(deck.id).subscribe({
      next: () => this.load(),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }

  private load(): void {
    this.flashcardApi.decks().subscribe({
      next: (decks) => this.decks.set(decks),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }
}
