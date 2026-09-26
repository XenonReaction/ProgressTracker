import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aNode, aTreeResource, aUrlResource } from '../core/test-data';
import { NodeList } from './node-list';

describe('NodeList', () => {
  let fixture: ComponentFixture<NodeList>;
  let http: HttpTestingController;
  let page: HTMLElement;

  beforeEach(async () => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(NodeList);
    page = fixture.nativeElement;
    http.expectOne('/api/v1/nodes').flush([
      aNode({ id: 1, title: 'Generics', readiness: 40 }),
      aNode({ id: 2, title: 'OOP', readiness: 80, resources: [aUrlResource('https://example.com')] }),
      aNode({
        id: 3,
        title: 'Collections',
        readiness: 70,
        resources: [aTreeResource(9, 'Collections in Depth'), aTreeResource(4, 'Reading', false)],
      }),
    ]);
    await fixture.whenStable();
  });

  afterEach(() => {
    http.verify();
    vi.restoreAllMocks();
  });

  it('shows each node with its readiness, linking to its page and its edit form', () => {
    const rows = page.querySelectorAll('tbody tr');
    expect(rows.length).toBe(3);
    expect(rows[1].textContent).toContain('OOP');
    expect(rows[1].textContent).toContain('80%');
    const links = Array.from(rows[1].querySelectorAll('a')).map((a) => a.getAttribute('href'));
    expect(links).toEqual(['/nodes/2', '/nodes/2/edit']);
  });

  it('names the trees a node takes its readiness from, and counts its resources', () => {
    const row = page.querySelectorAll('tbody tr')[2];
    expect(row.textContent?.replace(/\s+/g, ' ')).toContain('70% from Collections in Depth');
    expect(row.textContent).not.toContain('Reading');
    expect(row.querySelectorAll('a')[1].getAttribute('href')).toBe('/trees/9');
    expect(row.querySelectorAll('td')[2].textContent).toBe('2');
  });

  it('deletes after confirmation and reloads the list', async () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);

    deleteButtons()[0].click();
    http.expectOne({ method: 'DELETE', url: '/api/v1/nodes/1' }).flush(null, { status: 204, statusText: 'No Content' });
    http.expectOne('/api/v1/nodes').flush([aNode({ id: 2, title: 'OOP' })]);
    await fixture.whenStable();

    expect(page.querySelectorAll('tbody tr').length).toBe(1);
  });

  it('does nothing when the user cancels', () => {
    vi.spyOn(window, 'confirm').mockReturnValue(false);

    deleteButtons()[0].click();

    http.expectNone({ method: 'DELETE' });
  });

  it('explains a refused delete and links to the trees that use the node', async () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);

    deleteButtons()[1].click();
    http.expectOne({ method: 'DELETE', url: '/api/v1/nodes/2' }).flush(
      {
        status: 409,
        title: 'Conflict',
        detail: 'Node 2 is used in 1 tree(s); remove it from them before deleting it',
        trees: [{ id: 7, title: 'Java Fundamentals' }],
      },
      { status: 409, statusText: 'Conflict' },
    );
    await fixture.whenStable();

    const alert = page.querySelector('[role=alert]');
    expect(alert?.textContent).toContain('is used in 1 tree(s)');
    expect(alert?.querySelector('a')?.textContent).toBe('Java Fundamentals');
    expect(alert?.querySelector('a')?.getAttribute('href')).toBe('/trees/7');
  });

  function deleteButtons(): HTMLButtonElement[] {
    return Array.from(page.querySelectorAll('tbody button'));
  }
});
