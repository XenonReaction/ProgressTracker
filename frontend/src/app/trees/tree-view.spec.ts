import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aTreeNode } from '../core/test-data';
import { TreeView } from './tree-view';

describe('TreeView', () => {
  let fixture: ComponentFixture<TreeView>;
  let http: HttpTestingController;
  let page: HTMLElement;

  // syntax (95) -> oop (80) -> streams (15); oop is ready, streams is locked (needs >= 80 avg)
  const syntax = aTreeNode({ id: 1, nodeId: 11, title: 'Java Syntax', readiness: 95, positionY: 0, dependentIds: [2] });
  const oop = aTreeNode({ id: 2, nodeId: 12, title: 'OOP', readiness: 60, positionY: 150, prerequisiteIds: [1], dependentIds: [3] });
  const streams = aTreeNode({ id: 3, nodeId: 13, title: 'Streams', readiness: 15, positionY: 300, prerequisiteIds: [2] });

  beforeEach(async () => {
    TestBed.configureTestingModule({ providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()] });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(TreeView);
    page = fixture.nativeElement;
    fixture.componentRef.setInput('id', '4');
    await fixture.whenStable();
    http.expectOne('/api/v1/trees/4').flush({ id: 4, title: 'Java Fundamentals', description: 'Core skills', category: null, tags: [], createdAt: '', updatedAt: '' });
    http.expectOne('/api/v1/trees/4/nodes').flush([syntax, oop, streams]);
    await fixture.whenStable();
  });

  afterEach(() => http.verify());

  it('draws every node at its position and every edge', () => {
    expect(page.querySelector('h1')?.textContent).toBe('Java Fundamentals');
    expect(nodes().length).toBe(3);
    expect(nodeEl(2).getAttribute('transform')).toBe('translate(-90 122)');
    expect(page.querySelectorAll('line.edge').length).toBe(2);
  });

  it('styles nodes as ready or locked from their prerequisites', () => {
    expect(nodeEl(1).classList).toContain('ready'); // no prerequisites
    expect(nodeEl(2).classList).toContain('ready'); // syntax 95 meets 80/70
    expect(nodeEl(3).classList).toContain('locked'); // oop 60 is below 80/70
    expect(nodeEl(3).classList).toContain('node');
  });

  it('shows what a clicked node needs and unlocks, with a link to edit its readiness', async () => {
    nodeEl(2).dispatchEvent(new Event('click'));
    await fixture.whenStable();

    const details = page.querySelector('aside')!;
    expect(details.querySelector('h2')?.textContent).toBe('OOP');
    const [needs, unlocks] = Array.from(details.querySelectorAll('ul'));
    expect(needs.textContent).toContain('Java Syntax (95%)');
    expect(unlocks.textContent).toContain('Streams');
    expect(details.querySelector('a')?.getAttribute('href')).toBe('/nodes/12/edit?returnTo=%2Ftrees%2F4');
    expect(nodeEl(2).classList).toContain('selected');
  });

  it('clicking the selected node again closes the details', async () => {
    nodeEl(2).dispatchEvent(new Event('click'));
    await fixture.whenStable();
    nodeEl(2).dispatchEvent(new Event('click'));
    await fixture.whenStable();

    expect(page.querySelector('aside')).toBeNull();
  });

  function nodes(): SVGGElement[] {
    return Array.from(page.querySelectorAll('g.node'));
  }

  function nodeEl(treeNodeId: number): SVGGElement {
    return page.querySelector(`g[data-tree-node-id="${treeNodeId}"]`) as SVGGElement;
  }
});
