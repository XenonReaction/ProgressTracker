package com.progressiontracker.flashcards;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class CardProgressTest {

	private static final Instant WHEN = Instant.parse("2026-09-01T10:00:00Z");

	@Test
	void aCardNeverAnsweredHasNotPassedAndIsNotScheduled() {
		assertThat(CardProgress.of(List.of(), null)).isEqualTo(new CardProgress(0, false, null, null));
	}

	@Test
	void threeCorrectAnswersInARowPassACard() {
		assertThat(CardProgress.of(List.of(true, true), WHEN)).isEqualTo(new CardProgress(2, false, WHEN, null));
		assertThat(CardProgress.of(List.of(true, true, true), WHEN))
			.isEqualTo(new CardProgress(3, true, WHEN, WHEN.plus(Duration.ofDays(7))));
	}

	@Test
	void onlyTheLatestAnswersCount() {
		// Newest first: a wrong answer after three right ones un-passes the card...
		assertThat(CardProgress.of(List.of(false, true, true, true), WHEN).passed()).isFalse();
		// ...until it's right three times in a row again
		assertThat(CardProgress.of(List.of(true, true, true, false, true), WHEN).passed()).isTrue();
		// The streak shown stops at the pass mark
		assertThat(CardProgress.of(List.of(true, true, true, true, true), WHEN).correctInARow()).isEqualTo(3);
		assertThat(CardProgress.of(List.of(true, false, true, true), WHEN).correctInARow()).isEqualTo(1);
	}

	@Test
	void aPassedCardWaitsLongerAfterEachCorrectAnswer() {
		assertThat(dueAfter(3)).isEqualTo(Duration.ofDays(7));
		assertThat(dueAfter(4)).isEqualTo(Duration.ofDays(14));
		assertThat(dueAfter(5)).isEqualTo(Duration.ofDays(30));
		assertThat(dueAfter(6)).isEqualTo(Duration.ofDays(60));
		assertThat(dueAfter(10)).isEqualTo(Duration.ofDays(60));
		// Not passed: always on the review page, so no due date
		assertThat(CardProgress.of(List.of(false, true, true, true, true), WHEN).dueAt()).isNull();
	}

	@Test
	void aPassedCardIsDueOnceItsDateHasCome() {
		CardProgress passed = CardProgress.of(List.of(true, true, true), WHEN);

		assertThat(passed.isDue(WHEN.plus(Duration.ofDays(6)))).isFalse();
		assertThat(passed.isDue(WHEN.plus(Duration.ofDays(7)))).isTrue();
		assertThat(CardProgress.of(List.of(true), WHEN).isDue(WHEN.plus(Duration.ofDays(100)))).isFalse();
	}

	private static Duration dueAfter(int correctInARow) {
		List<Boolean> answers = Collections.nCopies(correctInARow, true);
		return Duration.between(WHEN, CardProgress.of(answers, WHEN).dueAt());
	}

}
