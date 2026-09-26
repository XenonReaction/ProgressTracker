import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aLesson } from '../core/test-data';
import { LessonView } from './lesson-view';

describe('LessonView', () => {
  let fixture: ComponentFixture<LessonView>;
  let http: HttpTestingController;
  let page: HTMLElement;

  const lesson = aLesson({
    id: 4,
    title: 'Flexbox',
    summary: 'The basics',
    progress: 30,
    lastReviewedAt: '2026-09-20T10:00:00Z',
    sections: [
      { title: 'Container', body: 'Use `display: flex`.' },
      { title: 'Danger', body: 'Hi <img src=x onerror="alert(1)"> <script>alert(2)</script>' },
    ],
  });

  beforeEach(async () => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(LessonView);
    page = fixture.nativeElement;
    fixture.componentRef.setInput('id', '4');
    await fixture.whenStable();
  });

  afterEach(() => http.verify());

  it('records opening the lesson, then shows it from the response', async () => {
    http.expectOne({ method: 'POST', url: '/api/v1/lessons/4/opens' }).flush(lesson);
    await fixture.whenStable();

    expect(page.querySelector('h1')?.textContent).toBe('Flexbox');
    expect(page.textContent).toContain('The basics');
    expect(Array.from(page.querySelectorAll('section h2')).map((h) => h.textContent)).toEqual([
      'Container',
      'Danger',
    ]);
    expect(page.textContent).toContain('30% (self-reported)');
    expect(page.textContent).toContain('Last opened: Sep 20, 2026');
  });

  it('renders each section as Markdown, stripping anything that could run', async () => {
    http.expectOne({ method: 'POST', url: '/api/v1/lessons/4/opens' }).flush(lesson);
    await fixture.whenStable();

    const [container, danger] = Array.from(page.querySelectorAll('.markdown'));
    expect(container.innerHTML).toContain('<code>display: flex</code>');
    expect(danger.querySelector('script')).toBeNull();
    expect(danger.querySelector('img')?.getAttribute('onerror')).toBeNull();
  });

  it('saves the progress entered', async () => {
    http.expectOne({ method: 'POST', url: '/api/v1/lessons/4/opens' }).flush(lesson);
    await fixture.whenStable();
    const input = page.querySelector('input[type=number]') as HTMLInputElement;
    expect(input.value).toBe('30');

    input.value = '80';
    input.dispatchEvent(new Event('input'));
    page.querySelector('form')!.dispatchEvent(new Event('submit'));

    const request = http.expectOne({ method: 'POST', url: '/api/v1/lessons/4/progress' });
    expect(request.request.body).toEqual({ progress: 80 });
    request.flush({ ...lesson, progress: 80 });
    await fixture.whenStable();
    expect(page.textContent).toContain('80% (self-reported)');
  });
});
