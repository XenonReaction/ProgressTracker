import { Component, OnInit, inject, input, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { DeckRequest } from '../core/api.models';
import { FlashcardApi } from '../core/flashcard-api';
import { errorMessage } from '../core/problem';

/** Create (`/decks/new`) or edit (`/decks/:id/edit`) a deck's details. Cards are edited on the deck's page. */
@Component({
  selector: 'app-deck-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './deck-form.html',
})
export class DeckForm implements OnInit {
  private readonly flashcardApi = inject(FlashcardApi);
  private readonly router = inject(Router);

  /** Route param; absent when creating. */
  readonly id = input<string>();

  protected readonly form = new FormGroup({
    title: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(200)],
    }),
    description: new FormControl('', { nonNullable: true }),
  });

  protected readonly error = signal<string | null>(null);
  protected readonly saving = signal(false);

  ngOnInit(): void {
    const id = this.id();
    if (id) {
      this.flashcardApi.deck(Number(id)).subscribe({
        next: (deck) =>
          this.form.setValue({ title: deck.title, description: deck.description ?? '' }),
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
    const request: DeckRequest = {
      title: value.title.trim(),
      description: value.description.trim() || null,
    };
    const id = this.id();
    const save$ = id
      ? this.flashcardApi.updateDeck(Number(id), request)
      : this.flashcardApi.createDeck(request);
    this.saving.set(true);
    this.error.set(null);
    save$.subscribe({
      next: (deck) => this.router.navigateByUrl(`/decks/${deck.id}`),
      error: (error) => {
        this.saving.set(false);
        this.error.set(errorMessage(error));
      },
    });
  }

  protected cancelUrl(): string {
    const id = this.id();
    return id ? `/decks/${id}` : '/decks';
  }
}
