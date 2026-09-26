import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { FlashcardApi } from './flashcard-api';
import { aCard } from './test-data';

describe('FlashcardApi', () => {
  let api: FlashcardApi;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    api = TestBed.inject(FlashcardApi);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('calls the deck endpoints', () => {
    api.decks().subscribe();
    api.deck(4).subscribe();
    api.createDeck({ title: 'CSS', description: null }).subscribe();
    api.updateDeck(4, { title: 'HTML', description: 'Markup' }).subscribe();
    api.deleteDeck(4).subscribe();

    http.expectOne({ method: 'GET', url: '/api/v1/decks' }).flush([]);
    http.expectOne({ method: 'GET', url: '/api/v1/decks/4' }).flush({});
    expect(http.expectOne({ method: 'POST', url: '/api/v1/decks' }).request.body).toEqual({
      title: 'CSS',
      description: null,
    });
    expect(http.expectOne({ method: 'PUT', url: '/api/v1/decks/4' }).request.body.title).toBe(
      'HTML',
    );
    http.expectOne({ method: 'DELETE', url: '/api/v1/decks/4' }).flush(null);
  });

  it('calls the card, review and review-queue endpoints', () => {
    api.cards(4).subscribe();
    api.createCard(4, { front: 'Q', back: 'A' }).subscribe();
    api.updateCard(4, 7, { front: 'Q2', back: 'A2' }).subscribe();
    api.deleteCard(4, 7).subscribe();
    api.review(aCard({ id: 7, deckId: 4 }), true).subscribe();
    api.reviewQueue().subscribe();
    api.reviewQueue(4).subscribe();

    http.expectOne({ method: 'GET', url: '/api/v1/decks/4/cards' }).flush([]);
    expect(http.expectOne({ method: 'POST', url: '/api/v1/decks/4/cards' }).request.body).toEqual({
      front: 'Q',
      back: 'A',
    });
    expect(
      http.expectOne({ method: 'PUT', url: '/api/v1/decks/4/cards/7' }).request.body.back,
    ).toBe('A2');
    http.expectOne({ method: 'DELETE', url: '/api/v1/decks/4/cards/7' }).flush(null);
    expect(
      http.expectOne({ method: 'POST', url: '/api/v1/decks/4/cards/7/reviews' }).request.body,
    ).toEqual({ correct: true });
    http.expectOne({ method: 'GET', url: '/api/v1/review-queue' }).flush([]);
    http.expectOne({ method: 'GET', url: '/api/v1/review-queue?deckId=4' }).flush([]);
  });
});
