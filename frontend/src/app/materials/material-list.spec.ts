import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aMaterial } from '../core/test-data';
import { MaterialList } from './material-list';

describe('MaterialList', () => {
  let fixture: ComponentFixture<MaterialList>;
  let http: HttpTestingController;
  let page: HTMLElement;

  beforeEach(async () => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(MaterialList);
    page = fixture.nativeElement;
    http
      .expectOne('/api/v1/materials')
      .flush([
        aMaterial({
          id: 3,
          title: 'Flexbox guide',
          progress: 60,
          lastReviewedAt: '2026-09-15T10:00:00Z',
        }),
        aMaterial({ id: 5, title: 'Grid video', url: 'https://example.com/grid' }),
      ]);
    await fixture.whenStable();
  });

  afterEach(() => {
    http.verify();
    vi.restoreAllMocks();
  });

  it('shows each material with its progress, when it was last updated and a link to open it', () => {
    const [guide, video] = Array.from(page.querySelectorAll('tbody tr'));
    expect(guide.textContent).toContain('60%');
    expect(guide.textContent).toContain('Sep 15, 2026');
    expect(video.textContent).toContain('Never');
    const links = Array.from(video.querySelectorAll('a')).map((a) => a.getAttribute('href'));
    expect(links).toEqual(['/materials/5', 'https://example.com/grid', '/materials/5/edit']);
  });

  it('explains a refused delete and links to the nodes that list the material', async () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);

    (page.querySelector('tbody button') as HTMLButtonElement).click();
    http
      .expectOne({ method: 'DELETE', url: '/api/v1/materials/3' })
      .flush(
        {
          status: 409,
          title: 'Conflict',
          detail: 'Material 3 is a resource of 1 node(s)',
          nodes: [{ id: 8, title: 'CSS Flexbox' }],
        },
        { status: 409, statusText: 'Conflict' },
      );
    await fixture.whenStable();

    expect(page.querySelector('[role=alert]')?.textContent).toContain(
      'Material 3 is a resource of 1 node(s)',
    );
    expect(page.querySelector('[role=alert] a')?.getAttribute('href')).toBe('/nodes/8');
  });

  it('deletes after a confirmation that says the history goes too, then reloads', async () => {
    const confirm = vi.spyOn(window, 'confirm').mockReturnValue(true);

    (page.querySelectorAll('tbody button')[1] as HTMLButtonElement).click();

    expect(confirm.mock.calls[0][0]).toContain('Its progress history is deleted too');
    http.expectOne({ method: 'DELETE', url: '/api/v1/materials/5' }).flush(null);
    http.expectOne('/api/v1/materials').flush([aMaterial({ id: 3 })]);
    await fixture.whenStable();
    expect(page.querySelectorAll('tbody tr').length).toBe(1);
  });
});
