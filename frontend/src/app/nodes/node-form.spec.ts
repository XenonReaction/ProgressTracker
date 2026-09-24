import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';

import { aNode, aTree } from '../core/test-data';
import { NodeForm } from './node-form';

describe('NodeForm', () => {
  let fixture: ComponentFixture<NodeForm>;
  let http: HttpTestingController;
  let navigate: ReturnType<typeof vi.spyOn>;
  let page: HTMLElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    navigate = vi.spyOn(TestBed.inject(Router), 'navigateByUrl').mockResolvedValue(true);
    fixture = TestBed.createComponent(NodeForm);
    page = fixture.nativeElement;
  });

  afterEach(() => http.verify());

  describe('creating', () => {
    beforeEach(async () => {
      await fixture.whenStable();
    });

    it('posts the entered values and opens the new node', async () => {
      type(input('title'), '  Generics  ');
      type(page.querySelector('textarea')!, 'Type parameters');
      type(input('tags'), 'java, types,');
      await addLink('https://dev.java/learn/generics/', 'dev.java');

      submit();

      const request = http.expectOne({ method: 'POST', url: '/api/v1/nodes' });
      expect(request.request.body).toEqual({
        title: 'Generics',
        description: 'Type parameters',
        readiness: 0,
        links: [{ url: 'https://dev.java/learn/generics/', label: 'dev.java' }],
        linkedTreeId: null,
        tags: ['java', 'types'],
      });
      request.flush(aNode({ id: 8 }));
      expect(navigate).toHaveBeenCalledWith('/nodes/8');
    });

    it('does not ask for readiness, which is set on the node page', () => {
      expect(input('readiness', 'number')).toBeNull();
      expect(page.textContent).toContain('It starts at 0%');
    });

    it('does not submit an invalid form', async () => {
      type(input('tags'), 'x'.repeat(51));
      submit();
      await fixture.whenStable();

      http.expectNone('/api/v1/nodes');
      expect(page.textContent).toContain('Title is required');
      expect(page.textContent).toContain('Each tag can be at most 50 characters');
    });

    it('rejects links that are not http(s) URLs', async () => {
      type(input('title'), 'Generics');
      await addLink('not a url', '');
      submit();
      await fixture.whenStable();

      http.expectNone('/api/v1/nodes');
      expect(page.textContent).toContain('Enter an http(s) URL');
    });

    it('links the node to a chosen tree instead of a hand-entered value', async () => {
      type(input('title'), 'Collections');
      await chooseSource('linked_tree');
      http.expectOne({ method: 'GET', url: '/api/v1/trees' }).flush([aTree({ id: 3, title: 'Collections in Depth' })]);
      await fixture.whenStable();

      chooseTree(1);
      submit();

      const request = http.expectOne({ method: 'POST', url: '/api/v1/nodes' });
      expect(request.request.body.linkedTreeId).toBe(3);
      request.flush(aNode());
    });

    it('requires a tree when the linked source is chosen', async () => {
      type(input('title'), 'Collections');
      await chooseSource('linked_tree');
      http.expectOne('/api/v1/trees').flush([aTree({ id: 3 })]);
      submit();
      await fixture.whenStable();

      http.expectNone('/api/v1/nodes');
      expect(page.textContent).toContain("Choose the tree this node's readiness comes from");
    });

    it('shows validation problems returned by the backend', async () => {
      type(input('title'), 'Generics');
      submit();
      http.expectOne('/api/v1/nodes').flush(
        { status: 400, title: 'Bad Request', errors: [{ field: 'title', message: 'size must be between 0 and 200' }] },
        { status: 400, statusText: 'Bad Request' },
      );
      await fixture.whenStable();

      expect(page.querySelector('[role=alert]')?.textContent).toContain('title size must be between 0 and 200');
      expect(navigate).not.toHaveBeenCalled();
    });
  });

  describe('editing', () => {
    beforeEach(async () => {
      fixture.componentRef.setInput('id', '5');
      fixture.componentRef.setInput('returnTo', '/trees/2');
      await fixture.whenStable();
      http.expectOne({ method: 'GET', url: '/api/v1/nodes/5' }).flush(
        aNode({
          id: 5,
          title: 'OOP',
          readiness: 60,
          manualReadiness: 60,
          tags: ['java'],
          links: [{ url: 'https://example.com', label: 'Docs' }],
        }),
      );
      await fixture.whenStable();
    });

    it('loads the existing node into the form', () => {
      expect(input('title').value).toBe('OOP');
      expect(input('tags').value).toBe('java');
      expect(page.textContent).toContain("You enter this node's readiness yourself (60%), on its page");
      expect((page.querySelector('input[formControlName=url]') as HTMLInputElement).value).toBe('https://example.com');
    });

    it('puts the updated node, keeping its readiness, and goes back to where the user came from', () => {
      type(input('title'), 'Object-oriented programming');
      submit();

      const request = http.expectOne({ method: 'PUT', url: '/api/v1/nodes/5' });
      expect(request.request.body.title).toBe('Object-oriented programming');
      expect(request.request.body.readiness).toBe(60);
      expect(request.request.body.tags).toEqual(['java']);
      expect(request.request.body.links).toEqual([{ url: 'https://example.com', label: 'Docs' }]);
      request.flush(aNode());
      expect(navigate).toHaveBeenCalledWith('/trees/2');
    });
  });

  describe('editing a linked node', () => {
    beforeEach(async () => {
      fixture.componentRef.setInput('id', '5');
      await fixture.whenStable();
      http.expectOne('/api/v1/nodes/5').flush(
        aNode({
          id: 5,
          title: 'Collections',
          readiness: 57,
          manualReadiness: 25,
          readinessSourceType: 'linked_tree',
          linkedTree: { id: 3, title: 'Collections in Depth' },
        }),
      );
      http.expectOne('/api/v1/trees').flush([aTree({ id: 2, title: 'Java' }), aTree({ id: 3, title: 'Collections in Depth' })]);
      await fixture.whenStable();
    });

    it('shows the linked tree with its current readiness and keeps the hand-entered value', () => {
      expect(select().selectedOptions[0].textContent).toBe('Collections in Depth');
      expect(page.textContent).toContain("It's 57% now.");
      expect(page.textContent).toContain('Your hand-entered value (25%) is kept');
    });

    it('unlinking sends the hand-entered value and no tree', async () => {
      await chooseSource('manual');
      expect(page.textContent).toContain("You enter this node's readiness yourself (25%)");
      submit();

      const request = http.expectOne({ method: 'PUT', url: '/api/v1/nodes/5' });
      expect(request.request.body.linkedTreeId).toBeNull();
      expect(request.request.body.readiness).toBe(25);
      request.flush(aNode());
    });
  });

  it('ignores a returnTo that points outside the app', () => {
    fixture.componentRef.setInput('returnTo', '//evil.example.com');
    expect((fixture.componentInstance as unknown as { cancelUrl(): string }).cancelUrl()).toBe('/nodes');
  });

  function input(name: string, type?: string): HTMLInputElement {
    const selector = `input[formControlName=${name}]` + (type ? `[type=${type}]` : '');
    return page.querySelector(selector) as HTMLInputElement;
  }

  function type(element: HTMLInputElement | HTMLTextAreaElement, value: string): void {
    element.value = value;
    element.dispatchEvent(new Event('input'));
  }

  async function addLink(url: string, label: string): Promise<void> {
    (Array.from(page.querySelectorAll('button')).find((b) => b.textContent?.includes('Add link')) as HTMLButtonElement).click();
    await fixture.whenStable();
    const links = page.querySelectorAll('fieldset div');
    const row = links[links.length - 1];
    type(row.querySelector('input[formControlName=url]') as HTMLInputElement, url);
    type(row.querySelector('input[formControlName=label]') as HTMLInputElement, label);
  }

  async function chooseSource(source: 'manual' | 'linked_tree'): Promise<void> {
    (page.querySelector(`input[type=radio][value=${source}]`) as HTMLInputElement).click();
    await fixture.whenStable();
  }

  function select(): HTMLSelectElement {
    return page.querySelector('select[formControlName=linkedTreeId]') as HTMLSelectElement;
  }

  function chooseTree(index: number): void {
    select().selectedIndex = index;
    select().dispatchEvent(new Event('change'));
  }

  function submit(): void {
    page.querySelector('form')!.dispatchEvent(new Event('submit'));
  }
});
