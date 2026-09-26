package com.progressiontracker.coding;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

class SetProgressTest {

	private static final Instant EARLIER = Instant.parse("2026-09-01T10:00:00Z");

	private static final Instant LATER = Instant.parse("2026-09-15T10:00:00Z");

	@Test
	void anEmptySetIsAtZero() {
		assertThat(SetProgress.of(List.of())).isEqualTo(new SetProgress(0, 0, 0, null));
	}

	@Test
	void readinessIsTheShareOfQuestionsSolvedWhetherOrNotTheSolutionWasSeenFirst() {
		SetProgress progress = SetProgress.of(List.of(new QuestionProgress(true, false, false, EARLIER),
				new QuestionProgress(true, true, true, LATER), new QuestionProgress(false, true, false, EARLIER)));

		// 2 of 3 solved: 67%; the latest attempt is the set's last review
		assertThat(progress).isEqualTo(new SetProgress(3, 2, 67, LATER));
	}

	@Test
	void aRevealedButUnsolvedQuestionDoesNotCount() {
		assertThat(SetProgress.of(List.of(new QuestionProgress(false, true, false, EARLIER))).readiness()).isZero();
	}

}
