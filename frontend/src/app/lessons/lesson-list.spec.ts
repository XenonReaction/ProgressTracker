import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aLesson } from '../core/test-data';
import { LessonList } from './lesson-list';

describe('LessonList', () => {
  let fixture: ComponentFixture<LessonList>;
  let http: HttpTestingController;
  let page: HTMLElement;

  beforeEach(async () => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(LessonList);
    page = fixture.nativeElement;
    http.expectOne('/api/v1/lessons').flush([
      aLesson({
        id: 3,
        title: 'Flexbox',
        progress: 30,
        lastReviewedAt: '2026-09-20T10:00:00Z',
        sections: [{ title: 'One', body: 'Text' }],
      }),
      aLesson({ id: 5, title: 'Grid' }),
    ]);
    await fixture.whenStable();
  });

  afterEach(() => {
    http.verify();
    vi.restoreAllMocks();
  });

  it('shows each lesson with its sections, progress and when it was last opened', () => {
    const [flexbox, grid] = Array.from(page.querySelectorAll('tbody tr'));
    expect(flexbox.textContent).toContain('30%');
    expect(flexbox.textContent).toContain('Sep 20, 2026');
    expect(flexbox.querySelectorAll('td')[1].textContent).toBe('1');
    expect(grid.textContent).toContain('Never');
    expect(Array.from(grid.querySelectorAll('a')).map((a) => a.getAttribute('href'))).toEqual([
      '/lessons/5',
      '/lessons/5/edit',
    ]);
  });

  it('explains a refused delete and links to the nodes that list the lesson', async () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);

    (page.querySelector('tbody button') as HTMLButtonElement).click();
    http
      .expectOne({ method: 'DELETE', url: '/api/v1/lessons/3' })
      .flush(
        {
          status: 409,
          title: 'Conflict',
          detail: 'Lesson 3 is a resource of 1 node(s)',
          nodes: [{ id: 8, title: 'CSS Flexbox' }],
        },
        { status: 409, statusText: 'Conflict' },
      );
    await fixture.whenStable();

    expect(page.querySelector('[role=alert]')?.textContent).toContain(
      'Lesson 3 is a resource of 1 node(s)',
    );
    expect(page.querySelector('[role=alert] a')?.getAttribute('href')).toBe('/nodes/8');
  });
});
