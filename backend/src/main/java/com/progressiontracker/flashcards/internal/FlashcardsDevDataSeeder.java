package com.progressiontracker.flashcards.internal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.user.CurrentUserService;
import com.progressiontracker.user.User;

/**
 * Sample flashcards for local development, alongside the progression module's dev seed. Runs
 * only with the {@code dev} profile, never with {@code prod}, and does nothing if the default
 * user already has a deck.
 * <p>
 * Contents: a "CSS Flexbox" deck of five cards. One has passed (three correct answers), one is
 * two correct answers in, one was last answered wrong and two have never been answered, so
 * the deck is at 20% and the review page lists four cards.
 */
@Component
@Profile("dev & !prod")
public class FlashcardsDevDataSeeder implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(FlashcardsDevDataSeeder.class);

	private final CurrentUserService currentUser;

	private final DeckRepository decks;

	private final CardRepository cards;

	private final CardReviewRepository reviews;

	public FlashcardsDevDataSeeder(CurrentUserService currentUser, DeckRepository decks, CardRepository cards,
			CardReviewRepository reviews) {
		this.currentUser = currentUser;
		this.decks = decks;
		this.cards = cards;
		this.reviews = reviews;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		User user = currentUser.getCurrentUser();
		if (decks.existsByOwner(user)) {
			log.info("User '{}' already has decks; skipping flashcards dev seed", user.getUsername());
			return;
		}
		Instant dayAgo = Instant.now().minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MICROS);

		Deck flexbox = new Deck(user, "CSS Flexbox");
		flexbox.setDescription("Laying out items in one dimension.");
		decks.save(flexbox);
		Card display = card(flexbox, "Which declaration makes an element a flex container?", "display: flex");
		Card direction = card(flexbox, "Which property sets the main axis?", "flex-direction");
		Card justify = card(flexbox, "Which property aligns items along the main axis?", "justify-content");
		card(flexbox, "Which property aligns items along the cross axis?", "align-items");
		card(flexbox, "What does flex: 1 expand to?", "flex-grow: 1; flex-shrink: 1; flex-basis: 0%");

		answer(display, user, dayAgo, true, true, true);
		answer(direction, user, dayAgo.plusSeconds(60), true, true);
		answer(justify, user, dayAgo.plusSeconds(120), true, false);

		log.info("Seeded flashcards dev data for user '{}'", user.getUsername());
	}

	private Card card(Deck deck, String front, String back) {
		return cards.save(new Card(deck, front, back));
	}

	/** Answers the card once for each result, a second apart, oldest first. */
	private void answer(Card card, User user, Instant from, boolean... correct) {
		for (int i = 0; i < correct.length; i++) {
			reviews.save(new CardReview(card, user, from.plusSeconds(i), correct[i]));
		}
	}

}
