import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aQuestionSet } from '../core/test-data';
import { QuestionSetList } from './question-set-list';

describe('QuestionSetList', () => {
  let fixture: ComponentFixture<QuestionSetList>;
  let http: HttpTestingController;
  let page: HTMLElement;

  beforeEach(async () => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(QuestionSetList);
    page = fixture.nativeElement;
    http
      .expectOne('/api/v1/question-sets')
      .flush([
        aQuestionSet({
          id: 3,
          title: 'Flexbox',
          questionCount: 4,
          solvedCount: 3,
          readiness: 75,
          lastReviewedAt: '2026-09-22T10:00:00Z',
        }),
        aQuestionSet({ id: 5, title: 'Grid' }),
      ]);
    await fixture.whenStable();
  });

  afterEach(() => {
    http.verify();
    vi.restoreAllMocks();
  });

  it('shows each set with how many questions are solved', () => {
    const [flexbox, grid] = Array.from(page.querySelectorAll('tbody tr'));
    expect(flexbox.textContent).toContain('3 of 4');
    expect(flexbox.textContent).toContain('75%');
    expect(flexbox.textContent).toContain('Sep 22, 2026');
    expect(grid.textContent).toContain('Never');
  });

  it('explains a refused delete and links to the nodes that list the set', async () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);

    (page.querySelector('tbody button') as HTMLButtonElement).click();
    http
      .expectOne({ method: 'DELETE', url: '/api/v1/question-sets/3' })
      .flush(
        {
          status: 409,
          title: 'Conflict',
          detail: 'Question set 3 is a resource of 1 node(s)',
          nodes: [{ id: 8, title: 'CSS Flexbox' }],
        },
        { status: 409, statusText: 'Conflict' },
      );
    await fixture.whenStable();

    expect(page.querySelector('[role=alert] a')?.getAttribute('href')).toBe('/nodes/8');
  });
});
