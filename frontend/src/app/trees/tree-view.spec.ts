import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, TestRequest, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';

import { Prerequisite } from '../core/api.models';
import { aNode, aTree, aTreeNode } from '../core/test-data';
import { TreeView } from './tree-view';

describe('TreeView', () => {
  let fixture: ComponentFixture<TreeView>;
  let http: HttpTestingController;
  let page: HTMLElement;

  // syntax (95) -> oop (60) -> streams (15); oop is ready (95 meets 80/70), streams is locked
  const syntax = aTreeNode({ id: 1, nodeId: 11, title: 'Java Syntax', readiness: 95, positionY: 0, dependentIds: [2] });
  const oop = aTreeNode({ id: 2, nodeId: 12, title: 'OOP', readiness: 60, positionY: 150, prerequisiteIds: [1], dependentIds: [3] });
  const streams = aTreeNode({ id: 3, nodeId: 13, title: 'Streams', readiness: 15, positionY: 300, prerequisiteIds: [2] });
  const edges: Prerequisite[] = [
    { id: 21, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 2, route: null },
    { id: 22, prerequisiteTreeNodeId: 2, dependentTreeNodeId: 3, route: null },
  ];

  beforeEach(async () => {
    TestBed.configureTestingModule({ providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()] });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(TreeView);
    page = fixture.nativeElement;
    fixture.componentRef.setInput('id', '4');
    await fixture.whenStable();
    http.expectOne('/api/v1/trees/4').flush(
      aTree({ id: 4, title: 'Java Fundamentals', description: 'Core skills', category: 'Technology', tags: ['java'] }),
    );
    http.expectOne('/api/v1/trees/4/nodes').flush([syntax, oop, streams]);
    http.expectOne('/api/v1/trees/4/prerequisites').flush(edges);
    await fixture.whenStable();
  });

  afterEach(() => {
    http.verify();
    vi.restoreAllMocks();
  });

  describe('viewing', () => {
    it('draws every node at its position and every edge', () => {
      expect(page.querySelector('h1')?.textContent).toBe('Java Fundamentals');
      expect(page.querySelectorAll('g.node').length).toBe(3);
      expect(nodeEl(2).getAttribute('transform')).toBe('translate(-90 122)');
      expect(page.querySelectorAll('g.edge').length).toBe(2);
    });

    it('styles nodes as ready or locked from their prerequisites', () => {
      expect(nodeEl(1).classList).toContain('ready'); // no prerequisites
      expect(nodeEl(2).classList).toContain('ready'); // syntax 95 meets 80/70
      expect(nodeEl(3).classList).toContain('locked'); // oop 60 is below 80/70
      expect(nodeEl(3).classList).toContain('node');
    });

    it('shows the tree metadata and opens in view mode, without editing tools', () => {
      expect(page.textContent).toContain('Category: Technology');
      expect(page.textContent).toContain('Tags: java');
      expect(button('Edit')).toBeTruthy();
      expect(page.querySelector('[role=toolbar]')).toBeNull();
      expect(link('Edit details')).toBeUndefined();
    });

    it('shows a clicked node with its links and a readiness editor, and no thresholds', async () => {
      await pressAndRelease(2);

      const details = page.querySelector('aside')!;
      expect(details.querySelector('h2')?.textContent).toBe('OOP');
      expect(details.querySelector('app-readiness-editor input')).not.toBeNull();
      expect(thresholdInput('aggregateThreshold')).toBeNull();
      expect(details.querySelector('ul')?.textContent).toContain('Java Syntax (95%)');
      expect(link('Open node page').getAttribute('href')).toBe('/nodes/12');
      expect(button('Remove from tree', false)).toBeUndefined();
    });

    it('saves a new hand-entered readiness and reloads the tree', async () => {
      await pressAndRelease(2);
      type(page.querySelector('app-readiness-editor input') as HTMLInputElement, '85');
      await fixture.whenStable();
      page.querySelector('app-readiness-editor form')!.dispatchEvent(new Event('submit'));

      const request = http.expectOne({ method: 'PUT', url: '/api/v1/nodes/12/readiness' });
      expect(request.request.body).toEqual({ readiness: 85 });
      request.flush(aNode({ id: 12, readiness: 85 }));
      expectReload([syntax, { ...oop, readiness: 85 }, streams]);
      await fixture.whenStable();

      expect(nodeEl(3).classList).toContain('ready'); // OOP's 85 now meets Streams' 80/70
    });

    it('does not drag nodes in view mode', async () => {
      mouse('mousedown', nodeEl(2), 0, 0);
      mouse('mousemove', canvas(), 50, 20);
      mouse('mouseup', canvas(), 50, 20);
      await fixture.whenStable();

      expect(nodeEl(2).getAttribute('transform')).toBe('translate(-90 122)');
      http.expectNone({ method: 'PUT' });
    });
  });

  describe('edit mode', () => {
    beforeEach(async () => {
      await enterEditMode();
    });

    it('starts an edit session and shows the tools, starting with select', () => {
      expect(button('Select / move').getAttribute('aria-pressed')).toBe('true');
      expect(button('Connect').getAttribute('aria-pressed')).toBe('false');
      expect(link('Edit details').getAttribute('href')).toBe('/trees/4/edit?resumeEdit=true');
      expect(button('Edit', false)).toBeUndefined();
    });

    it('"Done" keeps the changes and goes back to view mode', async () => {
      button('Done').click();
      http.expectOne({ method: 'DELETE', url: '/api/v1/trees/4/edit-session' }).flush(null, { status: 204, statusText: 'No Content' });
      await fixture.whenStable();

      expect(page.querySelector('[role=toolbar]')).toBeNull();
      expect(button('Edit')).toBeTruthy();
    });

    it('"Discard changes" asks, restores the tree and reloads it', async () => {
      vi.spyOn(window, 'confirm').mockReturnValue(true);

      button('Discard changes').click();
      http.expectOne({ method: 'POST', url: '/api/v1/trees/4/edit-session/discard' }).flush(null, { status: 204, statusText: 'No Content' });
      http.expectOne('/api/v1/trees/4').flush(aTree({ id: 4, title: 'Java Fundamentals' }));
      expectReload();
      await fixture.whenStable();

      expect(page.querySelector('[role=toolbar]')).toBeNull();
    });

    it('asks before leaving, and discards the changes when the user leaves anyway', async () => {
      const confirm = vi.spyOn(window, 'confirm').mockReturnValue(false);
      expect(component().canLeave('/nodes')).toBe(false);
      expect(confirm.mock.calls[0][0]).toContain('Leave without saving?');

      confirm.mockReturnValue(true);
      const leaving = component().canLeave('/nodes');
      http.expectOne({ method: 'POST', url: '/api/v1/trees/4/edit-session/discard' }).flush(null, { status: 204, statusText: 'No Content' });
      expect(await leaving).toBe(true);
    });

    it('lets the user open the tree details form without leaving the session', () => {
      const confirm = vi.spyOn(window, 'confirm');
      expect(component().canLeave('/trees/4/edit?resumeEdit=true')).toBe(true);
      expect(confirm).not.toHaveBeenCalled();
    });

    it('asks the browser to warn before the tab is closed', () => {
      const event = new Event('beforeunload', { cancelable: true });
      window.dispatchEvent(event);
      expect(event.defaultPrevented).toBe(true);
    });
  });

  describe('right-angle edges', () => {
    function edgeLine(id: number): string | null {
      return page.querySelector(`g[data-edge-id="${id}"] polyline.edge-line`)!.getAttribute('points');
    }

    function handle(edgeId: number): SVGLineElement | null {
      return page.querySelector(`g[data-edge-id="${edgeId}"] line.segment-handle`);
    }

    it('leave the bottom of the prerequisite and enter the top of the dependent', () => {
      // Syntax (0, 0) → OOP (0, 150): out at y 28, in at y 122, the middle segment halfway
      expect(edgeLine(21)).toBe('0,28 0,75 0,75 0,122');
    });

    it('offer draggable segments only in edit mode with the select tool', async () => {
      expect(handle(21)).toBeNull();
      await enterEditMode();
      expect(handle(21)).not.toBeNull();
      button('Connect').click();
      await fixture.whenStable();
      expect(handle(21)).toBeNull();
    });

    it('saves a dragged segment as the edge route, and undo puts the default back', async () => {
      await enterEditMode();

      mouse('mousedown', handle(21)!, 0, 0);
      mouse('mousemove', canvas(), 0, 20);
      await fixture.whenStable();
      expect(edgeLine(21)).toBe('0,28 0,95 0,95 0,122');
      mouse('mouseup', canvas(), 0, 20);

      const save = http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/prerequisites/21/route' });
      expect(save.request.body).toEqual({ route: { segments: 3, offsets: [20] } });
      save.flush({ ...edges[0], route: { segments: 3, offsets: [20] } });
      await fixture.whenStable();
      expect(button('Undo').getAttribute('title')).toBe('Undo reshape arrow "Java Syntax" → "OOP" (Ctrl+Z)');

      button('Undo').click();
      const undo = http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/prerequisites/21/route' });
      expect(undo.request.body).toEqual({ route: null });
      undo.flush(edges[0]);
      expectReload();
    });

    it('resets a route when a move changes its number of segments, and undo restores both', async () => {
      await enterEditMode();
      mouse('mousedown', handle(21)!, 0, 0);
      mouse('mousemove', canvas(), 0, 20);
      mouse('mouseup', canvas(), 0, 20);
      const routed = { ...edges[0], route: { segments: 3 as const, offsets: [20] } };
      http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/prerequisites/21/route' }).flush(routed);
      await fixture.whenStable();

      // Move OOP above Java Syntax: that edge now needs 5 segments
      mouse('mousedown', nodeEl(2), 0, 0);
      mouse('mousemove', canvas(), 0, -250);
      mouse('mouseup', canvas(), 0, -250);
      http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/2' }).flush({ ...oop, positionY: -100 });
      const reset = http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/prerequisites/21/route' });
      expect(reset.request.body).toEqual({ route: null });
      reset.flush(edges[0]);
      await fixture.whenStable();

      button('Undo').click();
      http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/2' }).flush(oop);
      const restore = http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/prerequisites/21/route' });
      expect(restore.request.body).toEqual({ route: { segments: 3, offsets: [20] } });
      restore.flush(routed);
      expectReload();
      http.expectNone({ method: 'DELETE' });
    });
  });

  describe('undo and redo', () => {
    const noContent = { status: 204, statusText: 'No Content' };

    beforeEach(async () => {
      await enterEditMode();
    });

    it('starts with nothing to undo or redo', () => {
      expect(button('Undo').disabled).toBe(true);
      expect(button('Redo').disabled).toBe(true);
    });

    it('undoes and redoes a move', async () => {
      mouse('mousedown', nodeEl(2), 0, 0);
      mouse('mousemove', canvas(), 50, 20);
      mouse('mouseup', canvas(), 50, 20);
      http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/2' }).flush({ ...oop, positionX: 50, positionY: 170 });
      await fixture.whenStable();
      expect(button('Undo').getAttribute('title')).toBe('Undo move "OOP" (Ctrl+Z)');

      button('Undo').click();
      const undo = http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/2' });
      expect(undo.request.body).toEqual({ positionX: 0, positionY: 150, aggregateThreshold: 80, individualThreshold: 70 });
      undo.flush(oop);
      expectReload();
      await fixture.whenStable();
      expect(button('Redo').disabled).toBe(false);

      button('Redo').click();
      const redo = http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/2' });
      expect(redo.request.body).toEqual({ positionX: 50, positionY: 170, aggregateThreshold: 80, individualThreshold: 70 });
      redo.flush({ ...oop, positionX: 50, positionY: 170 });
      expectReload();
    });

    it('undoes a new arrow by deleting it, and redo adds it again', async () => {
      button('Connect').click();
      await fixture.whenStable();
      click(nodeEl(1));
      click(nodeEl(3));
      http.expectOne({ method: 'POST', url: '/api/v1/trees/4/prerequisites' }).flush({ id: 23, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 3, route: null });
      const withNewEdge = [...edges, { id: 23, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 3, route: null }];
      expectReload([syntax, oop, streams], withNewEdge);
      await fixture.whenStable();

      button('Undo').click();
      http.expectOne({ method: 'DELETE', url: '/api/v1/trees/4/prerequisites/23' }).flush(null, noContent);
      expectReload();
      await fixture.whenStable();

      button('Redo').click();
      const redo = http.expectOne({ method: 'POST', url: '/api/v1/trees/4/prerequisites' });
      expect(redo.request.body).toEqual({ prerequisiteTreeNodeId: 1, dependentTreeNodeId: 3 });
      redo.flush({ id: 24, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 3, route: null });
      expectReload();
    });

    it('undoing a removal places the node again with its arrows', async () => {
      vi.spyOn(window, 'confirm').mockReturnValue(true);
      button('Delete').click();
      await fixture.whenStable();
      click(nodeEl(2));
      http.expectOne({ method: 'DELETE', url: '/api/v1/trees/4/nodes/2' }).flush(null, noContent);
      expectReload([syntax, streams], []);
      await fixture.whenStable();

      button('Undo').click();
      const place = http.expectOne({ method: 'POST', url: '/api/v1/trees/4/nodes' });
      expect(place.request.body).toEqual({ nodeId: 12, positionX: 0, positionY: 150, aggregateThreshold: 80, individualThreshold: 70 });
      place.flush({ ...oop, id: 8 });
      const arrows = http.match({ method: 'POST', url: '/api/v1/trees/4/prerequisites' });
      expect(arrows.map((r) => r.request.body)).toEqual([
        { prerequisiteTreeNodeId: 1, dependentTreeNodeId: 8 },
        { prerequisiteTreeNodeId: 8, dependentTreeNodeId: 3 },
      ]);
      arrows.forEach((r, i) => r.flush({ id: 30 + i, route: null, ...r.request.body }));
      expectReload();
    });

    it('undoing a created node deletes it from the library, and redo creates it again', async () => {
      button('Add node').click();
      http.expectOne({ method: 'GET', url: '/api/v1/nodes' }).flush([]);
      await fixture.whenStable();
      mouse('click', page.querySelector('rect.background')!, 10, 20);
      await fixture.whenStable();
      (page.querySelector('app-add-node-panel input') as HTMLInputElement).value = 'Spring Core';
      page.querySelector('app-add-node-panel form')!.dispatchEvent(new Event('submit'));
      http.expectOne({ method: 'POST', url: '/api/v1/nodes' }).flush(aNode({ id: 15, title: 'Spring Core' }));
      const created = aTreeNode({ id: 5, nodeId: 15, title: 'Spring Core', positionX: -200, positionY: -128 });
      http.expectOne({ method: 'POST', url: '/api/v1/trees/4/nodes' }).flush(created);
      expectReload([syntax, oop, streams, created]);
      await fixture.whenStable();

      button('Undo').click();
      http.expectOne({ method: 'DELETE', url: '/api/v1/trees/4/nodes/5' }).flush(null, noContent);
      http.expectOne({ method: 'GET', url: '/api/v1/nodes/15/trees' }).flush([]);
      http.expectOne({ method: 'DELETE', url: '/api/v1/nodes/15' }).flush(null, noContent);
      expectReload();
      await fixture.whenStable();

      button('Redo').click();
      const recreate = http.expectOne({ method: 'POST', url: '/api/v1/nodes' });
      expect(recreate.request.body).toEqual({ title: 'Spring Core', description: null, readiness: 0, links: [] });
      recreate.flush(aNode({ id: 16, title: 'Spring Core' }));
      const place = http.expectOne({ method: 'POST', url: '/api/v1/trees/4/nodes' });
      expect(place.request.body).toEqual({ nodeId: 16, positionX: -200, positionY: -128, aggregateThreshold: 80, individualThreshold: 70 });
      place.flush({ ...created, id: 6, nodeId: 16 });
      expectReload([syntax, oop, streams, { ...created, id: 6, nodeId: 16 }]);
      await fixture.whenStable();

      // Undo again finds the recreated node under its new ids
      button('Undo').click();
      http.expectOne({ method: 'DELETE', url: '/api/v1/trees/4/nodes/6' }).flush(null, noContent);
      http.expectOne({ method: 'GET', url: '/api/v1/nodes/16/trees' }).flush([]);
      http.expectOne({ method: 'DELETE', url: '/api/v1/nodes/16' }).flush(null, noContent);
      expectReload();
    });

    it('undoes with Ctrl+Z and redoes with Ctrl+Y or Ctrl+Shift+Z, but not while typing', async () => {
      await dragOopAndSave();

      const input = document.createElement('input');
      page.appendChild(input);
      input.dispatchEvent(new KeyboardEvent('keydown', { key: 'z', ctrlKey: true, bubbles: true }));
      http.expectNone({ method: 'PUT' });

      document.dispatchEvent(new KeyboardEvent('keydown', { key: 'z', ctrlKey: true }));
      http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/2' }).flush(oop);
      expectReload();
      await fixture.whenStable();

      document.dispatchEvent(new KeyboardEvent('keydown', { key: 'Z', ctrlKey: true, shiftKey: true }));
      http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/2' }).flush(oop);
      expectReload();
      await fixture.whenStable();

      document.dispatchEvent(new KeyboardEvent('keydown', { key: 'z', ctrlKey: true }));
      http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/2' }).flush(oop);
      expectReload();
      await fixture.whenStable();
      document.dispatchEvent(new KeyboardEvent('keydown', { key: 'y', ctrlKey: true }));
      http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/2' }).flush(oop);
      expectReload();
    });

    it('clears the history and says why when an undo fails', async () => {
      await dragOopAndSave();

      button('Undo').click();
      http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/2' }).flush(
        { status: 404, title: 'Not Found', detail: 'Tree node 2 not found' },
        { status: 404, statusText: 'Not Found' },
      );
      expectReload();
      await fixture.whenStable();

      expect(page.querySelector('[role=alert]')?.textContent).toContain(
        "Couldn't undo: Tree node 2 not found. The undo history has been cleared.",
      );
      expect(button('Undo').disabled).toBe(true);
      expect(button('Redo').disabled).toBe(true);
    });

    it('ends the history when edit mode ends', async () => {
      await dragOopAndSave();

      button('Done').click();
      http.expectOne({ method: 'DELETE', url: '/api/v1/trees/4/edit-session' }).flush(null, noContent);
      await fixture.whenStable();
      await enterEditMode();

      expect(button('Undo').disabled).toBe(true);
    });

    async function dragOopAndSave(): Promise<void> {
      mouse('mousedown', nodeEl(2), 0, 0);
      mouse('mousemove', canvas(), 50, 20);
      mouse('mouseup', canvas(), 50, 20);
      http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/2' }).flush({ ...oop, positionX: 50, positionY: 170 });
      await fixture.whenStable();
    }
  });

  describe('an unfinished edit session', () => {
    beforeEach(async () => {
      fixture.componentRef.setInput('id', '6');
      await fixture.whenStable();
      http.expectOne('/api/v1/trees/6').flush(aTree({ id: 6, editSessionStartedAt: '2026-01-01T00:00:00Z' }));
      http.expectOne('/api/v1/trees/6/nodes').flush([]);
      http.expectOne('/api/v1/trees/6/prerequisites').flush([]);
      await fixture.whenStable();
    });

    it('asks whether to keep or discard it before anything else', () => {
      expect(page.textContent).toContain("unsaved changes from an edit session that didn't finish");
      expect(button('Edit', false)).toBeUndefined();
      expect(component().canLeave('/nodes')).toBe(true);
    });

    it('"Keep changes" finishes the session', async () => {
      button('Keep changes').click();
      http.expectOne({ method: 'DELETE', url: '/api/v1/trees/6/edit-session' }).flush(null, { status: 204, statusText: 'No Content' });
      await fixture.whenStable();

      expect(page.textContent).not.toContain("didn't finish");
      expect(button('Edit')).toBeTruthy();
    });

    it('"Discard changes" restores the tree', async () => {
      button('Discard changes').click();
      http.expectOne({ method: 'POST', url: '/api/v1/trees/6/edit-session/discard' }).flush(null, { status: 204, statusText: 'No Content' });
      http.expectOne('/api/v1/trees/6').flush(aTree({ id: 6 }));
      http.expectOne('/api/v1/trees/6/nodes').flush([]);
      http.expectOne('/api/v1/trees/6/prerequisites').flush([]);
      await fixture.whenStable();

      expect(page.textContent).not.toContain("didn't finish");
    });
  });

  it('resumes edit mode when coming back from the details form', async () => {
    const navigate = vi.spyOn(TestBed.inject(Router), 'navigate').mockResolvedValue(true);
    fixture.componentRef.setInput('resumeEdit', 'true');
    fixture.componentRef.setInput('id', '6');
    await fixture.whenStable();
    http.expectOne('/api/v1/trees/6').flush(aTree({ id: 6, editSessionStartedAt: '2026-01-01T00:00:00Z' }));
    http.expectOne('/api/v1/trees/6/nodes').flush([]);
    http.expectOne('/api/v1/trees/6/prerequisites').flush([]);
    await fixture.whenStable();

    expect(page.querySelector('[role=toolbar]')).not.toBeNull();
    expect(navigate).toHaveBeenCalledWith([], { queryParams: {}, replaceUrl: true });
  });

  describe('linked nodes', () => {
    const collections = aTreeNode({
      id: 7,
      nodeId: 17,
      title: 'Collections',
      readiness: 57,
      linkedTree: { id: 9, title: 'Collections in Depth' },
    });

    beforeEach(async () => {
      // Going to another tree reuses the component, as "Open linked tree" does
      fixture.componentRef.setInput('id', '5');
      await fixture.whenStable();
      http.expectOne('/api/v1/trees/5').flush(aTree({ id: 5, title: 'Java' }));
      http.expectOne('/api/v1/trees/5/nodes').flush([collections]);
      http.expectOne('/api/v1/trees/5/prerequisites').flush([]);
      await fixture.whenStable();
    });

    it('loads the new tree when the id changes', () => {
      expect(page.querySelector('h1')?.textContent).toBe('Java');
      expect(page.querySelectorAll('g.node').length).toBe(1);
    });

    it('marks a linked node on the canvas', () => {
      expect(nodeEl(7).querySelector('.linked-marker')?.textContent).toBe('linked');
      expect(nodeEl(7).getAttribute('aria-label')).toBe('Collections, 57% ready, from linked tree Collections in Depth, ready');
    });

    it('names the linked tree in the details, with a link to open it and no readiness editor', async () => {
      await pressAndRelease(7);

      expect(page.querySelector('aside')?.textContent).toContain('from the linked tree Collections in Depth');
      expect(link('Open linked tree').getAttribute('href')).toBe('/trees/9');
      expect(page.querySelector('app-readiness-editor')).toBeNull();
    });
  });

  describe('select tool', () => {
    beforeEach(async () => {
      await enterEditMode();
    });

    it('shows what a clicked node needs and unlocks, with its thresholds', async () => {
      await pressAndRelease(2);

      const details = page.querySelector('aside')!;
      expect(details.querySelector('h2')?.textContent).toBe('OOP');
      const [needs, unlocks] = Array.from(details.querySelectorAll('ul'));
      expect(needs.textContent).toContain('Java Syntax (95%)');
      expect(unlocks.textContent).toContain('Streams');
      expect(thresholdInput('aggregateThreshold').value).toBe('80');
      expect(thresholdInput('individualThreshold').value).toBe('70');
      expect(page.querySelector('app-readiness-editor')).toBeNull();
      expect(nodeEl(2).classList).toContain('selected');
    });

    it('closes the details when the selected node is clicked again', async () => {
      await pressAndRelease(2);
      await pressAndRelease(2);

      expect(page.querySelector('aside')).toBeNull();
    });

    it('selects with the keyboard too', async () => {
      nodeEl(3).dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter' }));
      await fixture.whenStable();

      expect(page.querySelector('aside h2')?.textContent).toBe('Streams');
    });

    it('saves new thresholds and restyles the node', async () => {
      await pressAndRelease(3);
      type(thresholdInput('aggregateThreshold'), '50');
      type(thresholdInput('individualThreshold'), '50');
      page.querySelector('aside form')!.dispatchEvent(new Event('submit'));

      const request = http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/3' });
      expect(request.request.body).toEqual({ positionX: 0, positionY: 300, aggregateThreshold: 50, individualThreshold: 50 });
      request.flush({ ...streams, aggregateThreshold: 50, individualThreshold: 50 });
      await fixture.whenStable();

      expect(nodeEl(3).classList).toContain('ready'); // oop's 60 now meets 50/50
    });

    it('does not save thresholds outside 0-100', async () => {
      await pressAndRelease(3);
      type(thresholdInput('aggregateThreshold'), '120');
      page.querySelector('aside form')!.dispatchEvent(new Event('submit'));
      await fixture.whenStable();

      http.expectNone({ method: 'PUT' });
      expect(page.querySelector('aside')?.textContent).toContain('Thresholds must be whole numbers from 0 to 100');
    });

    it('drags a node and saves where it was dropped', async () => {
      mouse('mousedown', nodeEl(2), 0, 0);
      mouse('mousemove', canvas(), 50, 20);
      mouse('mouseup', canvas(), 50, 20);
      await fixture.whenStable();

      expect(nodeEl(2).getAttribute('transform')).toBe('translate(-40 142)');
      const request = http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/2' });
      expect(request.request.body).toEqual({ positionX: 50, positionY: 170, aggregateThreshold: 80, individualThreshold: 70 });
      request.flush({ ...oop, positionX: 50, positionY: 170 });
      expect(page.querySelector('aside')).toBeNull(); // a drag doesn't select
    });

    it('puts a dragged node back if saving fails', async () => {
      mouse('mousedown', nodeEl(2), 0, 0);
      mouse('mousemove', canvas(), 50, 20);
      mouse('mouseup', canvas(), 50, 20);
      http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/2' }).flush(
        { status: 404, title: 'Not Found', detail: 'Tree node 2 not found' },
        { status: 404, statusText: 'Not Found' },
      );
      expectReload();
      await fixture.whenStable();

      expect(page.querySelector('[role=alert]')?.textContent).toContain('Tree node 2 not found');
      expect(nodeEl(2).getAttribute('transform')).toBe('translate(-90 122)');
    });

    it('ignores tiny pointer jitter and treats it as a click', async () => {
      mouse('mousedown', nodeEl(2), 0, 0);
      mouse('mousemove', canvas(), 1, 1);
      mouse('mouseup', canvas(), 1, 1);
      await fixture.whenStable();

      http.expectNone({ method: 'PUT' });
      expect(page.querySelector('aside h2')?.textContent).toBe('OOP');
    });

    it('removes a node from the tree after confirmation', async () => {
      vi.spyOn(window, 'confirm').mockReturnValue(true);
      await pressAndRelease(3);

      button('Remove from tree').click();
      http.expectOne({ method: 'DELETE', url: '/api/v1/trees/4/nodes/3' }).flush(null, { status: 204, statusText: 'No Content' });
      expectReload([syntax, oop], [edges[0]]);
      await fixture.whenStable();

      expect(page.querySelectorAll('g.node').length).toBe(2);
      expect(page.querySelector('aside')).toBeNull();
    });
  });

  describe('connect tool', () => {
    beforeEach(async () => {
      await enterEditMode();
      button('Connect').click();
      await fixture.whenStable();
    });

    it('adds a prerequisite from the first clicked node to the second', async () => {
      click(nodeEl(1));
      await fixture.whenStable();
      expect(nodeEl(1).classList).toContain('connect-from');
      expect(page.textContent).toContain('Now click the node that "Java Syntax" unlocks');

      click(nodeEl(3));
      const request = http.expectOne({ method: 'POST', url: '/api/v1/trees/4/prerequisites' });
      expect(request.request.body).toEqual({ prerequisiteTreeNodeId: 1, dependentTreeNodeId: 3 });
      request.flush({ id: 23, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 3, route: null });
      expectReload([syntax, oop, { ...streams, prerequisiteIds: [2, 1] }], [...edges, { id: 23, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 3, route: null }]);
      await fixture.whenStable();

      expect(page.querySelectorAll('g.edge').length).toBe(3);
    });

    it('shows why the backend refused an edge, such as a cycle', async () => {
      click(nodeEl(3));
      click(nodeEl(1));
      http.expectOne({ method: 'POST', url: '/api/v1/trees/4/prerequisites' }).flush(
        { status: 409, title: 'Conflict', detail: 'Making tree node 3 a prerequisite of tree node 1 would create a cycle' },
        { status: 409, statusText: 'Conflict' },
      );
      await fixture.whenStable();

      expect(page.querySelector('[role=alert]')?.textContent).toContain('would create a cycle');
    });

    it('cancels with Escape or by clicking the same node again', async () => {
      click(nodeEl(1));
      document.dispatchEvent(new KeyboardEvent('keydown', { key: 'Escape' }));
      await fixture.whenStable();
      expect(nodeEl(1).classList).not.toContain('connect-from');

      click(nodeEl(1));
      click(nodeEl(1));
      await fixture.whenStable();
      expect(nodeEl(1).classList).not.toContain('connect-from');
      http.expectNone({ method: 'POST' });
    });
  });

  describe('delete tool', () => {
    beforeEach(async () => {
      await enterEditMode();
      button('Delete').click();
      await fixture.whenStable();
    });

    it('removes a clicked prerequisite arrow', async () => {
      click(page.querySelector('g[data-edge-id="22"]')!);

      http.expectOne({ method: 'DELETE', url: '/api/v1/trees/4/prerequisites/22' }).flush(null, { status: 204, statusText: 'No Content' });
      expectReload([syntax, oop, { ...streams, prerequisiteIds: [] }], [edges[0]]);
      await fixture.whenStable();

      expect(page.querySelectorAll('g.edge').length).toBe(1);
      expect(nodeEl(3).classList).toContain('ready');
    });

    it('removes a clicked node after confirmation, and keeps it when cancelled', () => {
      const confirm = vi.spyOn(window, 'confirm').mockReturnValue(false);
      click(nodeEl(2));
      http.expectNone({ method: 'DELETE' });
      expect(confirm.mock.calls[0][0]).toContain('The node stays in your library');

      confirm.mockReturnValue(true);
      click(nodeEl(2));
      http.expectOne({ method: 'DELETE', url: '/api/v1/trees/4/nodes/2' }).flush(null, { status: 204, statusText: 'No Content' });
      expectReload();
    });
  });

  describe('add tool', () => {
    beforeEach(async () => {
      await enterEditMode();
      button('Add node').click();
      http.expectOne({ method: 'GET', url: '/api/v1/nodes' }).flush([
        aNode({ id: 11, title: 'Java Syntax' }), // already in the tree
        aNode({ id: 14, title: 'Generics', readiness: 40 }),
      ]);
      await fixture.whenStable();
      // View box starts at (-210, -148): nodes span x 0..0, y 0..300, plus box size and padding
      mouse('click', page.querySelector('rect.background')!, 10, 20);
      await fixture.whenStable();
    });

    it('offers library nodes that are not already in the tree, at the clicked spot', () => {
      const panel = page.querySelector('app-add-node-panel')!;
      expect(panel.textContent).toContain('At (-200, -128)');
      const options = Array.from(panel.querySelectorAll('option')).map((o) => o.textContent);
      expect(options).toEqual(['Choose a node…', 'Generics (40%)']);
      expect(page.querySelector('circle.pending')).not.toBeNull();
    });

    it('places an existing library node there', async () => {
      const select = page.querySelector('app-add-node-panel select') as HTMLSelectElement;
      select.value = '14';
      button('Place').click();

      const request = http.expectOne({ method: 'POST', url: '/api/v1/trees/4/nodes' });
      expect(request.request.body).toEqual({ nodeId: 14, positionX: -200, positionY: -128 });
      request.flush(aTreeNode({ id: 5, nodeId: 14 }));
      expectReload();
      await fixture.whenStable();

      expect(page.querySelector('app-add-node-panel')).toBeNull();
    });

    it('creates a new library node and places it in one step', () => {
      (page.querySelector('app-add-node-panel input') as HTMLInputElement).value = 'Spring Core';
      page.querySelector('app-add-node-panel form')!.dispatchEvent(new Event('submit'));

      const create = http.expectOne({ method: 'POST', url: '/api/v1/nodes' });
      expect(create.request.body).toEqual({ title: 'Spring Core', description: null, readiness: 0, links: [] });
      create.flush(aNode({ id: 15, title: 'Spring Core', readiness: 0 }));
      const place = http.expectOne({ method: 'POST', url: '/api/v1/trees/4/nodes' });
      expect(place.request.body).toEqual({ nodeId: 15, positionX: -200, positionY: -128 });
      place.flush(aTreeNode({ id: 5, nodeId: 15 }));
      expectReload();
    });

    it('cancels without adding anything', async () => {
      button('Cancel').click();
      await fixture.whenStable();

      expect(page.querySelector('app-add-node-panel')).toBeNull();
      http.expectNone({ method: 'POST' });
    });
  });

  describe('reset to auto-layout', () => {
    beforeEach(async () => {
      await enterEditMode();
    });

    it('saves every position at once after confirmation', async () => {
      vi.spyOn(window, 'confirm').mockReturnValue(true);

      button('Reset to auto-layout').click();

      const request = http.expectOne({ method: 'PUT', url: '/api/v1/trees/4/nodes/positions' });
      expect(request.request.body.positions).toEqual([
        { treeNodeId: 1, positionX: 0, positionY: 0 },
        { treeNodeId: 2, positionX: 0, positionY: 150 },
        { treeNodeId: 3, positionX: 0, positionY: 300 },
      ]);
      request.flush([syntax, oop, { ...streams, positionX: 200 }]);
      // Every edge goes back to its default route too
      http.expectOne({ method: 'DELETE', url: '/api/v1/trees/4/prerequisites/routes' }).flush(null, { status: 204, statusText: 'No Content' });
      await fixture.whenStable();
      expect(nodeEl(3).getAttribute('transform')).toBe('translate(110 272)');
    });

    it('does nothing when cancelled', () => {
      vi.spyOn(window, 'confirm').mockReturnValue(false);
      button('Reset to auto-layout').click();
      http.expectNone({ method: 'PUT' });
    });
  });

  describe('deleting the tree', () => {
    beforeEach(async () => {
      await enterEditMode();
    });

    it('deletes after confirmation and returns to the tree list', () => {
      vi.spyOn(window, 'confirm').mockReturnValue(true);
      const navigate = vi.spyOn(TestBed.inject(Router), 'navigateByUrl').mockResolvedValue(true);

      button('Delete tree').click();
      http.expectOne({ method: 'DELETE', url: '/api/v1/trees/4' }).flush(null, { status: 204, statusText: 'No Content' });

      expect(navigate).toHaveBeenCalledWith('/trees');
    });

    it('keeps the tree when the user cancels', () => {
      vi.spyOn(window, 'confirm').mockReturnValue(false);
      button('Delete tree').click();
      http.expectNone({ method: 'DELETE' });
    });
  });

  // ---- helpers ----

  function nodeEl(treeNodeId: number): SVGGElement {
    return page.querySelector(`g[data-tree-node-id="${treeNodeId}"]`) as SVGGElement;
  }

  function canvas(): SVGSVGElement {
    return page.querySelector('svg') as SVGSVGElement;
  }

  function button(label: string, required = true): HTMLButtonElement {
    const found = Array.from(page.querySelectorAll('button')).find((b) => b.textContent?.trim() === label);
    if (!found && required) {
      throw new Error(`No button "${label}"`);
    }
    return found as HTMLButtonElement;
  }

  function component(): TreeView {
    return fixture.componentInstance;
  }

  async function enterEditMode(): Promise<void> {
    button('Edit').click();
    http.expectOne({ method: 'POST', url: '/api/v1/trees/4/edit-session' }).flush({ startedAt: '2026-01-01T00:00:00Z' });
    await fixture.whenStable();
  }

  function link(label: string): HTMLAnchorElement {
    return Array.from(page.querySelectorAll('a')).find((a) => a.textContent?.trim() === label) as HTMLAnchorElement;
  }

  function thresholdInput(name: string): HTMLInputElement {
    return page.querySelector(`input[formControlName=${name}]`) as HTMLInputElement;
  }

  function type(input: HTMLInputElement, value: string): void {
    input.value = value;
    input.dispatchEvent(new Event('input'));
  }

  function mouse(type: string, target: Element, clientX: number, clientY: number): void {
    target.dispatchEvent(new MouseEvent(type, { bubbles: true, button: 0, clientX, clientY }));
  }

  function click(target: Element): void {
    target.dispatchEvent(new MouseEvent('click', { bubbles: true }));
  }

  /** A mouse press and release on a node without moving, as the select tool sees a click. */
  async function pressAndRelease(treeNodeId: number): Promise<void> {
    mouse('mousedown', nodeEl(treeNodeId), 0, 0);
    mouse('mouseup', canvas(), 0, 0);
    click(nodeEl(treeNodeId));
    await fixture.whenStable();
  }

  /** After a structural change the view re-reads the tree's nodes and edges. */
  function expectReload(treeNodes = [syntax, oop, streams], prerequisites = edges): TestRequest[] {
    const nodesRequest = http.expectOne({ method: 'GET', url: '/api/v1/trees/4/nodes' });
    const edgesRequest = http.expectOne({ method: 'GET', url: '/api/v1/trees/4/prerequisites' });
    nodesRequest.flush(treeNodes);
    edgesRequest.flush(prerequisites);
    return [nodesRequest, edgesRequest];
  }
});
