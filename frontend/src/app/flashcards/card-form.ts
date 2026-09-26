import { Component, OnInit, inject, input, output, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { Card, CardRequest } from '../core/api.models';
import { FlashcardApi } from '../core/flashcard-api';
import { errorMessage } from '../core/problem';

const MAX_SIDE_LENGTH = 5000;

/**
 * Adds a card to a deck, or edits `card` when it's given. After adding, the form clears for
 * the next card.
 */
@Component({
  selector: 'app-card-form',
  imports: [ReactiveFormsModule],
  template: `
    <form [formGroup]="form" (ngSubmit)="save()" class="card-form">
      <p>
        <label>
          Front<br />
          <textarea formControlName="front" rows="2" cols="40"></textarea>
        </label>
        &nbsp;
        <label>
          Back<br />
          <textarea formControlName="back" rows="2" cols="40"></textarea>
        </label>
      </p>
      @if (form.touched && form.invalid) {
        <p class="error">Both sides are required (max {{ maxLength }} characters each).</p>
      }
      @if (error(); as message) {
        <p class="error" role="alert">{{ message }}</p>
      }
      <p>
        <button type="submit" [disabled]="saving()">{{ card() ? 'Save card' : 'Add card' }}</button>
        @if (card()) {
          &nbsp;
          <button type="button" (click)="cancelled.emit()">Cancel</button>
        }
      </p>
    </form>
  `,
})
export class CardForm implements OnInit {
  private readonly flashcardApi = inject(FlashcardApi);

  readonly deckId = input.required<number>();
  /** The card to edit; null to add a new one. */
  readonly card = input<Card | null>(null);

  readonly saved = output<Card>();
  readonly cancelled = output<void>();

  protected readonly maxLength = MAX_SIDE_LENGTH;

  protected readonly form = new FormGroup({
    front: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(MAX_SIDE_LENGTH)],
    }),
    back: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(MAX_SIDE_LENGTH)],
    }),
  });

  protected readonly error = signal<string | null>(null);
  protected readonly saving = signal(false);

  ngOnInit(): void {
    const card = this.card();
    if (card) {
      this.form.setValue({ front: card.front, back: card.back });
    }
  }

  protected save(): void {
    const value = this.form.getRawValue();
    const request: CardRequest = { front: value.front.trim(), back: value.back.trim() };
    if (this.form.invalid || !request.front || !request.back) {
      this.form.markAllAsTouched();
      return;
    }
    const card = this.card();
    const save$ = card
      ? this.flashcardApi.updateCard(this.deckId(), card.id, request)
      : this.flashcardApi.createCard(this.deckId(), request);
    this.saving.set(true);
    this.error.set(null);
    save$.subscribe({
      next: (saved) => {
        this.saving.set(false);
        if (!card) {
          this.form.reset();
        }
        this.saved.emit(saved);
      },
      error: (error) => {
        this.saving.set(false);
        this.error.set(errorMessage(error));
      },
    });
  }
}
