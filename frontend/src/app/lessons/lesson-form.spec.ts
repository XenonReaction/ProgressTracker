import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';

import { aLesson } from '../core/test-data';
import { LessonForm } from './lesson-form';

describe('LessonForm', () => {
  let fixture: ComponentFixture<LessonForm>;
  let http: HttpTestingController;
  let navigate: ReturnType<typeof vi.spyOn>;
  let page: HTMLElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    navigate = vi.spyOn(TestBed.inject(Router), 'navigateByUrl').mockResolvedValue(true);
    fixture = TestBed.createComponent(LessonForm);
    page = fixture.nativeElement;
  });

  afterEach(() => http.verify());

  it('starts a new lesson with one empty section and saves the sections in order', async () => {
    await fixture.whenStable();
    expect(sections().length).toBe(1);
    type(page.querySelector('input[formControlName=title]')!, ' Flexbox ');
    fillSection(0, 'Axes', '`flex-direction`');
    button('+ Add section').click();
    await fixture.whenStable();
    fillSection(1, 'Container', 'Use `display: flex`.');

    button('Move section 2 up').click();
    await fixture.whenStable();
    expect(sections().map((s) => (s.querySelector('input') as HTMLInputElement).value)).toEqual([
      'Container',
      'Axes',
    ]);
    submit();

    const request = http.expectOne({ method: 'POST', url: '/api/v1/lessons' });
    expect(request.request.body).toEqual({
      title: 'Flexbox',
      summary: null,
      sections: [
        { title: 'Container', body: 'Use `display: flex`.' },
        { title: 'Axes', body: '`flex-direction`' },
      ],
    });
    request.flush(aLesson({ id: 9 }));
    expect(navigate).toHaveBeenCalledWith('/lessons/9');
  });

  it('needs a title and a heading and body for every section', async () => {
    await fixture.whenStable();

    submit();
    await fixture.whenStable();

    http.expectNone({ method: 'POST' });
    expect(page.textContent).toContain('Title is required');
    expect(page.textContent).toContain('A section needs a heading');
  });

  it('loads the lesson to edit with its sections', async () => {
    fixture.componentRef.setInput('id', '4');
    await fixture.whenStable();
    http
      .expectOne('/api/v1/lessons/4')
      .flush(
        aLesson({
          id: 4,
          title: 'Flexbox',
          summary: 'Basics',
          sections: [{ title: 'Container', body: 'Text' }],
        }),
      );
    await fixture.whenStable();

    expect(sections().length).toBe(1);
    button('Remove section').click();
    await fixture.whenStable();
    submit();

    const request = http.expectOne({ method: 'PUT', url: '/api/v1/lessons/4' });
    expect(request.request.body).toEqual({ title: 'Flexbox', summary: 'Basics', sections: [] });
    request.flush(aLesson({ id: 4 }));
  });

  function sections(): HTMLElement[] {
    return Array.from(page.querySelectorAll('.lesson-section'));
  }

  function fillSection(index: number, title: string, body: string): void {
    type(sections()[index].querySelector('input')!, title);
    type(sections()[index].querySelector('textarea')!, body);
  }

  function button(label: string): HTMLButtonElement {
    return Array.from(page.querySelectorAll('button')).find(
      (b) => b.getAttribute('aria-label') === label || b.textContent?.trim() === label,
    ) as HTMLButtonElement;
  }

  function type(field: HTMLInputElement | HTMLTextAreaElement, value: string): void {
    field.value = value;
    field.dispatchEvent(new Event('input'));
  }

  function submit(): void {
    page.querySelector('form')!.dispatchEvent(new Event('submit'));
  }
});
