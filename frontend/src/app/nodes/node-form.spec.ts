import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';

import { aNode } from '../core/test-data';
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

    it('posts the entered values and returns to the library', async () => {
      type(input('title'), '  Generics  ');
      type(page.querySelector('textarea')!, 'Type parameters');
      type(input('readiness', 'number'), '45');
      await addLink('https://dev.java/learn/generics/', 'dev.java');

      submit();

      const request = http.expectOne({ method: 'POST', url: '/api/v1/nodes' });
      expect(request.request.body).toEqual({
        title: 'Generics',
        description: 'Type parameters',
        readiness: 45,
        links: [{ url: 'https://dev.java/learn/generics/', label: 'dev.java' }],
      });
      request.flush(aNode());
      expect(navigate).toHaveBeenCalledWith('/nodes');
    });

    it('does not submit an invalid form', async () => {
      type(input('readiness', 'number'), '150');
      submit();
      await fixture.whenStable();

      http.expectNone('/api/v1/nodes');
      expect(page.textContent).toContain('Title is required');
      expect(page.textContent).toContain('Readiness must be a whole number from 0 to 100');
    });

    it('rejects links that are not http(s) URLs', async () => {
      type(input('title'), 'Generics');
      await addLink('not a url', '');
      submit();
      await fixture.whenStable();

      http.expectNone('/api/v1/nodes');
      expect(page.textContent).toContain('Enter an http(s) URL');
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
        aNode({ id: 5, title: 'OOP', readiness: 60, links: [{ url: 'https://example.com', label: 'Docs' }] }),
      );
      await fixture.whenStable();
    });

    it('loads the existing node into the form', () => {
      expect(input('title').value).toBe('OOP');
      expect(input('readiness', 'number').value).toBe('60');
      expect((page.querySelector('input[formControlName=url]') as HTMLInputElement).value).toBe('https://example.com');
    });

    it('puts the updated node and goes back to where the user came from', () => {
      type(input('readiness', 'number'), '85');
      submit();

      const request = http.expectOne({ method: 'PUT', url: '/api/v1/nodes/5' });
      expect(request.request.body.readiness).toBe(85);
      expect(request.request.body.links).toEqual([{ url: 'https://example.com', label: 'Docs' }]);
      request.flush(aNode());
      expect(navigate).toHaveBeenCalledWith('/trees/2');
    });
  });

  it('ignores a returnTo that points outside the app', () => {
    fixture.componentRef.setInput('returnTo', '//evil.example.com');
    expect((fixture.componentInstance as unknown as { safeReturnUrl(): string }).safeReturnUrl()).toBe('/nodes');
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

  function submit(): void {
    page.querySelector('form')!.dispatchEvent(new Event('submit'));
  }
});
