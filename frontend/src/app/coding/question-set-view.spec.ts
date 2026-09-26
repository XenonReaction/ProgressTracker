import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aCodingQuestion, aQuestionSet } from '../core/test-data';
import { QuestionSetView } from './question-set-view';

describe('QuestionSetView', () => {
  let fixture: ComponentFixture<QuestionSetView>;
  let http: HttpTestingController;
  let page: HTMLElement;

  beforeEach(async () => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(QuestionSetView);
    page = fixture.nativeElement;
    fixture.componentRef.setInput('id', '4');
    await fixture.whenStable();
    http
      .expectOne('/api/v1/question-sets/4')
      .flush(
        aQuestionSet({ id: 4, title: 'Flexbox', questionCount: 2, solvedCount: 1, readiness: 50 }),
      );
    http
      .expectOne('/api/v1/question-sets/4/questions')
      .flush([
        aCodingQuestion({ id: 7, setId: 4, title: 'Centre a box', solved: true }),
        aCodingQuestion({
          id: 8,
          setId: 4,
          title: 'Nav bar',
          language: 'html',
          solutionRevealed: true,
        }),
      ]);
    await fixture.whenStable();
  });

  afterEach(() => {
    http.verify();
    vi.restoreAllMocks();
  });

  it("shows the set's readiness and each question with its language and status", () => {
    expect(page.textContent).toContain('50%: 1 of 2 questions solved.');
    const rows = Array.from(page.querySelectorAll('tbody tr')).map((row) => row.textContent);
    expect(rows[0]).toContain('CSS');
    expect(rows[0]).toContain('Solved');
    expect(rows[1]).toContain('HTML');
    expect(rows[1]).toContain('Solution seen, not solved yet');
    expect(page.querySelector('a[href="/question-sets/4/questions/new"]')).not.toBeNull();
    expect(page.querySelector('a[href="/question-sets/4/questions/8"]')).not.toBeNull();
  });

  it('deletes a question after confirming, then reloads', async () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);

    (Array.from(page.querySelectorAll('tbody button'))[1] as HTMLButtonElement).click();

    http.expectOne({ method: 'DELETE', url: '/api/v1/question-sets/4/questions/8' }).flush(null);
    http.expectOne('/api/v1/question-sets/4').flush(aQuestionSet({ id: 4 }));
    http
      .expectOne('/api/v1/question-sets/4/questions')
      .flush([aCodingQuestion({ id: 7, setId: 4 })]);
    await fixture.whenStable();
    expect(page.querySelectorAll('tbody tr').length).toBe(1);
  });
});
