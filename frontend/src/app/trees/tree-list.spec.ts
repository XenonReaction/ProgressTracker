import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { TreeList } from './tree-list';

describe('TreeList', () => {
  it('links each tree to its view', async () => {
    TestBed.configureTestingModule({ providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()] });
    const http = TestBed.inject(HttpTestingController);
    const fixture = TestBed.createComponent(TreeList);

    http.expectOne('/api/v1/trees').flush([
      { id: 3, title: 'Java Fundamentals', description: null, category: 'Technology', tags: ['java', 'backend'], createdAt: '', updatedAt: '' },
    ]);
    await fixture.whenStable();

    const row = (fixture.nativeElement as HTMLElement).querySelector('tbody tr')!;
    expect(row.querySelector('a')?.getAttribute('href')).toBe('/trees/3');
    expect(row.textContent).toContain('java, backend');
    http.verify();
  });
});
