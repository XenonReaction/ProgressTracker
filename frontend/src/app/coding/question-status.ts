import { CodingLanguage, CodingQuestion } from '../core/api.models';

export const LANGUAGE_NAMES: Record<CodingLanguage, string> = { html: 'HTML', css: 'CSS' };

/** Where a question stands, in words. */
export function questionStatus(question: CodingQuestion): string {
  if (question.solved) {
    return question.revealedBeforeSolved ? 'Solved (after seeing the solution)' : 'Solved';
  }
  return question.solutionRevealed ? 'Solution seen, not solved yet' : 'Not solved yet';
}
