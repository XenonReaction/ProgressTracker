import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aTree } from '../core/test-data';
import { TreeList } from './tree-list';

describe('TreeList', () => {
  let fixture: ComponentFixture<TreeList>;
  let http: HttpTestingController;
  let page: HTMLElement;

  beforeEach(async () => {
    TestBed.configureTestingModule({ providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()] });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(TreeList);
    page = fixture.nativeElement;
    http.expectOne('/api/v1/trees').flush([
      aTree({
        id: 3,
        title: 'Java Fundamentals',
        category: 'Technology',
        tags: ['java', 'backend'],
        readiness: 64,
        lastReviewedAt: '2026-09-01T10:00:00Z',
      }),
      aTree({ id: 5, title: 'Spring Basics' }),
    ]);
    await fixture.whenStable();
  });

  afterEach(() => {
    http.verify();
    vi.restoreAllMocks();
  });

  it('links to creating a tree, and to viewing and editing each tree', () => {
    expect(page.querySelector('a')?.getAttribute('href')).toBe('/trees/new');
    const [view, edit] = Array.from(page.querySelectorAll('tbody tr')[0].querySelectorAll('a'));
    expect(view.getAttribute('href')).toBe('/trees/3');
    expect(edit.getAttribute('href')).toBe('/trees/3/edit');
    expect(page.querySelector('tbody tr')?.textContent).toContain('java, backend');
  });

  it('shows each tree\'s own readiness and when anything in it was last reviewed', () => {
    const [java, spring] = Array.from(page.querySelectorAll('tbody tr'));
    expect(java.textContent).toContain('64%');
    expect(java.textContent).toContain('Sep 1, 2026');
    expect(spring.textContent).toContain('Never');
  });

  it('deletes after a confirmation that explains what is removed, then reloads', async () => {
    const confirm = vi.spyOn(window, 'confirm').mockReturnValue(true);

    deleteButtons()[0].click();

    expect(confirm.mock.calls[0][0]).toContain('Delete the tree "Java Fundamentals"?');
    expect(confirm.mock.calls[0][0]).toContain('stay in your library');
    http.expectOne({ method: 'DELETE', url: '/api/v1/trees/3' }).flush(null, { status: 204, statusText: 'No Content' });
    http.expectOne('/api/v1/trees').flush([aTree({ id: 5, title: 'Spring Basics' })]);
    await fixture.whenStable();
    expect(page.querySelectorAll('tbody tr').length).toBe(1);
  });

  it('does nothing when the user cancels', () => {
    vi.spyOn(window, 'confirm').mockReturnValue(false);

    deleteButtons()[1].click();

    http.expectNone({ method: 'DELETE' });
  });

  it('shows an error if the delete fails', async () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);

    deleteButtons()[1].click();
    http.expectOne({ method: 'DELETE', url: '/api/v1/trees/5' }).flush(
      { status: 404, title: 'Not Found', detail: 'Tree 5 not found' },
      { status: 404, statusText: 'Not Found' },
    );
    await fixture.whenStable();

    expect(page.querySelector('[role=alert]')?.textContent).toContain('Tree 5 not found');
  });

  function deleteButtons(): HTMLButtonElement[] {
    return Array.from(page.querySelectorAll('tbody button'));
  }
});
