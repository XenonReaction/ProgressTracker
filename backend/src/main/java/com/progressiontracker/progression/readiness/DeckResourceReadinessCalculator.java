package com.progressiontracker.progression.readiness;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.progressiontracker.flashcards.DeckSummary;
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
		return statusOf(resource.getDeckId(), flashcards.deck(resource.getDeckId()).orElse(null));
	}

	@Override
	public Map<String, ResourceStatus> statuses(Collection<NodeResource> resources, ReadinessContext context) {
		Map<Long, DeckSummary> decks = flashcards
			.deckSummaries(resources.stream().map(NodeResource::getDeckId).collect(Collectors.toSet()));
		return ReadinessCalculator.byTargetKey(resources,
				resource -> statusOf(resource.getDeckId(), decks.get(resource.getDeckId())));
	}

	private static ResourceStatus statusOf(Long deckId, DeckSummary deck) {
		if (deck == null) {
			// Deleting a deck that nodes list is refused, so this only guards against bad data
			log.warn("Deck {} not found; counting it as 0% readiness", deckId);
			return new ResourceStatus(null, 0, null);
		}
		return new ResourceStatus(deck.title(), deck.progress().readiness(), deck.progress().lastReviewedAt(),
				deck.progress().dueCount() > 0);
	}

}
