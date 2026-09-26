import { Card } from '../core/api.models';

/** Correct answers in a row that pass a card (the backend's `CardProgress.PASS_STREAK`). */
export const PASS_STREAK = 3;

/** Share of passed cards at which a deck is complete (the backend's `DeckProgress.COMPLETE_PERCENT`). */
export const COMPLETE_PERCENT = 80;

/** Where a card stands, in words. */
export function cardStatus(card: Card): string {
  if (card.passed) {
    return 'Passed';
  }
  if (card.lastReviewedAt === null) {
    return 'Not answered yet';
  }
  return `${card.correctInARow} of ${PASS_STREAK} correct in a row`;
}
