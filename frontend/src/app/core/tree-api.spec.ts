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

  it('edits a tree\'s nodes and edges', () => {
    api.addNode(4, { nodeId: 2, positionX: 10, positionY: 20 }).subscribe();
    api.updateNode(4, 7, { positionX: 1, positionY: 2, aggregateThreshold: 80, individualThreshold: 70 }).subscribe();
    api.updatePositions(4, [{ treeNodeId: 7, positionX: 0, positionY: 0 }]).subscribe();
    api.removeNode(4, 7).subscribe();
    api.prerequisites(4).subscribe();
    api.addPrerequisite(4, 7, 8).subscribe();
    api.removePrerequisite(4, 9).subscribe();

    expect(http.expectOne({ method: 'POST', url: '/api/v1/trees/4/nodes' }).request.body).toEqual({ nodeId: 2, positionX: 10, positionY: 20 });
    expect(http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/7' }).request.body.aggregateThreshold).toBe(80);
    expect(http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/positions' }).request.body).toEqual({
      positions: [{ treeNodeId: 7, positionX: 0, positionY: 0 }],
    });
    http.expectOne({ method: 'DELETE', url: '/api/v1/trees/4/nodes/7' });
    http.expectOne({ method: 'GET', url: '/api/v1/trees/4/prerequisites' });
    expect(http.expectOne({ method: 'POST', url: '/api/v1/trees/4/prerequisites' }).request.body).toEqual({
      prerequisiteTreeNodeId: 7,
      dependentTreeNodeId: 8,
    });
    http.expectOne({ method: 'DELETE', url: '/api/v1/trees/4/prerequisites/9' });
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
