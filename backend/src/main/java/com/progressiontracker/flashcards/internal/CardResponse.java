package com.progressiontracker.flashcards.internal;

import java.time.Instant;

import com.progressiontracker.flashcards.CardProgress;

/**
 * A card with where it stands. It has {@code passed} when its last 3 answers were correct;
 * {@code correctInARow} counts towards that. {@code deckTitle} is there for the review page,
 * which mixes cards from every deck.
 */
public record CardResponse(
		Long id,
		Long deckId,
		String deckTitle,
		String front,
		String back,
		int correctInARow,
		boolean passed,
		Instant lastReviewedAt,
		Instant createdAt,
		Instant updatedAt) {

	static CardResponse from(Card card, CardProgress progress) {
		return new CardResponse(card.getId(), card.getDeck().getId(), card.getDeck().getTitle(), card.getFront(),
				card.getBack(), progress.correctInARow(), progress.passed(), progress.lastReviewedAt(),
				card.getCreatedAt(), card.getUpdatedAt());
	}

}
