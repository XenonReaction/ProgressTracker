package com.progressiontracker.flashcards;

import java.time.Instant;
import java.util.List;

/**
 * Where one card stands for its user.
 *
 * @param correctInARow how many of the latest answers were correct, counting back to the last
 * wrong one, up to {@link #PASS_STREAK}
 * @param passed whether the last {@link #PASS_STREAK} answers were all correct
 * @param lastReviewedAt when it was last answered, or null if never
 */
public record CardProgress(int correctInARow, boolean passed, Instant lastReviewedAt) {

	/** Correct answers in a row that pass a card. One wrong answer un-passes it again. */
	public static final int PASS_STREAK = 3;

	public static final CardProgress NEVER_REVIEWED = new CardProgress(0, false, null);

	/**
	 * @param answersNewestFirst whether each answer was correct, the latest first
	 * @param lastReviewedAt when the latest answer was given
	 */
	public static CardProgress of(List<Boolean> answersNewestFirst, Instant lastReviewedAt) {
		if (answersNewestFirst.isEmpty()) {
			return NEVER_REVIEWED;
		}
		int inARow = 0;
		while (inARow < PASS_STREAK && inARow < answersNewestFirst.size() && answersNewestFirst.get(inARow)) {
			inARow++;
		}
		return new CardProgress(inARow, inARow == PASS_STREAK, lastReviewedAt);
	}

}
