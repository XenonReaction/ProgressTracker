import {
  aDeckResource,
  aLessonResource,
  aMaterialResource,
  aQuestionSetResource,
  aTreeResource,
  aUrlResource,
} from '../core/test-data';
import {
  countingResources,
  countingTrees,
  hasCountingResource,
  isSelfReported,
  readinessSources,
  resourceLink,
  resourceTitle,
  titleList,
  toRequest,
} from './resources';

describe('resources', () => {
  it('names a resource by its label, or else its target', () => {
    expect(resourceTitle(aTreeResource(3, 'CSS', true, 'Styling'))).toBe('Styling');
    expect(resourceTitle(aTreeResource(3, 'CSS'))).toBe('CSS');
    expect(resourceTitle(aDeckResource(4, 'Flexbox cards'))).toBe('Flexbox cards');
    expect(resourceTitle(aUrlResource('https://example.com'))).toBe('https://example.com');
  });

  it('links trees and decks to their pages', () => {
    expect(resourceLink(aTreeResource(3, 'CSS'))).toEqual(['/trees', 3]);
    expect(resourceLink(aDeckResource(4, 'Flexbox cards'))).toEqual(['/decks', 4]);
    expect(resourceLink(aMaterialResource(6, 'Guide'))).toEqual(['/materials', 6]);
    expect(resourceTitle(aMaterialResource(6, 'Guide'))).toBe('Guide');
    expect(isSelfReported(aMaterialResource(6, 'Guide'))).toBe(true);
    expect(isSelfReported(aDeckResource(4, 'Flexbox cards'))).toBe(false);
    expect(resourceLink(aLessonResource(7, 'Lesson'))).toEqual(['/lessons', 7]);
    expect(resourceTitle(aLessonResource(7, 'Lesson'))).toBe('Lesson');
    expect(isSelfReported(aLessonResource(7, 'Lesson'))).toBe(true);
    expect(resourceLink(aQuestionSetResource(8, 'Exercises'))).toEqual(['/question-sets', 8]);
    expect(resourceTitle(aQuestionSetResource(8, 'Exercises'))).toBe('Exercises');
    expect(isSelfReported(aQuestionSetResource(8, 'Exercises'))).toBe(false);
    expect(resourceLink(aUrlResource('https://example.com'))).toBeNull();
  });

  it('finds what counts', () => {
    const resources = [
      aUrlResource('https://example.com'),
      aTreeResource(3, 'CSS'),
      aTreeResource(4, 'Reading', false),
      aDeckResource(5, 'Cards'),
    ];

    expect(countingTrees(resources)).toEqual([{ id: 3, title: 'CSS' }]);
    expect(countingResources(resources).map(resourceTitle)).toEqual(['CSS', 'Cards']);
    expect(hasCountingResource(resources)).toBe(true);
    expect(hasCountingResource([aTreeResource(4, 'Reading', false)])).toBe(false);
    expect(readinessSources(resources)).toBe('tree CSS and deck Cards');
  });

  it('lists titles in a sentence', () => {
    expect(titleList([])).toBe('');
    expect(titleList([{ id: 1, title: 'A' }])).toBe('A');
    expect(titleList(['A', 'B'])).toBe('A and B');
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
      deckId: null,
      materialId: null,
      lessonId: null,
      questionSetId: null,
      label: 'Styling',
      counts: false,
    });
    expect(toRequest(aDeckResource(4, 'Cards')).deckId).toBe(4);
    expect(toRequest(aMaterialResource(6, 'Guide')).materialId).toBe(6);
    expect(toRequest(aLessonResource(7, 'Lesson')).lessonId).toBe(7);
    expect(toRequest(aQuestionSetResource(8, 'Exercises')).questionSetId).toBe(8);
  });
});
