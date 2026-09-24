import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aNode } from '../core/test-data';
import { NodeView } from './node-view';

describe('NodeView', () => {
  let fixture: ComponentFixture<NodeView>;
  let http: HttpTestingController;
  let page: HTMLElement;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()] });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(NodeView);
    page = fixture.nativeElement;
  });

  afterEach(() => http.verify());

  async function load(node = aNode(), trees = [{ id: 3, title: 'Java Fundamentals' }]): Promise<void> {
    fixture.componentRef.setInput('id', String(node.id));
    await fixture.whenStable();
    http.expectOne(`/api/v1/nodes/${node.id}`).flush(node);
    http.expectOne(`/api/v1/nodes/${node.id}/trees`).flush(trees);
    await fixture.whenStable();
  }

  it('shows the node read-only, with its links, tags, trees and an Edit link', async () => {
    await load(
      aNode({
        id: 5,
        title: 'Generics',
        description: 'Type parameters',
        tags: ['java', 'types'],
        links: [{ url: 'https://dev.java/learn/generics/', label: 'dev.java' }],
      }),
    );

    expect(page.querySelector('h1')?.textContent).toBe('Generics');
    expect(page.textContent).toContain('Type parameters');
    expect(page.textContent).toContain('Tags: java, types');
    expect(page.querySelector('input[type=text], textarea')).toBeNull();
    const external = link('dev.java');
    expect(external.getAttribute('href')).toBe('https://dev.java/learn/generics/');
    expect(external.getAttribute('target')).toBe('_blank');
    expect(link('Java Fundamentals').getAttribute('href')).toBe('/trees/3');
    expect(link('Edit').getAttribute('href')).toBe('/nodes/5/edit');
  });

  it('updates a hand-entered readiness in place', async () => {
    await load(aNode({ id: 5, readiness: 40 }));
    const input = page.querySelector('app-readiness-editor input') as HTMLInputElement;
    input.value = '65';
    input.dispatchEvent(new Event('input'));
    await fixture.whenStable();

    page.querySelector('app-readiness-editor form')!.dispatchEvent(new Event('submit', { cancelable: true }));
    const request = http.expectOne({ method: 'PUT', url: '/api/v1/nodes/5/readiness' });
    expect(request.request.body).toEqual({ readiness: 65 });
    request.flush(aNode({ id: 5, readiness: 65 }));
    await fixture.whenStable();

    expect(page.textContent).toContain('65%');
  });

  it('shows where a linked node gets its readiness, with no way to type one', async () => {
    await load(aNode({ id: 5, readiness: 57, linkedTree: { id: 9, title: 'Collections in Depth' } }), []);

    expect(page.textContent).toContain('57%, the average of the nodes in the linked tree Collections in Depth');
    expect(link('Collections in Depth').getAttribute('href')).toBe('/trees/9');
    expect(page.querySelector('app-readiness-editor')).toBeNull();
    expect(page.textContent).toContain('Not placed in any tree yet');
  });

  function link(label: string): HTMLAnchorElement {
    return Array.from(page.querySelectorAll('a')).find((a) => a.textContent?.trim() === label) as HTMLAnchorElement;
  }
});
