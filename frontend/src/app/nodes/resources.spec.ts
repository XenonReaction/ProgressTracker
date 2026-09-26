import { aTreeResource, aUrlResource } from '../core/test-data';
import {
  countingTrees,
  hasCountingResource,
  resourceTitle,
  titleList,
  toRequest,
} from './resources';

describe('resources', () => {
  it('names a resource by its label, or else its target', () => {
    expect(resourceTitle(aTreeResource(3, 'CSS', true, 'Styling'))).toBe('Styling');
    expect(resourceTitle(aTreeResource(3, 'CSS'))).toBe('CSS');
    expect(resourceTitle(aUrlResource('https://example.com'))).toBe('https://example.com');
  });

  it('finds the trees that count', () => {
    const resources = [
      aUrlResource('https://example.com'),
      aTreeResource(3, 'CSS'),
      aTreeResource(4, 'Reading', false),
    ];

    expect(countingTrees(resources)).toEqual([{ id: 3, title: 'CSS' }]);
    expect(hasCountingResource(resources)).toBe(true);
    expect(hasCountingResource([aTreeResource(4, 'Reading', false)])).toBe(false);
  });

  it('lists titles in a sentence', () => {
    expect(titleList([])).toBe('');
    expect(titleList([{ id: 1, title: 'A' }])).toBe('A');
    expect(
      titleList([
        { id: 1, title: 'A' },
        { id: 2, title: 'B' },
      ]),
    ).toBe('A and B');
    expect(
      titleList([
        { id: 1, title: 'A' },
        { id: 2, title: 'B' },
        { id: 3, title: 'C' },
      ]),
    ).toBe('A, B and C');
  });

  it('turns a resource back into its request', () => {
    expect(toRequest(aTreeResource(3, 'CSS', false, 'Styling'))).toEqual({
      type: 'tree',
      url: null,
      treeId: 3,
      label: 'Styling',
      counts: false,
    });
  });
});
