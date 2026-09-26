import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aCard } from '../core/test-data';
import { ReviewPage } from './review-page';

describe('ReviewPage', () => {
  let fixture: ComponentFixture<ReviewPage>;
  let http: HttpTestingController;
  let page: HTMLElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(ReviewPage);
    page = fixture.nativeElement;
  });

  afterEach(() => http.verify());

  it("studies every card not yet passed, showing each card's deck", async () => {
    http
      .expectOne('/api/v1/review-queue')
      .flush([
        aCard({ id: 7, deckTitle: 'HTML', front: 'Line break?' }),
        aCard({ id: 8, deckTitle: 'CSS', front: 'Main axis?' }),
      ]);
    await fixture.whenStable();

    expect(page.textContent).toContain('Card 1 of 2 · HTML');
    expect(page.querySelector('h2')?.textContent).toBe('Line break?');
  });

  it('says when there is nothing to review', async () => {
    http.expectOne('/api/v1/review-queue').flush([]);
    await fixture.whenStable();

    expect(page.textContent).toContain('Nothing to review: every card has passed.');
  });

  it('loads a fresh list to study again', async () => {
    http.expectOne('/api/v1/review-queue').flush([aCard({ id: 7, deckId: 4 })]);
    await fixture.whenStable();
    (
      Array.from(page.querySelectorAll('button')).find(
        (b) => b.textContent?.trim() === 'Show answer',
      ) as HTMLButtonElement
    ).click();
    await fixture.whenStable();
    (
      Array.from(page.querySelectorAll('button')).find(
        (b) => b.textContent?.trim() === 'Correct',
      ) as HTMLButtonElement
    ).click();
    http
      .expectOne({ method: 'POST', url: '/api/v1/decks/4/cards/7/reviews' })
      .flush(aCard({ id: 7, deckId: 4 }));
    await fixture.whenStable();

    (
      Array.from(page.querySelectorAll('button')).find(
        (b) => b.textContent?.trim() === 'Study again',
      ) as HTMLButtonElement
    ).click();

    http.expectOne('/api/v1/review-queue').flush([]);
    await fixture.whenStable();
    expect(page.textContent).toContain('Nothing to review');
  });
});
