import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export const MAX_TAG_LENGTH = 50;

/** "java, backend,, Java " -> ["java", "backend", "Java"]: trimmed, blanks and exact duplicates dropped. */
export function parseTags(text: string): string[] {
  return [...new Set(text.split(',').map((tag) => tag.trim()).filter((tag) => tag.length > 0))];
}

/** Rejects comma-separated tag text containing a tag longer than the backend allows. */
export const tagsValidator: ValidatorFn = (control: AbstractControl<string>): ValidationErrors | null =>
  parseTags(control.value ?? '').some((tag) => tag.length > MAX_TAG_LENGTH) ? { tagTooLong: true } : null;
