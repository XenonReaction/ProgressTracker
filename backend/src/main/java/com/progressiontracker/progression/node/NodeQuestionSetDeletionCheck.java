package com.progressiontracker.progression.node;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.progressiontracker.common.ConflictException;
import com.progressiontracker.coding.QuestionSetDeletionCheck;

/**
 * Refuses to delete a question set that nodes list as a resource (409, listing them), as for trees,
 * decks, materials and lessons.
 */
@Component
class NodeQuestionSetDeletionCheck implements QuestionSetDeletionCheck {

	private final NodeRepository nodes;

	NodeQuestionSetDeletionCheck(NodeRepository nodes) {
		this.nodes = nodes;
	}

	@Override
	public void checkCanDelete(Long setId) {
		List<Node> listing = nodes.findListing(NodeResourceType.QUESTION_SET, setId);
		if (!listing.isEmpty()) {
			throw new ConflictException("Question set " + setId + " is a resource of " + listing.size()
					+ " node(s); remove it from them first",
					Map.of("nodes", listing.stream().map(NodeRef::of).toList()));
		}
	}

}
