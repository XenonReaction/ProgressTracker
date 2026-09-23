import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { NodeApi } from './node-api';
import { aNode } from './test-data';

describe('NodeApi', () => {
  let api: NodeApi;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    api = TestBed.inject(NodeApi);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('lists nodes', () => {
    let result: unknown;
    api.list().subscribe((nodes) => (result = nodes));
    http.expectOne({ method: 'GET', url: '/api/v1/nodes' }).flush([aNode()]);
    expect(result).toEqual([aNode()]);
  });

  it('gets, creates, updates and deletes by id', () => {
    const request = { title: 'Generics', description: null, readiness: 10, links: [] };
    api.get(3).subscribe();
    api.create(request).subscribe();
    api.update(3, request).subscribe();
    api.delete(3).subscribe();

    http.expectOne({ method: 'GET', url: '/api/v1/nodes/3' }).flush(aNode());
    const create = http.expectOne({ method: 'POST', url: '/api/v1/nodes' });
    expect(create.request.body).toEqual(request);
    create.flush(aNode());
    const update = http.expectOne({ method: 'PUT', url: '/api/v1/nodes/3' });
    expect(update.request.body).toEqual(request);
    update.flush(aNode());
    http.expectOne({ method: 'DELETE', url: '/api/v1/nodes/3' }).flush(null, { status: 204, statusText: 'No Content' });
  });
});
