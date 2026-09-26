package com.progressiontracker.flashcards.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.progressiontracker.TestcontainersConfiguration;

@SpringBootTest
@ActiveProfiles("dev")
@Import(TestcontainersConfiguration.class)
class FlashcardsDevDataSeederTest {

	@Autowired
	private FlashcardsDevDataSeeder seeder;

	@Autowired
	private DeckService decks;

	@Autowired
	private CardService cards;

	@Test
	void seedsOneDeckWithCardsInEveryStateAndIsIdempotent() {
		assertSeeded();

		seeder.run(new DefaultApplicationArguments());

		assertSeeded();
	}

	private void assertSeeded() {
		assertThat(decks.list()).singleElement().satisfies(deck -> {
			assertThat(deck.title()).isEqualTo("CSS Flexbox");
			assertThat(deck.cardCount()).isEqualTo(5);
			assertThat(deck.passedCount()).isEqualTo(1);
			assertThat(deck.readiness()).isEqualTo(20);
			assertThat(deck.lastReviewedAt()).isNotNull();
			assertThat(deck.dueCount()).isEqualTo(1);
		});
		// Never answered first, then the least recently answered: the passed card is due
		assertThat(cards.reviewQueue(null))
			.extracting(CardResponse::back, CardResponse::correctInARow, CardResponse::due)
			.containsExactly(tuple("align-items", 0, false),
					tuple("flex-grow: 1; flex-shrink: 1; flex-basis: 0%", 0, false), tuple("display: flex", 3, true),
					tuple("flex-direction", 2, false), tuple("justify-content", 0, false));
	}

}
