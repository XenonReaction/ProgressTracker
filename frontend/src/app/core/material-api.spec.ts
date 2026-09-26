import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { MaterialApi } from './material-api';

describe('MaterialApi', () => {
  let api: MaterialApi;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    api = TestBed.inject(MaterialApi);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('calls the material endpoints', () => {
    const request = { title: 'Guide', url: 'https://example.com', notes: null };
    api.list().subscribe();
    api.get(4).subscribe();
    api.create(request).subscribe();
    api.update(4, request).subscribe();
    api.delete(4).subscribe();
    api.history(4).subscribe();
    api.recordProgress(4, { progress: 60, note: 'Halfway' }).subscribe();

    http.expectOne({ method: 'GET', url: '/api/v1/materials' }).flush([]);
    http.expectOne({ method: 'GET', url: '/api/v1/materials/4' }).flush({});
    expect(http.expectOne({ method: 'POST', url: '/api/v1/materials' }).request.body).toEqual(
      request,
    );
    expect(http.expectOne({ method: 'PUT', url: '/api/v1/materials/4' }).request.body).toEqual(
      request,
    );
    http.expectOne({ method: 'DELETE', url: '/api/v1/materials/4' }).flush(null);
    http.expectOne({ method: 'GET', url: '/api/v1/materials/4/progress' }).flush([]);
    expect(
      http.expectOne({ method: 'POST', url: '/api/v1/materials/4/progress' }).request.body,
    ).toEqual({
      progress: 60,
      note: 'Halfway',
    });
  });
});
