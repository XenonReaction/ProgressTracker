import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { aDeckResource, aTreeResource, aUrlResource } from '../core/test-data';
import { ReadinessBreakdown } from './readiness-breakdown';

describe('ReadinessBreakdown', () => {
  let fixture: ComponentFixture<ReadinessBreakdown>;
  let page: HTMLElement;

  beforeEach(async () => {
    TestBed.configureTestingModule({ providers: [provideRouter([])] });
    fixture = TestBed.createComponent(ReadinessBreakdown);
    page = fixture.nativeElement;
    fixture.componentRef.setInput('resources', [
      aDeckResource(4, 'Flexbox cards', true, 75),
      aUrlResource('https://example.com'),
      aTreeResource(3, 'Article', true, null, 60),
      aTreeResource(5, 'Reading', false, null, 10),
    ]);
    fixture.componentRef.setInput('readiness', 68);
    await fixture.whenStable();
  });

  it('shows what each counting resource contributes and the result', () => {
    expect(text()).toBe('Deck Flexbox cards: 75%, Tree Article: 60% → 68%');
    const links = Array.from(page.querySelectorAll('a')).map((a) => a.getAttribute('href'));
    expect(links).toEqual(['/decks/4', '/trees/3']);
    expect(page.textContent).toContain('Last reviewed: never');
  });

  it('shows when anything beneath was last reviewed, and can leave out the links', async () => {
    fixture.componentRef.setInput('lastReviewedAt', '2026-09-01T10:00:00Z');
    fixture.componentRef.setInput('links', false);
    await fixture.whenStable();

    expect(page.textContent).toContain('Last reviewed: Sep 1, 2026');
    expect(page.querySelectorAll('a').length).toBe(0);
    expect(text()).toBe('Deck Flexbox cards: 75%, Tree Article: 60% → 68%');
  });

  function text(): string {
    return (page.querySelector('.breakdown')?.textContent ?? '')
      .replace(/\s+/g, ' ')
      .replace(/ :/g, ':')
      .trim();
  }
});
