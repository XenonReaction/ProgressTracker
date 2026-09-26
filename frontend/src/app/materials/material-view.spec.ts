import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';

import { Material, ProgressUpdate } from '../core/api.models';
import { aMaterial } from '../core/test-data';
import { MaterialView } from './material-view';

describe('MaterialView', () => {
  let fixture: ComponentFixture<MaterialView>;
  let http: HttpTestingController;
  let page: HTMLElement;

  const guide = aMaterial({
    id: 4,
    title: 'Flexbox guide',
    url: 'https://css-tricks.com/flexbox/',
    notes: 'Container first',
    progress: 60,
    lastReviewedAt: '2026-09-15T10:00:00Z',
    updateCount: 2,
  });
  const history: ProgressUpdate[] = [
    { id: 2, progress: 60, note: null, recordedAt: '2026-09-15T10:00:00Z' },
    { id: 1, progress: 40, note: 'Container properties', recordedAt: '2026-09-01T10:00:00Z' },
  ];

  beforeEach(async () => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(MaterialView);
    page = fixture.nativeElement;
    fixture.componentRef.setInput('id', '4');
    await fixture.whenStable();
    await flush(guide, history);
  });

  afterEach(() => {
    http.verify();
    vi.restoreAllMocks();
  });

  it('shows the material, its self-reported progress and every earlier report', () => {
    expect(page.querySelector('h1')?.textContent).toBe('Flexbox guide');
    expect(page.querySelector('a[target=_blank]')?.getAttribute('href')).toBe(
      'https://css-tricks.com/flexbox/',
    );
    expect(page.textContent).toContain('Container first');
    expect(page.textContent).toContain('60% (self-reported)');
    const rows = Array.from(page.querySelectorAll('tbody tr')).map((row) => row.textContent);
    expect(rows[0]).toContain('60%');
    expect(rows[1]).toContain('40%');
    expect(rows[1]).toContain('Container properties');
  });

  it('records new progress, starting from the last value, and reloads', async () => {
    const progress = page.querySelector('input[type=number]') as HTMLInputElement;
    expect(progress.value).toBe('60');
    type(progress, '75');
    type(page.querySelector('form textarea') as HTMLTextAreaElement, ' The items ');
    page.querySelector('form')!.dispatchEvent(new Event('submit'));

    const request = http.expectOne({ method: 'POST', url: '/api/v1/materials/4/progress' });
    expect(request.request.body).toEqual({ progress: 75, note: 'The items' });
    request.flush({ ...guide, progress: 75 });
    await flush({ ...guide, progress: 75, updateCount: 3 }, [
      { id: 3, progress: 75, note: 'The items', recordedAt: '2026-09-20T10:00:00Z' },
      ...history,
    ]);

    expect(page.textContent).toContain('75% (self-reported)');
    expect(page.querySelectorAll('tbody tr').length).toBe(3);
  });

  it('refuses progress outside 0 to 100', async () => {
    type(page.querySelector('input[type=number]') as HTMLInputElement, '120');
    page.querySelector('form')!.dispatchEvent(new Event('submit'));
    await fixture.whenStable();

    http.expectNone({ method: 'POST' });
    expect(page.textContent).toContain('Progress must be a whole number from 0 to 100.');
  });

  it('deletes the material and goes back to the list', () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);
    const navigate = vi.spyOn(TestBed.inject(Router), 'navigateByUrl').mockResolvedValue(true);

    (
      Array.from(page.querySelectorAll('button')).find(
        (b) => b.textContent === 'Delete material',
      ) as HTMLButtonElement
    ).click();

    http.expectOne({ method: 'DELETE', url: '/api/v1/materials/4' }).flush(null);
    expect(navigate).toHaveBeenCalledWith('/materials');
  });

  async function flush(material: Material, updates: ProgressUpdate[]): Promise<void> {
    http.expectOne(`/api/v1/materials/${material.id}`).flush(material);
    http.expectOne(`/api/v1/materials/${material.id}/progress`).flush(updates);
    await fixture.whenStable();
  }

  function type(field: HTMLInputElement | HTMLTextAreaElement, value: string): void {
    field.value = value;
    field.dispatchEvent(new Event('input'));
  }
});
