import { aCard } from '../core/test-data';
import { cardStatus } from './card-status';

describe('cardStatus', () => {
  it('says whether a card has passed, or how close it is', () => {
    expect(
      cardStatus(aCard({ passed: true, correctInARow: 3, lastReviewedAt: '2026-09-01T00:00:00Z' })),
    ).toBe('Passed');
    expect(cardStatus(aCard())).toBe('Not answered yet');
    expect(cardStatus(aCard({ correctInARow: 2, lastReviewedAt: '2026-09-01T00:00:00Z' }))).toBe(
      '2 of 3 correct in a row',
    );
    expect(cardStatus(aCard({ correctInARow: 0, lastReviewedAt: '2026-09-01T00:00:00Z' }))).toBe(
      '0 of 3 correct in a row',
    );
  });
});
