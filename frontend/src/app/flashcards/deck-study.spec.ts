import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aCard, aDeck } from '../core/test-data';
import { DeckStudy } from './deck-study';

describe('DeckStudy', () => {
  let fixture: ComponentFixture<DeckStudy>;
  let http: HttpTestingController;
  let page: HTMLElement;

  beforeEach(async () => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(DeckStudy);
    page = fixture.nativeElement;
    fixture.componentRef.setInput('id', '4');
    await fixture.whenStable();
  });

  afterEach(() => http.verify());

  it("studies the deck's cards not yet passed", async () => {
    http.expectOne('/api/v1/decks/4').flush(aDeck({ id: 4, title: 'CSS Flexbox', cardCount: 3 }));
    http
      .expectOne('/api/v1/review-queue?deckId=4')
      .flush([aCard({ id: 8, deckId: 4, front: 'Main axis?' })]);
    await fixture.whenStable();

    expect(page.querySelector('h1')?.textContent).toBe('Study: CSS Flexbox');
    expect(page.textContent).toContain('Card 1 of 1');
  });

  it('offers to study every card once all have passed', async () => {
    http.expectOne('/api/v1/decks/4').flush(aDeck({ id: 4, cardCount: 2, passedCount: 2 }));
    http.expectOne('/api/v1/review-queue?deckId=4').flush([]);
    await fixture.whenStable();
    expect(page.textContent).toContain('Every card in this deck has passed.');

    (page.querySelector('button') as HTMLButtonElement).click();
    http
      .expectOne('/api/v1/decks/4/cards')
      .flush([
        aCard({ id: 7, deckId: 4, passed: true }),
        aCard({ id: 8, deckId: 4, passed: true }),
      ]);
    await fixture.whenStable();

    expect(page.textContent).toContain('Card 1 of 2');
  });

  it('says when the deck has no cards', async () => {
    http.expectOne('/api/v1/decks/4').flush(aDeck({ id: 4, cardCount: 0 }));
    http.expectOne('/api/v1/review-queue?deckId=4').flush([]);
    await fixture.whenStable();

    expect(page.textContent).toContain('This deck has no cards yet.');
  });
});
