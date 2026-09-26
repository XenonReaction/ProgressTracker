import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aDeck } from '../core/test-data';
import { DeckList } from './deck-list';

describe('DeckList', () => {
  let fixture: ComponentFixture<DeckList>;
  let http: HttpTestingController;
  let page: HTMLElement;

  beforeEach(async () => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(DeckList);
    page = fixture.nativeElement;
    http
      .expectOne('/api/v1/decks')
      .flush([
        aDeck({
          id: 3,
          title: 'CSS Flexbox',
          cardCount: 5,
          passedCount: 4,
          readiness: 80,
          complete: true,
          lastReviewedAt: '2026-09-01T10:00:00Z',
        }),
        aDeck({ id: 5, title: 'HTML Forms', cardCount: 2 }),
      ]);
    await fixture.whenStable();
  });

  afterEach(() => {
    http.verify();
    vi.restoreAllMocks();
  });

  it('shows each deck with its cards passed, readiness and whether it is complete', () => {
    const [flexbox, forms] = Array.from(page.querySelectorAll('tbody tr'));
    expect(flexbox.textContent).toContain('4 of 5');
    expect(flexbox.textContent).toContain('80%');
    expect(flexbox.textContent).toContain('Complete');
    expect(forms.textContent).not.toContain('Complete');
    expect(forms.textContent).toContain('Never');
    expect(Array.from(flexbox.querySelectorAll('a')).map((a) => a.getAttribute('href'))).toEqual([
      '/decks/3',
      '/decks/3/study',
      '/decks/3/edit',
    ]);
  });

  it('deletes a deck after a confirmation that says its cards go too, then reloads', async () => {
    const confirm = vi.spyOn(window, 'confirm').mockReturnValue(true);

    (page.querySelector('tbody button') as HTMLButtonElement).click();

    expect(confirm.mock.calls[0][0]).toContain(
      'Its cards and every answer to them are deleted too',
    );
    http
      .expectOne({ method: 'DELETE', url: '/api/v1/decks/3' })
      .flush(null, { status: 204, statusText: 'No Content' });
    http.expectOne('/api/v1/decks').flush([aDeck({ id: 5 })]);
    await fixture.whenStable();
    expect(page.querySelectorAll('tbody tr').length).toBe(1);
  });
});
