package com.progressiontracker.flashcards;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Where one card stands for its user.
 *
 * @param correctInARow how many of the latest answers were correct, counting back to the last
 * wrong one, up to {@link #PASS_STREAK}
 * @param passed whether the last {@link #PASS_STREAK} answers were all correct
 * @param lastReviewedAt when it was last answered, or null if never
 * @param dueAt when a passed card should be reviewed again (see {@link #intervalAfter}), or
 * null for a card not yet passed, which is always on the review page
 */
public record CardProgress(int correctInARow, boolean passed, Instant lastReviewedAt, Instant dueAt) {

	/** Correct answers in a row that pass a card. One wrong answer un-passes it again. */
	public static final int PASS_STREAK = 3;

	public static final CardProgress NEVER_REVIEWED = new CardProgress(0, false, null, null);

	/**
	 * @param answersNewestFirst whether each answer was correct, the latest first
	 * @param lastReviewedAt when the latest answer was given
	 */
	public static CardProgress of(List<Boolean> answersNewestFirst, Instant lastReviewedAt) {
		if (answersNewestFirst.isEmpty()) {
			return NEVER_REVIEWED;
		}
		int streak = 0;
		while (streak < answersNewestFirst.size() && answersNewestFirst.get(streak)) {
			streak++;
		}
		boolean passed = streak >= PASS_STREAK;
		return new CardProgress(Math.min(streak, PASS_STREAK), passed, lastReviewedAt,
				passed ? lastReviewedAt.plus(intervalAfter(streak)) : null);
	}

	/**
	 * How long a passed card waits for its next review after {@code streak} correct answers in
	 * a row: 7 days once it passes, then 14, 30, and 60 days from then on.
	 */
	public static Duration intervalAfter(int streak) {
		return switch (streak) {
			case PASS_STREAK -> Duration.ofDays(7);
			case PASS_STREAK + 1 -> Duration.ofDays(14);
			case PASS_STREAK + 2 -> Duration.ofDays(30);
			default -> Duration.ofDays(60);
		};
	}

	/** Whether this is a passed card whose next review has come. */
	public boolean isDue(Instant now) {
		return dueAt != null && !dueAt.isAfter(now);
	}

}
