import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';

import { aMaterial } from '../core/test-data';
import { MaterialForm } from './material-form';

describe('MaterialForm', () => {
  let fixture: ComponentFixture<MaterialForm>;
  let http: HttpTestingController;
  let navigate: ReturnType<typeof vi.spyOn>;
  let page: HTMLElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    navigate = vi.spyOn(TestBed.inject(Router), 'navigateByUrl').mockResolvedValue(true);
    fixture = TestBed.createComponent(MaterialForm);
    page = fixture.nativeElement;
  });

  afterEach(() => http.verify());

  it('creates a material and opens it', async () => {
    await fixture.whenStable();
    type(input('title'), ' Flexbox guide ');
    type(input('url'), 'https://css-tricks.com/flexbox/');

    submit();

    const request = http.expectOne({ method: 'POST', url: '/api/v1/materials' });
    expect(request.request.body).toEqual({
      title: 'Flexbox guide',
      url: 'https://css-tricks.com/flexbox/',
      notes: null,
    });
    request.flush(aMaterial({ id: 9 }));
    expect(navigate).toHaveBeenCalledWith('/materials/9');
  });

  it('needs a title and an http(s) URL', async () => {
    await fixture.whenStable();
    type(input('url'), 'ftp://example.com');

    submit();
    await fixture.whenStable();

    http.expectNone({ method: 'POST' });
    expect(page.textContent).toContain('Title is required');
    expect(page.textContent).toContain('Enter an http(s) URL.');
  });

  it('loads the material to edit and saves its details', async () => {
    fixture.componentRef.setInput('id', '4');
    await fixture.whenStable();
    http
      .expectOne('/api/v1/materials/4')
      .flush(aMaterial({ id: 4, title: 'Guide', url: 'https://example.com', notes: 'Notes' }));
    await fixture.whenStable();
    expect(input('title').value).toBe('Guide');

    type(page.querySelector('textarea')!, '');
    submit();

    const request = http.expectOne({ method: 'PUT', url: '/api/v1/materials/4' });
    expect(request.request.body).toEqual({
      title: 'Guide',
      url: 'https://example.com',
      notes: null,
    });
    request.flush(aMaterial({ id: 4 }));
    expect(navigate).toHaveBeenCalledWith('/materials/4');
  });

  function input(name: string): HTMLInputElement {
    return page.querySelector(`input[formControlName=${name}]`) as HTMLInputElement;
  }

  function type(field: HTMLInputElement | HTMLTextAreaElement, value: string): void {
    field.value = value;
    field.dispatchEvent(new Event('input'));
  }

  function submit(): void {
    page.querySelector('form')!.dispatchEvent(new Event('submit'));
  }
});
