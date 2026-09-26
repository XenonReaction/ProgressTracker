import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Card, CardRequest, Deck, DeckRequest } from './api.models';

@Injectable({ providedIn: 'root' })
export class FlashcardApi {
  private readonly http = inject(HttpClient);
  private readonly decksUrl = '/api/v1/decks';

  decks(): Observable<Deck[]> {
    return this.http.get<Deck[]>(this.decksUrl);
  }

  deck(id: number): Observable<Deck> {
    return this.http.get<Deck>(`${this.decksUrl}/${id}`);
  }

  createDeck(request: DeckRequest): Observable<Deck> {
    return this.http.post<Deck>(this.decksUrl, request);
  }

  updateDeck(id: number, request: DeckRequest): Observable<Deck> {
    return this.http.put<Deck>(`${this.decksUrl}/${id}`, request);
  }

  /** Deletes the deck with its cards and their answers. */
  deleteDeck(id: number): Observable<void> {
    return this.http.delete<void>(`${this.decksUrl}/${id}`);
  }

  cards(deckId: number): Observable<Card[]> {
    return this.http.get<Card[]>(`${this.decksUrl}/${deckId}/cards`);
  }

  createCard(deckId: number, request: CardRequest): Observable<Card> {
    return this.http.post<Card>(`${this.decksUrl}/${deckId}/cards`, request);
  }

  /** Replaces the card's text; its answers are kept. */
  updateCard(deckId: number, cardId: number, request: CardRequest): Observable<Card> {
    return this.http.put<Card>(`${this.decksUrl}/${deckId}/cards/${cardId}`, request);
  }

  deleteCard(deckId: number, cardId: number): Observable<void> {
    return this.http.delete<void>(`${this.decksUrl}/${deckId}/cards/${cardId}`);
  }

  /** Records one answer; returns the card with its new standing. */
  review(card: Card, correct: boolean): Observable<Card> {
    return this.http.post<Card>(`${this.decksUrl}/${card.deckId}/cards/${card.id}/reviews`, {
      correct,
    });
  }

  /**
   * Cards not yet passed, in the order to study them (never answered first, then the least
   * recently answered): from every deck, or from one.
   */
  reviewQueue(deckId?: number): Observable<Card[]> {
    const params: Record<string, number> = deckId === undefined ? {} : { deckId };
    return this.http.get<Card[]>('/api/v1/review-queue', { params });
  }
}
