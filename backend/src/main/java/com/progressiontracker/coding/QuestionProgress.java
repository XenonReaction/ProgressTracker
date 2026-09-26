package com.progressiontracker.coding;

import java.time.Instant;

/**
 * Where one question stands for its user.
 *
 * @param solved whether the user has marked it solved
 * @param solutionRevealed whether the user has asked to see the solution
 * @param revealedBeforeSolved whether they saw the solution before marking it solved; kept so
 * a later version can treat that differently (today a solved question counts the same)
 * @param lastAttemptAt the latest reveal or solve, or null if none
 */
public record QuestionProgress(boolean solved, boolean solutionRevealed, boolean revealedBeforeSolved,
		Instant lastAttemptAt) {

	public static final QuestionProgress UNTOUCHED = new QuestionProgress(false, false, false, null);

}
