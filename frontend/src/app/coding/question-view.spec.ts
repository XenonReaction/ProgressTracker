import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aCodingQuestion } from '../core/test-data';
import { QuestionView } from './question-view';

describe('QuestionView', () => {
  let fixture: ComponentFixture<QuestionView>;
  let http: HttpTestingController;
  let page: HTMLElement;

  const question = aCodingQuestion({
    id: 7,
    setId: 4,
    setTitle: 'Flexbox',
    title: 'Centre a box',
    problem: 'Centre `.box`.',
    examples: '```html\n<div class="frame"></div>\n```',
  });

  beforeEach(async () => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(QuestionView);
    page = fixture.nativeElement;
    fixture.componentRef.setInput('id', '4');
    fixture.componentRef.setInput('questionId', '7');
    await fixture.whenStable();
    http.expectOne('/api/v1/question-sets/4/questions/7').flush(question);
    await fixture.whenStable();
  });

  afterEach(() => {
    http.verify();
    vi.restoreAllMocks();
  });

  it('shows the problem and examples as Markdown, with the solution hidden', () => {
    expect(page.querySelector('h1')?.textContent).toBe('Centre a box');
    expect(page.textContent).toContain('CSS · Not solved yet');
    const [problem, examples] = Array.from(page.querySelectorAll('.markdown'));
    expect(problem.innerHTML).toContain('<code>.box</code>');
    expect(examples.querySelector('pre code')?.textContent).toContain('<div class="frame"></div>');
    expect(page.querySelector('.solution')).toBeNull();
  });

  it('shows the solution only after confirming that seeing it is recorded', async () => {
    const confirm = vi
      .spyOn(window, 'confirm')
      .mockReturnValueOnce(false)
      .mockReturnValueOnce(true);

    button('Show solution').click();
    http.expectNone({ method: 'POST' });
    expect(confirm.mock.calls[0][0]).toContain('is recorded');

    button('Show solution').click();
    http
      .expectOne({ method: 'POST', url: '/api/v1/question-sets/4/questions/7/reveal' })
      .flush({ ...question, solution: '.frame { display: flex; }', solutionRevealed: true });
    await fixture.whenStable();

    expect(page.querySelector('.solution')?.textContent).toBe('.frame { display: flex; }');
    expect(page.textContent).toContain('Solution seen, not solved yet');
  });

  it('marks the question solved', async () => {
    button('Mark solved').click();

    http
      .expectOne({ method: 'POST', url: '/api/v1/question-sets/4/questions/7/solved' })
      .flush({ ...question, solved: true });
    await fixture.whenStable();

    expect(button('Mark solved')).toBeUndefined();
    expect(page.textContent).toContain('CSS · Solved');
  });

  function button(label: string): HTMLButtonElement {
    return Array.from(page.querySelectorAll('button')).find(
      (b) => b.textContent?.trim() === label,
    ) as HTMLButtonElement;
  }
});
