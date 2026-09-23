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
    { id: 21, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 2 },
    { id: 22, prerequisiteTreeNodeId: 2, dependentTreeNodeId: 3 },
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

    it('shows the tree metadata with a link to edit it', () => {
      expect(page.textContent).toContain('Category: Technology');
      expect(page.textContent).toContain('Tags: java');
      expect(link('Edit details').getAttribute('href')).toBe('/trees/4/edit');
    });

    it('starts with the select tool', () => {
      expect(button('Select / move').getAttribute('aria-pressed')).toBe('true');
      expect(button('Connect').getAttribute('aria-pressed')).toBe('false');
    });
  });

  describe('select tool', () => {
    it('shows what a clicked node needs and unlocks, with its thresholds', async () => {
      await pressAndRelease(2);

      const details = page.querySelector('aside')!;
      expect(details.querySelector('h2')?.textContent).toBe('OOP');
      const [needs, unlocks] = Array.from(details.querySelectorAll('ul'));
      expect(needs.textContent).toContain('Java Syntax (95%)');
      expect(unlocks.textContent).toContain('Streams');
      expect(thresholdInput('aggregateThreshold').value).toBe('80');
      expect(thresholdInput('individualThreshold').value).toBe('70');
      expect(link('Edit readiness').getAttribute('href')).toBe('/nodes/12/edit?returnTo=%2Ftrees%2F4');
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
      request.flush({ id: 23, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 3 });
      expectReload([syntax, oop, { ...streams, prerequisiteIds: [2, 1] }], [...edges, { id: 23, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 3 }]);
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

  function button(label: string): HTMLButtonElement {
    const found = Array.from(page.querySelectorAll('button')).find((b) => b.textContent?.trim() === label);
    if (!found) {
      throw new Error(`No button "${label}"`);
    }
    return found;
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
