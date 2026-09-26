import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { CodingApi } from './coding-api';

describe('CodingApi', () => {
  let api: CodingApi;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    api = TestBed.inject(CodingApi);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('calls the question set endpoints', () => {
    api.sets().subscribe();
    api.set(4).subscribe();
    api.createSet({ title: 'Flexbox', description: null }).subscribe();
    api.updateSet(4, { title: 'Grid', description: null }).subscribe();
    api.deleteSet(4).subscribe();

    http.expectOne({ method: 'GET', url: '/api/v1/question-sets' }).flush([]);
    http.expectOne({ method: 'GET', url: '/api/v1/question-sets/4' }).flush({});
    http.expectOne({ method: 'POST', url: '/api/v1/question-sets' }).flush({});
    expect(
      http.expectOne({ method: 'PUT', url: '/api/v1/question-sets/4' }).request.body.title,
    ).toBe('Grid');
    http.expectOne({ method: 'DELETE', url: '/api/v1/question-sets/4' }).flush(null);
  });

  it('calls the question endpoints, asking for the solution only when editing', () => {
    const request = {
      title: 'Q',
      language: 'css' as const,
      problem: 'P',
      examples: null,
      solution: 'S',
    };
    api.questions(4).subscribe();
    api.question(4, 7).subscribe();
    api.question(4, 7, true).subscribe();
    api.createQuestion(4, request).subscribe();
    api.updateQuestion(4, 7, request).subscribe();
    api.deleteQuestion(4, 7).subscribe();
    api.reveal(4, 7).subscribe();
    api.markSolved(4, 7).subscribe();

    http.expectOne({ method: 'GET', url: '/api/v1/question-sets/4/questions' }).flush([]);
    http.expectOne({ method: 'GET', url: '/api/v1/question-sets/4/questions/7' }).flush({});
    http
      .expectOne({ method: 'GET', url: '/api/v1/question-sets/4/questions/7?includeSolution=true' })
      .flush({});
    expect(
      http.expectOne({ method: 'POST', url: '/api/v1/question-sets/4/questions' }).request.body,
    ).toEqual(request);
    http.expectOne({ method: 'PUT', url: '/api/v1/question-sets/4/questions/7' }).flush({});
    http.expectOne({ method: 'DELETE', url: '/api/v1/question-sets/4/questions/7' }).flush(null);
    http.expectOne({ method: 'POST', url: '/api/v1/question-sets/4/questions/7/reveal' }).flush({});
    http.expectOne({ method: 'POST', url: '/api/v1/question-sets/4/questions/7/solved' }).flush({});
  });
});
