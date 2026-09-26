import { aCodingQuestion } from '../core/test-data';
import { questionStatus } from './question-status';

describe('questionStatus', () => {
  it('says whether a question is solved, and whether the solution was seen first', () => {
    expect(questionStatus(aCodingQuestion())).toBe('Not solved yet');
    expect(questionStatus(aCodingQuestion({ solutionRevealed: true }))).toBe(
      'Solution seen, not solved yet',
    );
    expect(questionStatus(aCodingQuestion({ solved: true }))).toBe('Solved');
    expect(
      questionStatus(
        aCodingQuestion({ solved: true, solutionRevealed: true, revealedBeforeSolved: true }),
      ),
    ).toBe('Solved (after seeing the solution)');
  });
});
