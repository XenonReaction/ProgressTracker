package com.progressiontracker.progression.node;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.progressiontracker.common.ConflictException;
import com.progressiontracker.lessons.LessonDeletionCheck;

/**
 * Refuses to delete a lesson that nodes list as a resource (409, listing them), as for trees,
 * decks and materials.
 */
@Component
class NodeLessonDeletionCheck implements LessonDeletionCheck {

	private final NodeRepository nodes;

	NodeLessonDeletionCheck(NodeRepository nodes) {
		this.nodes = nodes;
	}

	@Override
	public void checkCanDelete(Long lessonId) {
		List<Node> listing = nodes.findListing(NodeResourceType.LESSON, lessonId);
		if (!listing.isEmpty()) {
			throw new ConflictException("Lesson " + lessonId + " is a resource of " + listing.size()
					+ " node(s); remove it from them first",
					Map.of("nodes", listing.stream().map(NodeRef::of).toList()));
		}
	}

}
