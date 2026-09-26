package com.progressiontracker.flashcards.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.progressiontracker.TestcontainersConfiguration;
import com.progressiontracker.user.User;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class FlashcardMappingTest {

	@Autowired
	private TestEntityManager em;

	@Autowired
	private CardReviewRepository reviews;

	private User owner;

	private Deck deck;

	@BeforeEach
	void setUp() {
		owner = em.persist(new User("alice"));
		deck = new Deck(owner, "CSS Flexbox");
		deck.setDescription("One-dimensional layout");
		em.persist(deck);
	}

	@Test
	void decksCardsAndReviewsRoundTrip() {
		Card card = em.persist(new Card(deck, "Main axis?", "flex-direction"));
		Instant when = Instant.now().truncatedTo(ChronoUnit.MICROS);
		em.persist(new CardReview(card, owner, when, true));
		em.flush();
		em.clear();

		Deck reloadedDeck = em.find(Deck.class, deck.getId());
		assertThat(reloadedDeck.getTitle()).isEqualTo("CSS Flexbox");
		assertThat(reloadedDeck.getDescription()).isEqualTo("One-dimensional layout");
		assertThat(reloadedDeck.getCreatedAt()).isNotNull();
		Card reloadedCard = em.find(Card.class, card.getId());
		assertThat(reloadedCard.getFront()).isEqualTo("Main axis?");
		assertThat(reloadedCard.getBack()).isEqualTo("flex-direction");
		assertThat(reviews.findByUserAndCardInOrderByReviewedAtDescIdDesc(owner, List.of(reloadedCard)))
			.singleElement()
			.satisfies(review -> {
				assertThat(review.getReviewedAt()).isEqualTo(when);
				assertThat(review.isCorrect()).isTrue();
			});
	}

	@Test
	void deletingADeckDeletesItsCardsAndTheirReviews() {
		Card card = em.persist(new Card(deck, "Main axis?", "flex-direction"));
		em.persist(new CardReview(card, owner, Instant.now(), true));
		em.flush();
		em.clear();

		em.remove(em.find(Deck.class, deck.getId()));
		em.flush();

		assertThat(count("select count(c) from Card c")).isZero();
		assertThat(count("select count(r) from CardReview r")).isZero();
	}

	private long count(String jpql) {
		return em.getEntityManager().createQuery(jpql, Long.class).getSingleResult();
	}

}
