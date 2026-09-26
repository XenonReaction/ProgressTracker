package com.progressiontracker.flashcards;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

class CardProgressTest {

	private static final Instant WHEN = Instant.parse("2026-09-01T10:00:00Z");

	@Test
	void aCardNeverAnsweredHasNotPassed() {
		assertThat(CardProgress.of(List.of(), null)).isEqualTo(new CardProgress(0, false, null));
	}

	@Test
	void threeCorrectAnswersInARowPassACard() {
		assertThat(CardProgress.of(List.of(true, true), WHEN)).isEqualTo(new CardProgress(2, false, WHEN));
		assertThat(CardProgress.of(List.of(true, true, true), WHEN)).isEqualTo(new CardProgress(3, true, WHEN));
	}

	@Test
	void onlyTheLatestAnswersCount() {
		// Newest first: a wrong answer after three right ones un-passes the card...
		assertThat(CardProgress.of(List.of(false, true, true, true), WHEN).passed()).isFalse();
		// ...until it's right three times in a row again
		assertThat(CardProgress.of(List.of(true, true, true, false, true), WHEN))
			.isEqualTo(new CardProgress(3, true, WHEN));
		// The streak stops at the pass mark
		assertThat(CardProgress.of(List.of(true, true, true, true, true), WHEN).correctInARow()).isEqualTo(3);
		assertThat(CardProgress.of(List.of(true, false, true, true), WHEN).correctInARow()).isEqualTo(1);
	}

}
