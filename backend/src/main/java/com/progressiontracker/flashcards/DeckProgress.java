package com.progressiontracker.flashcards;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

/**
 * Where a deck stands for its user.
 *
 * @param readiness the share of its cards that have passed, as a whole percent (0 for an
 * empty deck)
 * @param complete whether at least {@link #COMPLETE_PERCENT}% of its cards have passed
 * @param lastReviewedAt the latest answer to any of its cards, or null if none
 */
public record DeckProgress(int cardCount, int passedCount, int readiness, boolean complete, Instant lastReviewedAt) {

	/**
	 * The share of passed cards at which a deck counts as complete. It matches a tree node's
	 * default aggregate threshold, so a complete deck reads as "ready to move on".
	 */
	public static final int COMPLETE_PERCENT = 80;

	public static DeckProgress of(Collection<CardProgress> cards) {
		int cardCount = cards.size();
		int passedCount = (int) cards.stream().filter(CardProgress::passed).count();
		int readiness = cardCount == 0 ? 0 : (int) Math.round(passedCount * 100.0 / cardCount);
		boolean complete = cardCount > 0 && passedCount * 100 >= COMPLETE_PERCENT * cardCount;
		Instant lastReviewedAt = cards.stream()
			.map(CardProgress::lastReviewedAt)
			.filter(Objects::nonNull)
			.max(Comparator.naturalOrder())
			.orElse(null);
		return new DeckProgress(cardCount, passedCount, readiness, complete, lastReviewedAt);
	}

}
