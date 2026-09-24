import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';

import { aTree } from '../core/test-data';
import { TreeForm } from './tree-form';

describe('TreeForm', () => {
  let fixture: ComponentFixture<TreeForm>;
  let http: HttpTestingController;
  let navigate: ReturnType<typeof vi.spyOn>;
  let page: HTMLElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    navigate = vi.spyOn(TestBed.inject(Router), 'navigateByUrl').mockResolvedValue(true);
    fixture = TestBed.createComponent(TreeForm);
    page = fixture.nativeElement;
  });

  afterEach(() => http.verify());

  describe('creating', () => {
    beforeEach(async () => {
      await fixture.whenStable();
    });

    it('posts the entered values and opens the new tree', () => {
      type(input('title'), '  Java Fundamentals ');
      type(page.querySelector('textarea')!, 'Core skills');
      type(input('category'), 'Technology');
      type(input('tags'), 'java, backend, , java');

      submit();

      const request = http.expectOne({ method: 'POST', url: '/api/v1/trees' });
      expect(request.request.body).toEqual({
        title: 'Java Fundamentals',
        description: 'Core skills',
        category: 'Technology',
        tags: ['java', 'backend'],
      });
      request.flush(aTree({ id: 9 }));
      expect(navigate).toHaveBeenCalledWith('/trees/9');
    });

    it('sends blank optional fields as null and no tags as an empty list', () => {
      type(input('title'), 'Java');
      submit();

      const request = http.expectOne({ method: 'POST', url: '/api/v1/trees' });
      expect(request.request.body).toEqual({ title: 'Java', description: null, category: null, tags: [] });
      request.flush(aTree());
    });

    it('does not submit without a title or with an over-long tag', async () => {
      type(input('tags'), 'x'.repeat(51));
      submit();
      await fixture.whenStable();

      http.expectNone('/api/v1/trees');
      expect(page.textContent).toContain('Title is required');
      expect(page.textContent).toContain('Each tag can be at most 50 characters');
    });

    it('shows problems returned by the backend', async () => {
      type(input('title'), 'Java');
      submit();
      http.expectOne('/api/v1/trees').flush(
        { status: 400, title: 'Bad Request', errors: [{ field: 'category', message: 'size must be between 0 and 100' }] },
        { status: 400, statusText: 'Bad Request' },
      );
      await fixture.whenStable();

      expect(page.querySelector('[role=alert]')?.textContent).toContain('category size must be between 0 and 100');
      expect(navigate).not.toHaveBeenCalled();
    });

    it('cancels back to the tree list', () => {
      expect(cancelLink().getAttribute('href')).toBe('/trees');
    });
  });

  describe('editing', () => {
    beforeEach(async () => {
      fixture.componentRef.setInput('id', '4');
      await fixture.whenStable();
      http.expectOne({ method: 'GET', url: '/api/v1/trees/4' }).flush(
        aTree({ id: 4, title: 'Java', description: 'Core', category: 'Technology', tags: ['java', 'backend'] }),
      );
      await fixture.whenStable();
    });

    it('loads the existing tree into the form', () => {
      expect(page.querySelector('h1')?.textContent).toBe('Edit tree');
      expect(input('title').value).toBe('Java');
      expect(input('category').value).toBe('Technology');
      expect(input('tags').value).toBe('java, backend');
    });

    it('puts the changes and returns to the tree', () => {
      type(input('title'), 'Java Deep Dive');
      type(input('tags'), 'java');
      submit();

      const request = http.expectOne({ method: 'PUT', url: '/api/v1/trees/4' });
      expect(request.request.body).toEqual({ title: 'Java Deep Dive', description: 'Core', category: 'Technology', tags: ['java'] });
      request.flush(aTree({ id: 4 }));
      expect(navigate).toHaveBeenCalledWith('/trees/4');
    });

    it('cancels back to the tree', () => {
      expect(cancelLink().getAttribute('href')).toBe('/trees/4');
    });

    it('goes back into edit mode when opened from the tree editor', async () => {
      fixture.componentRef.setInput('resumeEdit', 'true');
      await fixture.whenStable();
      expect(cancelLink().getAttribute('href')).toBe('/trees/4?resumeEdit=true');

      submit();
      http.expectOne({ method: 'PUT', url: '/api/v1/trees/4' }).flush(aTree({ id: 4 }));
      expect(navigate).toHaveBeenCalledWith('/trees/4?resumeEdit=true');
    });
  });

  function input(name: string): HTMLInputElement {
    return page.querySelector(`input[formControlName=${name}]`) as HTMLInputElement;
  }

  function type(element: HTMLInputElement | HTMLTextAreaElement, value: string): void {
    element.value = value;
    element.dispatchEvent(new Event('input'));
  }

  function submit(): void {
    page.querySelector('form')!.dispatchEvent(new Event('submit'));
  }

  function cancelLink(): HTMLAnchorElement {
    return Array.from(page.querySelectorAll('a')).find((a) => a.textContent === 'Cancel') as HTMLAnchorElement;
  }
});
