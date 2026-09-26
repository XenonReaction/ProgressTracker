import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aDeckResource, aNode, aTreeResource, aUrlResource } from '../core/test-data';
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

  it('shows the node read-only, with its resources, tags, trees and an Edit link', async () => {
    await load(
      aNode({
        id: 5,
        title: 'Generics',
        description: 'Type parameters',
        tags: ['java', 'types'],
        resources: [aUrlResource('https://dev.java/learn/generics/', 'dev.java')],
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

  it('lists resources by type, trees then decks then links, with their readiness and whether they count', async () => {
    await load(
      aNode({
        id: 5,
        resources: [
          aUrlResource('https://example.com', 'Docs'),
          aTreeResource(9, 'Collections in Depth', true, 'Deep dive', 57),
          aDeckResource(4, 'Flexbox cards', false, 75),
          aTreeResource(3, 'Reading', false, null, 10),
        ],
      }),
    );

    const headings = Array.from(page.querySelectorAll('h3')).map((h) => h.textContent);
    expect(headings).toEqual(['Trees', 'Decks', 'Links']);
    const items = (index: number) =>
      Array.from(page.querySelectorAll('h3 + ul')[index].querySelectorAll('li')).map((li) =>
        li.textContent?.replace(/\s+/g, ' ').trim(),
      );
    expect(items(0)).toEqual([
      'Deep dive (Collections in Depth) · 57% · counts toward readiness',
      'Reading · 10% · for reference',
    ]);
    expect(items(1)).toEqual(['Flexbox cards · 75% · for reference']);
    expect(link('Flexbox cards').getAttribute('href')).toBe('/decks/4');
    expect(link('Docs').getAttribute('href')).toBe('https://example.com');
  });

  it('says when a flashcard beneath it is due for review, and which resource', async () => {
    await load(
      aNode({
        id: 5,
        readiness: 100,
        reviewDue: true,
        resources: [{ ...aDeckResource(4, 'Flexbox cards', true, 100), reviewDue: true }],
      }),
    );

    expect(page.querySelector('p.review-due')?.textContent).toContain('Review due:');
    expect(page.querySelector('p.review-due a')?.getAttribute('href')).toBe('/review');
    expect(page.querySelector('h3 + ul li')?.textContent?.replace(/\s+/g, ' ').trim()).toBe(
      'Flexbox cards · 100% · counts toward readiness · review due',
    );
  });

    it('breaks a derived readiness down into what each counting resource contributes', async () => {
    await load(
      aNode({
        id: 5,
        readiness: 68,
        lastReviewedAt: '2026-09-01T10:00:00Z',
        resources: [aDeckResource(4, 'Flexbox cards', true, 75), aTreeResource(3, 'Article', true, null, 60)],
      }),
      [],
    );

    const breakdown = page.querySelector('.breakdown')?.textContent?.replace(/\s+/g, ' ').replace(/ :/g, ':');
    expect(breakdown).toContain('Deck Flexbox cards: 75%, Tree Article: 60% → 68%');
    expect(page.textContent).toContain('Last reviewed: Sep 1, 2026');
    expect(page.querySelector('app-readiness-editor')).toBeNull();
  });

  it('shows where a linked node gets its readiness, with no way to type one', async () => {
    await load(aNode({ id: 5, readiness: 57, resources: [aTreeResource(9, 'Collections in Depth', true, null, 57)] }), []);

    expect(page.textContent).toContain('The average of the resources that count toward it');
    expect(link('Collections in Depth').getAttribute('href')).toBe('/trees/9');
    expect(page.querySelector('app-readiness-editor')).toBeNull();
    expect(page.textContent).toContain('Last reviewed: never');
    expect(page.textContent).toContain('Not placed in any tree yet');
  });

  function link(label: string): HTMLAnchorElement {
    return Array.from(page.querySelectorAll('a')).find((a) => a.textContent?.trim() === label) as HTMLAnchorElement;
  }
});
