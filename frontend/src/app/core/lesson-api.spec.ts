import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { LessonApi } from './lesson-api';

describe('LessonApi', () => {
  let api: LessonApi;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    api = TestBed.inject(LessonApi);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('calls the lesson endpoints', () => {
    const request = {
      title: 'Flexbox',
      summary: null,
      sections: [{ title: 'Intro', body: 'Text' }],
    };
    api.list().subscribe();
    api.get(4).subscribe();
    api.create(request).subscribe();
    api.update(4, request).subscribe();
    api.delete(4).subscribe();
    api.recordOpen(4).subscribe();
    api.recordProgress(4, 70).subscribe();

    http.expectOne({ method: 'GET', url: '/api/v1/lessons' }).flush([]);
    http.expectOne({ method: 'GET', url: '/api/v1/lessons/4' }).flush({});
    expect(http.expectOne({ method: 'POST', url: '/api/v1/lessons' }).request.body).toEqual(
      request,
    );
    expect(http.expectOne({ method: 'PUT', url: '/api/v1/lessons/4' }).request.body).toEqual(
      request,
    );
    http.expectOne({ method: 'DELETE', url: '/api/v1/lessons/4' }).flush(null);
    http.expectOne({ method: 'POST', url: '/api/v1/lessons/4/opens' }).flush({});
    expect(
      http.expectOne({ method: 'POST', url: '/api/v1/lessons/4/progress' }).request.body,
    ).toEqual({ progress: 70 });
  });
});
