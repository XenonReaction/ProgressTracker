import { FormControl } from '@angular/forms';

import { parseTags, tagsValidator } from './tags';

describe('tags', () => {
  it('splits on commas, trims, and drops blanks and exact duplicates', () => {
    expect(parseTags(' java, backend,, java ,Java ')).toEqual(['java', 'backend', 'Java']);
  });

  it('parses empty text as no tags', () => {
    expect(parseTags('')).toEqual([]);
    expect(parseTags(' , ')).toEqual([]);
  });

  it('rejects a tag longer than 50 characters', () => {
    expect(tagsValidator(new FormControl(`ok, ${'x'.repeat(51)}`))).toEqual({ tagTooLong: true });
    expect(tagsValidator(new FormControl(`ok, ${'x'.repeat(50)}`))).toBeNull();
  });
});
