package com.progressiontracker.flashcards;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.progressiontracker.flashcards.internal.Card;
import com.progressiontracker.flashcards.internal.CardRepository;
import com.progressiontracker.flashcards.internal.CardReview;
import com.progressiontracker.flashcards.internal.CardReviewRepository;
import com.progressiontracker.flashcards.internal.Deck;
import com.progressiontracker.flashcards.internal.DeckRepository;
import com.progressiontracker.user.CurrentUserService;
import com.progressiontracker.user.User;

/**
 * Readiness, "last reviewed" and due reviews for cards and decks, worked out from the current
 * user's recorded answers when asked, never stored. The rules are in {@link CardProgress} and
 * {@link DeckProgress}. Call it inside a transaction.
 */
@Component
public class FlashcardReadinessCalculator {

	private final DeckRepository decks;

	private final CardRepository cards;

	private final CardReviewRepository reviews;

	private final CurrentUserService currentUser;

	private final Clock clock;

	public FlashcardReadinessCalculator(DeckRepository decks, CardRepository cards, CardReviewRepository reviews,
			CurrentUserService currentUser, Clock clock) {
		this.decks = decks;
		this.cards = cards;
		this.reviews = reviews;
		this.currentUser = currentUser;
		this.clock = clock;
	}

	/** Now, by the application's clock. */
	public Instant now() {
		return clock.instant();
	}

	/** Each card's progress, by card id. */
	public Map<Long, CardProgress> cards(Collection<Card> cardsToCheck) {
		if (cardsToCheck.isEmpty()) {
			return Map.of();
		}
		User user = currentUser.getCurrentUser();
		Map<Long, List<CardReview>> byCard = new HashMap<>();
		for (CardReview review : reviews.findByUserAndCardInOrderByReviewedAtDescIdDesc(user, cardsToCheck)) {
			byCard.computeIfAbsent(review.getCard().getId(), id -> new ArrayList<>()).add(review);
		}
		Map<Long, CardProgress> progress = new LinkedHashMap<>();
		for (Card card : cardsToCheck) {
			List<CardReview> newestFirst = byCard.getOrDefault(card.getId(), List.of());
			progress.put(card.getId(),
					newestFirst.isEmpty() ? CardProgress.NEVER_REVIEWED
							: CardProgress.of(newestFirst.stream().map(CardReview::isCorrect).toList(),
									newestFirst.get(0).getReviewedAt()));
		}
		return progress;
	}

	/** Each deck's progress, by deck id. */
	public Map<Long, DeckProgress> decks(Collection<Deck> decks) {
		if (decks.isEmpty()) {
			return Map.of();
		}
		List<Card> allCards = cards.findByDeckInOrderByIdAsc(decks);
		Map<Long, CardProgress> cardProgress = cards(allCards);
		Map<Long, List<CardProgress>> byDeck = new HashMap<>();
		for (Card card : allCards) {
			byDeck.computeIfAbsent(card.getDeck().getId(), id -> new ArrayList<>()).add(cardProgress.get(card.getId()));
		}
		Map<Long, DeckProgress> progress = new LinkedHashMap<>();
		for (Deck deck : decks) {
			progress.put(deck.getId(), DeckProgress.of(byDeck.getOrDefault(deck.getId(), List.of()), now()));
		}
		return progress;
	}

	public DeckProgress deck(Deck deck) {
		return decks(List.of(deck)).get(deck.getId());
	}

	/**
	 * One of the current user's decks by id, with its progress: how other modules read a
	 * deck. Empty if there's no such deck or it's someone else's.
	 */
	public Optional<DeckSummary> deck(Long deckId) {
		return decks.findByIdAndOwner(deckId, currentUser.getCurrentUser())
			.map(deck -> new DeckSummary(deck.getId(), deck.getTitle(), deck(deck)));
	}

	/**
	 * The batch form of {@link #deck(Long)}: the current user's decks among these ids, with
	 * their progress, by id, in a fixed number of queries. Ids that aren't the user's decks
	 * are left out.
	 */
	public Map<Long, DeckSummary> deckSummaries(Collection<Long> deckIds) {
		if (deckIds.isEmpty()) {
			return Map.of();
		}
		List<Deck> found = decks.findByIdInAndOwner(deckIds, currentUser.getCurrentUser());
		Map<Long, DeckProgress> progress = decks(found);
		Map<Long, DeckSummary> summaries = new HashMap<>();
		for (Deck deck : found) {
			summaries.put(deck.getId(), new DeckSummary(deck.getId(), deck.getTitle(), progress.get(deck.getId())));
		}
		return summaries;
	}

}
