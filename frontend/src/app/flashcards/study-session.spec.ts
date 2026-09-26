import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { aCard } from '../core/test-data';
import { StudySession } from './study-session';

describe('StudySession', () => {
  let fixture: ComponentFixture<StudySession>;
  let http: HttpTestingController;
  let page: HTMLElement;

  const first = aCard({
    id: 7,
    deckId: 4,
    deckTitle: 'CSS',
    front: 'Flex container?',
    back: 'display: flex',
    correctInARow: 2,
    lastReviewedAt: '2026-09-01T00:00:00Z',
  });
  const second = aCard({ id: 8, deckId: 5, deckTitle: 'HTML', front: 'Line break?', back: '<br>' });

  beforeEach(async () => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(StudySession);
    page = fixture.nativeElement;
    fixture.componentRef.setInput('cards', [first, second]);
    await fixture.whenStable();
  });

  afterEach(() => http.verify());

  it('shows the front, then the back once asked', async () => {
    expect(page.textContent).toContain('Card 1 of 2');
    expect(page.textContent).toContain('2 of 3 correct in a row');
    expect(page.querySelector('h2')?.textContent).toBe('Flex container?');
    expect(page.textContent).not.toContain('display: flex');
    expect(button('Correct')).toBeUndefined();

    button('Show answer')!.click();
    await fixture.whenStable();

    expect(page.querySelector('.answer')?.textContent).toBe('display: flex');
  });

  it('records each verdict, moves on, and ends with a summary', async () => {
    await answer('Correct');
    const firstReview = http.expectOne({ method: 'POST', url: '/api/v1/decks/4/cards/7/reviews' });
    expect(firstReview.request.body).toEqual({ correct: true });
    firstReview.flush({ ...first, passed: true, correctInARow: 3 });
    await fixture.whenStable();
    expect(page.querySelector('h2')?.textContent).toBe('Line break?');

    await answer('Wrong');
    const secondReview = http.expectOne({ method: 'POST', url: '/api/v1/decks/5/cards/8/reviews' });
    expect(secondReview.request.body).toEqual({ correct: false });
    secondReview.flush({ ...second, lastReviewedAt: '2026-09-02T00:00:00Z' });
    await fixture.whenStable();

    expect(page.textContent).toContain('You got 1 of 2 right.');
    expect(page.textContent).toContain('1 card has now passed.');
    let again = 0;
    fixture.componentInstance.again.subscribe(() => again++);
    button('Study again')!.click();
    expect(again).toBe(1);
  });

  it('does not count a due card that was already passed as newly passed', async () => {
    const due = aCard({
      id: 9,
      deckId: 4,
      front: 'Gap?',
      back: 'gap',
      passed: true,
      correctInARow: 3,
      due: true,
    });
    fixture.componentRef.setInput('cards', [due]);
    await fixture.whenStable();
    expect(page.textContent).toContain('Passed, review due');

    await answer('Correct');
    http
      .expectOne({ method: 'POST', url: '/api/v1/decks/4/cards/9/reviews' })
      .flush({ ...due, due: false });
    await fixture.whenStable();

    expect(page.textContent).toContain('You got 1 of 1 right.');
    expect(page.textContent).not.toContain('now passed');
  });

  it("shows each card's deck when asked", async () => {
    fixture.componentRef.setInput('showDeck', true);
    await fixture.whenStable();

    expect(page.textContent).toContain('Card 1 of 2 · CSS');
  });

  it('stays on the card and shows the error if saving fails', async () => {
    await answer('Correct');
    http
      .expectOne({ method: 'POST', url: '/api/v1/decks/4/cards/7/reviews' })
      .flush(
        { status: 404, title: 'Not Found', detail: 'Card 7 not found' },
        { status: 404, statusText: 'Not Found' },
      );
    await fixture.whenStable();

    expect(page.querySelector('[role=alert]')?.textContent).toContain('Card 7 not found');
    expect(page.querySelector('h2')?.textContent).toBe('Flex container?');
  });

  it('starts again from the first card when given a new list', async () => {
    await answer('Correct');
    http.expectOne({ method: 'POST', url: '/api/v1/decks/4/cards/7/reviews' }).flush(first);
    await fixture.whenStable();

    fixture.componentRef.setInput('cards', [second]);
    await fixture.whenStable();

    expect(page.textContent).toContain('Card 1 of 1');
    expect(page.querySelector('h2')?.textContent).toBe('Line break?');
  });

  async function answer(verdict: 'Correct' | 'Wrong'): Promise<void> {
    button('Show answer')!.click();
    await fixture.whenStable();
    button(verdict)!.click();
  }

  function button(label: string): HTMLButtonElement | undefined {
    return Array.from(page.querySelectorAll('button')).find((b) => b.textContent?.trim() === label);
  }
});
