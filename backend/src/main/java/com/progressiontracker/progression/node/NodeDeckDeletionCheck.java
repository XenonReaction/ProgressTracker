package com.progressiontracker.progression.node;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.progressiontracker.common.ConflictException;
import com.progressiontracker.flashcards.DeckDeletionCheck;

/**
 * Refuses to delete a deck that nodes list as a resource (409, listing them), as deleting a
 * tree that nodes list is refused. It would silently change those nodes.
 */
@Component
class NodeDeckDeletionCheck implements DeckDeletionCheck {

	private final NodeRepository nodes;

	NodeDeckDeletionCheck(NodeRepository nodes) {
		this.nodes = nodes;
	}

	@Override
	public void checkCanDelete(Long deckId) {
		List<Node> listing = nodes.findListing(NodeResourceType.DECK, deckId);
		if (!listing.isEmpty()) {
			throw new ConflictException(
					"Deck " + deckId + " is a resource of " + listing.size() + " node(s); remove it from them first",
					Map.of("nodes", listing.stream().map(NodeRef::of).toList()));
		}
	}

}
