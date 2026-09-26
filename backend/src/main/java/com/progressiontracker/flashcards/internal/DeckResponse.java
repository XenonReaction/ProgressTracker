package com.progressiontracker.flashcards.internal;

import java.time.Instant;

import com.progressiontracker.flashcards.DeckProgress;

/**
 * A deck with where it stands: {@code readiness} is the share of its cards that have passed,
 * and it's {@code complete} once that reaches 80%.
 */
public record DeckResponse(
		Long id,
		String title,
		String description,
		int cardCount,
		int passedCount,
		int readiness,
		boolean complete,
		Instant lastReviewedAt,
		Instant createdAt,
		Instant updatedAt) {

	static DeckResponse from(Deck deck, DeckProgress progress) {
		return new DeckResponse(deck.getId(), deck.getTitle(), deck.getDescription(), progress.cardCount(),
				progress.passedCount(), progress.readiness(), progress.complete(), progress.lastReviewedAt(),
				deck.getCreatedAt(), deck.getUpdatedAt());
	}

}
