import { HttpErrorResponse } from '@angular/common/http';

import { Problem } from './api.models';

/** Pulls the backend's problem body out of an HTTP error, if it sent one. */
export function problemOf(error: unknown): Problem | null {
  if (error instanceof HttpErrorResponse && error.error && typeof error.error === 'object') {
    return error.error as Problem;
  }
  return null;
}

/** A one-line, human-readable message for any HTTP error. */
export function errorMessage(error: unknown): string {
  const problem = problemOf(error);
  if (problem?.errors?.length) {
    return problem.errors.map((e) => `${e.field} ${e.message}`).join('; ');
  }
  if (problem?.detail) {
    return problem.detail;
  }
  if (error instanceof HttpErrorResponse && error.status === 0) {
    return 'Cannot reach the backend. Is it running on port 8080?';
  }
  return 'Something went wrong';
}
