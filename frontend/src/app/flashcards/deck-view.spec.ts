import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';

import { Card, Deck } from '../core/api.models';
import { aCard, aDeck } from '../core/test-data';
import { DeckView } from './deck-view';

describe('DeckView', () => {
  let fixture: ComponentFixture<DeckView>;
  let http: HttpTestingController;
  let page: HTMLElement;

  const deck = aDeck({ id: 4, title: 'CSS Flexbox', cardCount: 2, passedCount: 1, readiness: 50 });
  const cards = [
    aCard({
      id: 7,
      deckId: 4,
      front: 'Flex container?',
      back: 'display: flex',
      passed: true,
      correctInARow: 3,
      lastReviewedAt: '2026-09-01T00:00:00Z',
    }),
    aCard({ id: 8, deckId: 4, front: 'Main axis?', back: 'flex-direction' }),
  ];

  beforeEach(async () => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(DeckView);
    page = fixture.nativeElement;
    fixture.componentRef.setInput('id', '4');
    await fixture.whenStable();
    await flush(deck, cards);
  });

  afterEach(() => {
    http.verify();
    vi.restoreAllMocks();
  });

  it("shows the deck's readiness and each card with where it stands", () => {
    expect(page.querySelector('h1')?.textContent).toBe('CSS Flexbox');
    expect(page.textContent).toContain('50%: 1 of 2 cards passed.');
    expect(page.textContent).toContain('Complete once 80% of its cards have passed');
    const rows = Array.from(page.querySelectorAll('tbody tr')).map((row) => row.textContent);
    expect(rows[0]).toContain('Passed');
    expect(rows[1]).toContain('Not answered yet');
    expect(page.querySelector('a[href="/decks/4/study"]')).not.toBeNull();
  });

  it('says when the deck is complete', async () => {
    fixture.componentRef.setInput('id', '5');
    await fixture.whenStable();
    await flush(aDeck({ id: 5, cardCount: 5, passedCount: 4, readiness: 80, complete: true }), []);

    expect(page.textContent).toContain('Complete: at least 80% of its cards have passed.');
  });

  it('adds a card and reloads the deck, whose counts change', async () => {
    const [front, back] = Array.from(
      page.querySelectorAll<HTMLTextAreaElement>('form textarea'),
    ).slice(-2);
    type(front, ' Cross axis? ');
    type(back, 'align-items');
    forms().at(-1)!.dispatchEvent(new Event('submit'));

    const request = http.expectOne({ method: 'POST', url: '/api/v1/decks/4/cards' });
    expect(request.request.body).toEqual({ front: 'Cross axis?', back: 'align-items' });
    request.flush(aCard({ id: 9, deckId: 4 }));
    await flush({ ...deck, cardCount: 3 }, [
      ...cards,
      aCard({ id: 9, deckId: 4, front: 'Cross axis?' }),
    ]);

    expect(page.querySelectorAll('tbody tr').length).toBe(3);
    expect(front.value).toBe('');
  });

  it('does not add a card with a blank side', async () => {
    const [front] = Array.from(page.querySelectorAll<HTMLTextAreaElement>('form textarea')).slice(
      -2,
    );
    type(front, 'Only a front');
    forms().at(-1)!.dispatchEvent(new Event('submit'));
    await fixture.whenStable();

    http.expectNone({ method: 'POST' });
    expect(page.textContent).toContain('Both sides are required');
  });

  it('edits a card in place', async () => {
    buttons('Edit')[1].click();
    await fixture.whenStable();
    const [front] = Array.from(page.querySelectorAll<HTMLTextAreaElement>('tbody textarea'));
    expect(front.value).toBe('Main axis?');

    type(front, 'Which property sets the main axis?');
    page.querySelector('tbody form')!.dispatchEvent(new Event('submit'));

    const request = http.expectOne({ method: 'PUT', url: '/api/v1/decks/4/cards/8' });
    expect(request.request.body).toEqual({
      front: 'Which property sets the main axis?',
      back: 'flex-direction',
    });
    request.flush(cards[1]);
    await flush(deck, cards);
    expect(page.querySelector('tbody textarea')).toBeNull();
  });

  it('deletes a card after confirming', async () => {
    const confirm = vi.spyOn(window, 'confirm').mockReturnValue(true);

    buttons('Delete')[0].click();

    expect(confirm.mock.calls[0][0]).toContain('Delete the card "Flex container?"');
    http.expectOne({ method: 'DELETE', url: '/api/v1/decks/4/cards/7' }).flush(null);
    await flush({ ...deck, cardCount: 1 }, [cards[1]]);
    expect(page.querySelectorAll('tbody tr').length).toBe(1);
  });

  it('deletes the deck and goes back to the list', () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);
    const navigate = vi.spyOn(TestBed.inject(Router), 'navigateByUrl').mockResolvedValue(true);

    buttons('Delete deck')[0].click();

    http.expectOne({ method: 'DELETE', url: '/api/v1/decks/4' }).flush(null);
    expect(navigate).toHaveBeenCalledWith('/decks');
  });

  async function flush(withDeck: Deck, withCards: Card[]): Promise<void> {
    http.expectOne(`/api/v1/decks/${withDeck.id}`).flush(withDeck);
    http.expectOne(`/api/v1/decks/${withDeck.id}/cards`).flush(withCards);
    await fixture.whenStable();
  }

  function forms(): HTMLFormElement[] {
    return Array.from(page.querySelectorAll('form'));
  }

  function buttons(label: string): HTMLButtonElement[] {
    return Array.from(page.querySelectorAll<HTMLButtonElement>('button')).filter(
      (b) => b.textContent?.trim() === label,
    );
  }

  function type(field: HTMLTextAreaElement, value: string): void {
    field.value = value;
    field.dispatchEvent(new Event('input'));
  }
});
