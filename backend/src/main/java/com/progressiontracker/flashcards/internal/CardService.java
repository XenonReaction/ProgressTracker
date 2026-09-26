package com.progressiontracker.flashcards.internal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.flashcards.CardProgress;
import com.progressiontracker.flashcards.FlashcardReadinessCalculator;
import com.progressiontracker.user.CurrentUserService;

/** A deck's cards, the answers given to them, and the review queue. */
@Service
@Transactional
public class CardService {

	private static final Logger log = LoggerFactory.getLogger(CardService.class);

	/** Never-answered cards first, then the least recently answered; ties in the order they were added. */
	private static final Comparator<CardResponse> REVIEW_ORDER = Comparator
		.comparing(CardResponse::lastReviewedAt, Comparator.nullsFirst(Comparator.naturalOrder()))
		.thenComparing(CardResponse::id);

	private final CardRepository cards;

	private final CardReviewRepository reviews;

	private final DeckService deckService;

	private final FlashcardReadinessCalculator readiness;

	private final CurrentUserService currentUser;

	public CardService(CardRepository cards, CardReviewRepository reviews, DeckService deckService,
			FlashcardReadinessCalculator readiness, CurrentUserService currentUser) {
		this.cards = cards;
		this.reviews = reviews;
		this.deckService = deckService;
		this.readiness = readiness;
		this.currentUser = currentUser;
	}

	@Transactional(readOnly = true)
	public List<CardResponse> list(Long deckId) {
		return toResponses(cards.findByDeckOrderByIdAsc(deckService.findOwned(deckId)));
	}

	public CardResponse create(Long deckId, CardRequest request) {
		Card card = cards.save(new Card(deckService.findOwned(deckId), request.front(), request.back()));
		return CardResponse.from(card, CardProgress.NEVER_REVIEWED);
	}

	public CardResponse update(Long deckId, Long cardId, CardRequest request) {
		Card card = findInDeck(deckId, cardId);
		card.setFront(request.front());
		card.setBack(request.back());
		cards.flush(); // so the response carries the new updatedAt
		return toResponse(card);
	}

	public void delete(Long deckId, Long cardId) {
		cards.delete(findInDeck(deckId, cardId));
		log.info("Deleted card {} from deck {}", cardId, deckId);
	}

	/** Records one answer and returns the card's new standing. */
	public CardResponse review(Long deckId, Long cardId, ReviewRequest request) {
		Card card = findInDeck(deckId, cardId);
		Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS); // the column's precision
		reviews.save(new CardReview(card, currentUser.getCurrentUser(), now, request.correct()));
		return toResponse(card);
	}

	/**
	 * Every card that hasn't passed yet, in one deck or (with no {@code deckId}) all of the
	 * user's decks: never-answered cards first, then the least recently answered.
	 */
	@Transactional(readOnly = true)
	public List<CardResponse> reviewQueue(Long deckId) {
		List<Deck> decks = deckService.findOwnedOrAll(deckId);
		if (decks.isEmpty()) {
			return List.of();
		}
		return toResponses(cards.findByDeckInOrderByIdAsc(decks)).stream()
			.filter(card -> !card.passed())
			.sorted(REVIEW_ORDER)
			.toList();
	}

	private Card findInDeck(Long deckId, Long cardId) {
		return cards.findByIdAndDeck(cardId, deckService.findOwned(deckId))
			.orElseThrow(() -> new NotFoundException("Card", cardId));
	}

	private CardResponse toResponse(Card card) {
		return toResponses(List.of(card)).get(0);
	}

	private List<CardResponse> toResponses(List<Card> cardsToMap) {
		Map<Long, CardProgress> progress = readiness.cards(cardsToMap);
		return cardsToMap.stream().map(card -> CardResponse.from(card, progress.get(card.getId()))).toList();
	}

}
