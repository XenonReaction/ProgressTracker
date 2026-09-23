import { HttpErrorResponse } from '@angular/common/http';

import { errorMessage, problemOf } from './problem';

function httpError(status: number, body: unknown): HttpErrorResponse {
  return new HttpErrorResponse({ status, error: body });
}

describe('problem helpers', () => {
  it('uses the problem detail', () => {
    expect(errorMessage(httpError(409, { status: 409, title: 'Conflict', detail: 'Would create a cycle' }))).toBe(
      'Would create a cycle',
    );
  });

  it('lists field errors from validation problems', () => {
    const error = httpError(400, {
      status: 400,
      title: 'Bad Request',
      detail: 'Request validation failed',
      errors: [
        { field: 'title', message: 'must not be blank' },
        { field: 'readiness', message: 'must be less than or equal to 100' },
      ],
    });
    expect(errorMessage(error)).toBe('title must not be blank; readiness must be less than or equal to 100');
  });

  it('explains when the backend is unreachable', () => {
    expect(errorMessage(httpError(0, null))).toContain('Cannot reach the backend');
  });

  it('falls back to a generic message', () => {
    expect(errorMessage(new Error('boom'))).toBe('Something went wrong');
    expect(problemOf(new Error('boom'))).toBeNull();
  });

  it('exposes extra problem properties', () => {
    const trees = [{ id: 1, title: 'Java' }];
    expect(problemOf(httpError(409, { status: 409, title: 'Conflict', trees }))?.trees).toEqual(trees);
  });
});
