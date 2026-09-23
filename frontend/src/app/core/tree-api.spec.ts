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
});
