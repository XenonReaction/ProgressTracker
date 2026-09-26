import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';

import {
  aDeck,
  aLesson,
  aMaterial,
  aNode,
  aQuestionSet,
  aTree,
  aTreeResource,
  aUrlResource,
} from '../core/test-data';
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
        resources: [
          {
            type: 'url',
            url: 'https://dev.java/learn/generics/',
            label: 'dev.java',
            counts: false,
          },
        ],
        tags: ['java', 'types'],
      });
      request.flush(aNode({ id: 8 }));
      expect(navigate).toHaveBeenCalledWith('/nodes/8');
    });

    it('does not ask for readiness, which is set on the node page', () => {
      expect(input('readiness', 'number')).toBeNull();
      expect(page.textContent).toContain('it starts at 0%');
    });

    it('does not submit an invalid form', async () => {
      type(input('tags'), 'x'.repeat(51));
      submit();
      submit();
      await fixture.whenStable();

      http.expectNone('/api/v1/nodes');
      expect(page.textContent).toContain('Title is required');
      expect(page.textContent).toContain('Each tag can be at most 50 characters');
    });

    it('rejects links that are not http(s) URLs', async () => {
      type(input('title'), 'Generics');
      await addLink('ftp://example.com', '');
      submit();
      await fixture.whenStable();

      http.expectNone('/api/v1/nodes');
      expect(page.textContent).toContain('Enter an http(s) URL');
    });

    it('adds a tree, which counts toward readiness unless unticked', async () => {
      type(input('title'), 'Collections');
      await addTree();
      http
        .expectOne({ method: 'GET', url: '/api/v1/trees' })
        .flush([
          aTree({ id: 3, title: 'Collections in Depth' }),
          aTree({ id: 4, title: 'Reading' }),
        ]);
      await fixture.whenStable();
      chooseTree(0, 1);
      await fixture.whenStable();
      expect(page.textContent).toContain(
        'Readiness will be the average of the resources that count.',
      );

      await addTree();
      chooseTree(1, 2);
      counts(1).click();
      type(labelInput(1), ' Further reading ');
      await fixture.whenStable();
      submit();

      const request = http.expectOne({ method: 'POST', url: '/api/v1/nodes' });
      expect(request.request.body.resources).toEqual([
        { type: 'tree', treeId: 3, label: null, counts: true },
        { type: 'tree', treeId: 4, label: 'Further reading', counts: false },
      ]);
      request.flush(aNode());
    });

    it('adds a flashcard deck, which counts unless unticked, and requires one to be chosen', async () => {
      type(input('title'), 'CSS Flexbox');
      button('+ Add deck').click();
      await fixture.whenStable();
      http
        .expectOne({ method: 'GET', url: '/api/v1/decks' })
        .flush([aDeck({ id: 4, title: 'Flexbox cards' })]);
      await fixture.whenStable();
      expect(counts(0).checked).toBe(true);
      submit();
      await fixture.whenStable();
      http.expectNone({ method: 'POST', url: '/api/v1/nodes' });
      expect(page.textContent).toContain('Choose a deck.');

      const select = rows()[0].querySelector('select') as HTMLSelectElement;
      select.selectedIndex = 1;
      select.dispatchEvent(new Event('change'));
      await fixture.whenStable();
      expect(page.textContent).toContain(
        'Readiness will be the average of the resources that count.',
      );
      submit();

      const request = http.expectOne({ method: 'POST', url: '/api/v1/nodes' });
      expect(request.request.body.resources).toEqual([
        { type: 'deck', deckId: 4, label: null, counts: true },
      ]);
      request.flush(aNode());
    });

    it('adds a material, which counts unless unticked', async () => {
      type(input('title'), 'CSS Flexbox');
      button('+ Add material').click();
      await fixture.whenStable();
      http
        .expectOne({ method: 'GET', url: '/api/v1/materials' })
        .flush([aMaterial({ id: 6, title: 'Flexbox guide' })]);
      await fixture.whenStable();
      expect(counts(0).checked).toBe(true);
      submit();
      await fixture.whenStable();
      http.expectNone({ method: 'POST', url: '/api/v1/nodes' });
      expect(page.textContent).toContain('Choose a material.');

      const select = rows()[0].querySelector('select') as HTMLSelectElement;
      select.selectedIndex = 1;
      select.dispatchEvent(new Event('change'));
      await fixture.whenStable();
      submit();

      const request = http.expectOne({ method: 'POST', url: '/api/v1/nodes' });
      expect(request.request.body.resources).toEqual([
        { type: 'material', materialId: 6, label: null, counts: true },
      ]);
      request.flush(aNode());
    });

    it('adds a lesson, which counts unless unticked', async () => {
      type(input('title'), 'CSS Flexbox');
      button('+ Add lesson').click();
      await fixture.whenStable();
      http
        .expectOne({ method: 'GET', url: '/api/v1/lessons' })
        .flush([aLesson({ id: 7, title: 'Flexbox lesson' })]);
      await fixture.whenStable();
      expect(counts(0).checked).toBe(true);
      submit();
      await fixture.whenStable();
      http.expectNone({ method: 'POST', url: '/api/v1/nodes' });
      expect(page.textContent).toContain('Choose a lesson.');

      const select = rows()[0].querySelector('select') as HTMLSelectElement;
      select.selectedIndex = 1;
      select.dispatchEvent(new Event('change'));
      await fixture.whenStable();
      submit();

      const request = http.expectOne({ method: 'POST', url: '/api/v1/nodes' });
      expect(request.request.body.resources).toEqual([
        { type: 'lesson', lessonId: 7, label: null, counts: true },
      ]);
      request.flush(aNode());
    });

    it('adds a coding question set, which counts unless unticked', async () => {
      type(input('title'), 'CSS Flexbox');
      button('+ Add question set').click();
      await fixture.whenStable();
      http
        .expectOne({ method: 'GET', url: '/api/v1/question-sets' })
        .flush([aQuestionSet({ id: 8, title: 'Flexbox exercises' })]);
      await fixture.whenStable();
      expect(counts(0).checked).toBe(true);
      submit();
      await fixture.whenStable();
      http.expectNone({ method: 'POST', url: '/api/v1/nodes' });
      expect(page.textContent).toContain('Choose a question set.');

      const select = rows()[0].querySelector('select') as HTMLSelectElement;
      select.selectedIndex = 1;
      select.dispatchEvent(new Event('change'));
      await fixture.whenStable();
      submit();

      const request = http.expectOne({ method: 'POST', url: '/api/v1/nodes' });
      expect(request.request.body.resources).toEqual([
        { type: 'question_set', questionSetId: 8, label: null, counts: true },
      ]);
      request.flush(aNode());
    });

    it('requires a tree to be chosen', async () => {
      type(input('title'), 'Collections');
      await addTree();
      http.expectOne('/api/v1/trees').flush([aTree({ id: 3 })]);
      submit();
      await fixture.whenStable();

      http.expectNone('/api/v1/nodes');
      expect(page.textContent).toContain('Choose a tree.');
    });

    it('reorders and removes resources', async () => {
      type(input('title'), 'Generics');
      await addLink('https://first.example', 'First');
      await addLink('https://second.example', 'Second');
      await addLink('https://third.example', 'Third');

      button('Move resource 3 up').click();
      await fixture.whenStable();
      expect(rows().map((row) => labelInputIn(row).value)).toEqual(['First', 'Third', 'Second']);
      expect(
        (rows()[1].querySelector('input[formControlName=url]') as HTMLInputElement).value,
      ).toBe('https://third.example');
      expect(button('Move resource 1 up').disabled).toBe(true);
      expect(button('Move resource 3 down').disabled).toBe(true);
      (rows()[0].querySelector('button:last-of-type') as HTMLButtonElement).click();
      await fixture.whenStable();
      expect(rows().map((row) => labelInputIn(row).value)).toEqual(['Third', 'Second']);
      submit();

      const request = http.expectOne({ method: 'POST', url: '/api/v1/nodes' });
      expect(request.request.body.resources.map((r: { label: string }) => r.label)).toEqual([
        'Third',
        'Second',
      ]);
      request.flush(aNode());
    });

    it('shows validation problems returned by the backend', async () => {
      type(input('title'), 'Generics');
      submit();
      http.expectOne('/api/v1/nodes').flush(
        {
          status: 400,
          title: 'Bad Request',
          errors: [{ field: 'title', message: 'size must be between 0 and 200' }],
        },
        { status: 400, statusText: 'Bad Request' },
      );
      await fixture.whenStable();

      expect(page.querySelector('[role=alert]')?.textContent).toContain(
        'title size must be between 0 and 200',
      );
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
          resources: [aUrlResource('https://example.com', 'Docs')],
        }),
      );
      await fixture.whenStable();
    });

    it('loads the existing node into the form', () => {
      expect(input('title').value).toBe('OOP');
      expect(input('tags').value).toBe('java');
      expect(page.textContent).toContain('so you enter it yourself (60%)');
      expect((page.querySelector('input[formControlName=url]') as HTMLInputElement).value).toBe(
        'https://example.com',
      );
    });

    it('puts the updated node, keeping its readiness, and goes back to where the user came from', () => {
      type(input('title'), 'Object-oriented programming');
      submit();

      const request = http.expectOne({ method: 'PUT', url: '/api/v1/nodes/5' });
      expect(request.request.body.title).toBe('Object-oriented programming');
      expect(request.request.body.readiness).toBe(60);
      expect(request.request.body.tags).toEqual(['java']);
      expect(request.request.body.resources).toEqual([
        { type: 'url', url: 'https://example.com', label: 'Docs', counts: false },
      ]);
      request.flush(aNode());
      expect(navigate).toHaveBeenCalledWith('/trees/2');
    });
  });

  describe('editing a node that takes its readiness from a tree', () => {
    beforeEach(async () => {
      fixture.componentRef.setInput('id', '5');
      await fixture.whenStable();
      http.expectOne('/api/v1/nodes/5').flush(
        aNode({
          id: 5,
          title: 'Collections',
          readiness: 57,
          manualReadiness: 25,
          resources: [aTreeResource(3, 'Collections in Depth', true, 'Deep dive')],
        }),
      );
      http
        .expectOne('/api/v1/trees')
        .flush([aTree({ id: 2, title: 'Java' }), aTree({ id: 3, title: 'Collections in Depth' })]);
      await fixture.whenStable();
    });

    it('shows the tree with its current readiness and keeps the hand-entered value', () => {
      expect(treeSelect(0).selectedOptions[0].textContent).toBe('Collections in Depth');
      expect(labelInput(0).value).toBe('Deep dive');
      expect(counts(0).checked).toBe(true);
      expect(page.textContent).toContain("It's 57% now.");
      expect(page.textContent).toContain('Your hand-entered value (25%) is kept');
    });

    it('stops showing the saved readiness once the trees that count change', async () => {
      chooseTree(0, 1);
      await fixture.whenStable();

      expect(page.textContent).not.toContain("It's 57% now.");
    });

    it('unticking the tree goes back to the hand-entered value', async () => {
      counts(0).click();
      await fixture.whenStable();
      expect(page.textContent).toContain('so you enter it yourself (25%)');
      submit();

      const request = http.expectOne({ method: 'PUT', url: '/api/v1/nodes/5' });
      expect(request.request.body.resources).toEqual([
        { type: 'tree', treeId: 3, label: 'Deep dive', counts: false },
      ]);
      expect(request.request.body.readiness).toBe(25);
      request.flush(aNode());
    });
  });

  it('ignores a returnTo that points outside the app', () => {
    fixture.componentRef.setInput('returnTo', '//evil.example.com');
    expect((fixture.componentInstance as unknown as { cancelUrl(): string }).cancelUrl()).toBe(
      '/nodes',
    );
  });

  function input(name: string, type?: string): HTMLInputElement {
    const selector = `input[formControlName=${name}]` + (type ? `[type=${type}]` : '');
    return page.querySelector(selector) as HTMLInputElement;
  }

  function type(element: HTMLInputElement | HTMLTextAreaElement, value: string): void {
    element.value = value;
    element.dispatchEvent(new Event('input'));
  }

  function button(label: string): HTMLButtonElement {
    return Array.from(page.querySelectorAll('button')).find(
      (b) => b.getAttribute('aria-label') === label || b.textContent?.trim() === label,
    ) as HTMLButtonElement;
  }

  function rows(): HTMLElement[] {
    return Array.from(page.querySelectorAll('fieldset .resource'));
  }

  async function addLink(url: string, label: string): Promise<void> {
    button('+ Add link').click();
    await fixture.whenStable();
    const row = rows().at(-1)!;
    type(row.querySelector('input[formControlName=url]') as HTMLInputElement, url);
    type(row.querySelector('input[formControlName=label]') as HTMLInputElement, label);
  }

  async function addTree(): Promise<void> {
    button('+ Add tree').click();
    await fixture.whenStable();
  }

  function treeSelect(row: number): HTMLSelectElement {
    return rows()[row].querySelector('select') as HTMLSelectElement;
  }

  function chooseTree(row: number, index: number): void {
    treeSelect(row).selectedIndex = index;
    treeSelect(row).dispatchEvent(new Event('change'));
  }

  function counts(row: number): HTMLInputElement {
    return rows()[row].querySelector('input[type=checkbox]') as HTMLInputElement;
  }

  function labelInput(row: number): HTMLInputElement {
    return labelInputIn(rows()[row]);
  }

  function labelInputIn(row: HTMLElement): HTMLInputElement {
    return row.querySelector('input[formControlName=label]') as HTMLInputElement;
  }

  function submit(): void {
    page.querySelector('form')!.dispatchEvent(new Event('submit'));
  }
});
