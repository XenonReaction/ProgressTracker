import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';

import { aDeck } from '../core/test-data';
import { DeckForm } from './deck-form';

describe('DeckForm', () => {
  let fixture: ComponentFixture<DeckForm>;
  let http: HttpTestingController;
  let navigate: ReturnType<typeof vi.spyOn>;
  let page: HTMLElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    navigate = vi.spyOn(TestBed.inject(Router), 'navigateByUrl').mockResolvedValue(true);
    fixture = TestBed.createComponent(DeckForm);
    page = fixture.nativeElement;
  });

  afterEach(() => http.verify());

  it('creates a deck and opens it', async () => {
    await fixture.whenStable();
    type(page.querySelector('input')!, '  CSS Flexbox ');

    submit();

    const request = http.expectOne({ method: 'POST', url: '/api/v1/decks' });
    expect(request.request.body).toEqual({ title: 'CSS Flexbox', description: null });
    request.flush(aDeck({ id: 9 }));
    expect(navigate).toHaveBeenCalledWith('/decks/9');
  });

  it('does not submit without a title', async () => {
    await fixture.whenStable();

    submit();
    await fixture.whenStable();

    http.expectNone({ method: 'POST' });
    expect(page.textContent).toContain('Title is required');
  });

  it('loads the deck to edit and saves the changes', async () => {
    fixture.componentRef.setInput('id', '4');
    await fixture.whenStable();
    http.expectOne('/api/v1/decks/4').flush(aDeck({ id: 4, title: 'CSS', description: 'Layout' }));
    await fixture.whenStable();
    expect(page.querySelector('input')!.value).toBe('CSS');

    type(page.querySelector('textarea')!, '');
    submit();

    const request = http.expectOne({ method: 'PUT', url: '/api/v1/decks/4' });
    expect(request.request.body).toEqual({ title: 'CSS', description: null });
    request.flush(aDeck({ id: 4 }));
    expect(navigate).toHaveBeenCalledWith('/decks/4');
  });

  function type(field: HTMLInputElement | HTMLTextAreaElement, value: string): void {
    field.value = value;
    field.dispatchEvent(new Event('input'));
  }

  function submit(): void {
    page.querySelector('form')!.dispatchEvent(new Event('submit'));
  }
});
