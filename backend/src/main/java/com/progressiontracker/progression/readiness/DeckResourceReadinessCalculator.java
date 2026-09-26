package com.progressiontracker.progression.readiness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.progressiontracker.flashcards.FlashcardReadinessCalculator;
import com.progressiontracker.progression.node.NodeResource;
import com.progressiontracker.progression.node.NodeResourceType;

/**
 * A flashcard deck resource: the share of its cards passed, its latest answer, and whether any
 * card is due for review, from the Flashcards module's public API.
 */
@Component
class DeckResourceReadinessCalculator implements ReadinessCalculator {

	private static final Logger log = LoggerFactory.getLogger(DeckResourceReadinessCalculator.class);

	private final FlashcardReadinessCalculator flashcards;

	DeckResourceReadinessCalculator(FlashcardReadinessCalculator flashcards) {
		this.flashcards = flashcards;
	}

	@Override
	public NodeResourceType resourceType() {
		return NodeResourceType.DECK;
	}

	@Override
	public ResourceStatus status(NodeResource resource, ReadinessContext context) {
		return flashcards.deck(resource.getDeckId())
			.map(deck -> new ResourceStatus(deck.title(), deck.progress().readiness(),
					deck.progress().lastReviewedAt(), deck.progress().dueCount() > 0))
			.orElseGet(() -> {
				// Deleting a deck that nodes list is refused, so this only guards against bad data
				log.warn("Deck {} not found; counting it as 0% readiness", resource.getDeckId());
				return new ResourceStatus(null, 0, null);
			});
	}

}
