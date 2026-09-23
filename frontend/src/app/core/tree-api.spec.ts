import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { TreeApi } from './tree-api';

describe('TreeApi', () => {
  let api: TreeApi;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    api = TestBed.inject(TreeApi);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('calls the tree endpoints', () => {
    api.list().subscribe();
    api.get(4).subscribe();
    api.nodes(4).subscribe();

    http.expectOne({ method: 'GET', url: '/api/v1/trees' }).flush([]);
    http.expectOne({ method: 'GET', url: '/api/v1/trees/4' }).flush({});
    http.expectOne({ method: 'GET', url: '/api/v1/trees/4/nodes' }).flush([]);
  });

  it('creates, updates and deletes trees', () => {
    const request = { title: 'Java', description: null, category: 'Technology', tags: ['java'] };
    api.create(request).subscribe();
    api.update(4, request).subscribe();
    api.delete(4).subscribe();

    const create = http.expectOne({ method: 'POST', url: '/api/v1/trees' });
    expect(create.request.body).toEqual(request);
    create.flush({});
    const update = http.expectOne({ method: 'PUT', url: '/api/v1/trees/4' });
    expect(update.request.body).toEqual(request);
    update.flush({});
    http.expectOne({ method: 'DELETE', url: '/api/v1/trees/4' }).flush(null, { status: 204, statusText: 'No Content' });
  });
});
