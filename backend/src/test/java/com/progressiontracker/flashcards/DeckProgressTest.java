package com.progressiontracker.flashcards;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class DeckProgressTest {

	private static final Instant EARLIER = Instant.parse("2026-09-01T10:00:00Z");

	private static final Instant LATER = Instant.parse("2026-09-15T10:00:00Z");

	@Test
	void anEmptyDeckIsAtZeroAndNotComplete() {
		assertThat(DeckProgress.of(List.of())).isEqualTo(new DeckProgress(0, 0, 0, false, null));
	}

	@Test
	void readinessIsTheShareOfCardsPassed() {
		// 16 of 20 passed: 80%, which is complete
		DeckProgress progress = DeckProgress.of(cards(16, 4));

		assertThat(progress.passedCount()).isEqualTo(16);
		assertThat(progress.cardCount()).isEqualTo(20);
		assertThat(progress.readiness()).isEqualTo(80);
		assertThat(progress.complete()).isTrue();
	}

	@Test
	void aDeckJustUnderEightyPercentIsNotComplete() {
		// 79 of 100 is 79%; 2 of 3 is 67%
		assertThat(DeckProgress.of(cards(79, 21)).complete()).isFalse();
		assertThat(DeckProgress.of(cards(2, 1))).extracting(DeckProgress::readiness, DeckProgress::complete)
			.containsExactly(67, false);
	}

	@Test
	void completenessUsesTheExactShareNotTheRoundedOne() {
		// 159 of 199 is 79.9%: shown as 80%, but not yet complete
		DeckProgress progress = DeckProgress.of(cards(159, 40));

		assertThat(progress.readiness()).isEqualTo(80);
		assertThat(progress.complete()).isFalse();
	}

	@Test
	void lastReviewedIsTheLatestAnswerToAnyCard() {
		DeckProgress progress = DeckProgress.of(List.of(new CardProgress(1, false, EARLIER),
				CardProgress.NEVER_REVIEWED, new CardProgress(0, false, LATER)));

		assertThat(progress.lastReviewedAt()).isEqualTo(LATER);
	}

	private static List<CardProgress> cards(int passed, int notPassed) {
		List<CardProgress> cards = new ArrayList<>();
		for (int i = 0; i < passed; i++) {
			cards.add(new CardProgress(3, true, EARLIER));
		}
		for (int i = 0; i < notPassed; i++) {
			cards.add(CardProgress.NEVER_REVIEWED);
		}
		return cards;
	}

}
