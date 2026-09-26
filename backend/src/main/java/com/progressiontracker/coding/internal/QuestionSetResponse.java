package com.progressiontracker.coding.internal;

import java.time.Instant;

import com.progressiontracker.coding.SetProgress;

/** A question set with where it stands: {@code readiness} is the share of its questions solved. */
public record QuestionSetResponse(
		Long id,
		String title,
		String description,
		int questionCount,
		int solvedCount,
		int readiness,
		Instant lastReviewedAt,
		Instant createdAt,
		Instant updatedAt) {

	static QuestionSetResponse from(QuestionSet set, SetProgress progress) {
		return new QuestionSetResponse(set.getId(), set.getTitle(), set.getDescription(), progress.questionCount(),
				progress.solvedCount(), progress.readiness(), progress.lastReviewedAt(), set.getCreatedAt(),
				set.getUpdatedAt());
	}

}
