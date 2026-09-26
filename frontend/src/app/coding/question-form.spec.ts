import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';

import { aCodingQuestion } from '../core/test-data';
import { QuestionForm } from './question-form';

describe('QuestionForm', () => {
  let fixture: ComponentFixture<QuestionForm>;
  let http: HttpTestingController;
  let navigate: ReturnType<typeof vi.spyOn>;
  let page: HTMLElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    navigate = vi.spyOn(TestBed.inject(Router), 'navigateByUrl').mockResolvedValue(true);
    fixture = TestBed.createComponent(QuestionForm);
    page = fixture.nativeElement;
    fixture.componentRef.setInput('id', '4');
  });

  afterEach(() => http.verify());

  it('creates a question in the set', async () => {
    await fixture.whenStable();
    type('input[formControlName=title]', 'Centre a box');
    type('textarea[formControlName=problem]', 'Centre `.box`.');
    type('textarea[formControlName=solution]', '.frame { display: flex; }');

    submit();

    const request = http.expectOne({ method: 'POST', url: '/api/v1/question-sets/4/questions' });
    expect(request.request.body).toEqual({
      title: 'Centre a box',
      language: 'css',
      problem: 'Centre `.box`.',
      examples: null,
      solution: '.frame { display: flex; }',
    });
    request.flush(aCodingQuestion());
    expect(navigate).toHaveBeenCalledWith('/question-sets/4');
  });

  it('needs a title, a problem and a solution', async () => {
    await fixture.whenStable();

    submit();
    await fixture.whenStable();

    http.expectNone({ method: 'POST' });
    expect(page.textContent).toContain('Describe the problem.');
    expect(page.textContent).toContain('Write the solution.');
  });

  it('loads the question with its solution for editing, without revealing it', async () => {
    fixture.componentRef.setInput('questionId', '7');
    await fixture.whenStable();

    http
      .expectOne('/api/v1/question-sets/4/questions/7?includeSolution=true')
      .flush(aCodingQuestion({ id: 7, language: 'html', solution: '<div></div>' }));
    await fixture.whenStable();

    expect(
      (page.querySelector('textarea[formControlName=solution]') as HTMLTextAreaElement).value,
    ).toBe('<div></div>');
    expect((page.querySelector('select') as HTMLSelectElement).selectedOptions[0].textContent).toBe(
      'HTML',
    );
  });

  function type(selector: string, value: string): void {
    const field = page.querySelector(selector) as HTMLInputElement | HTMLTextAreaElement;
    field.value = value;
    field.dispatchEvent(new Event('input'));
  }

  function submit(): void {
    page.querySelector('form')!.dispatchEvent(new Event('submit'));
  }
});
