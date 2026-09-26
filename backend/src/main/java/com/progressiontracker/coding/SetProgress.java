package com.progressiontracker.coding;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

/**
 * Where a question set stands for its user.
 *
 * @param readiness the share of its questions solved, as a whole percent (0 for an empty set)
 * @param lastReviewedAt the latest attempt on any of its questions, or null if none
 */
public record SetProgress(int questionCount, int solvedCount, int readiness, Instant lastReviewedAt) {

	/** A question counts when solved: it's 0% or 100%, and the set is their average. */
	public static SetProgress of(Collection<QuestionProgress> questions) {
		int count = questions.size();
		int solved = (int) questions.stream().filter(QuestionProgress::solved).count();
		int readiness = count == 0 ? 0 : (int) Math.round(solved * 100.0 / count);
		Instant last = questions.stream()
			.map(QuestionProgress::lastAttemptAt)
			.filter(Objects::nonNull)
			.max(Comparator.naturalOrder())
			.orElse(null);
		return new SetProgress(count, solved, readiness, last);
	}

}
