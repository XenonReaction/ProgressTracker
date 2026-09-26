package com.progressiontracker.progression.tree;

import java.time.Instant;
import java.util.List;

import com.progressiontracker.progression.readiness.ReadinessContext;

/**
 * Tree metadata, with the tree's own readiness (the average of its nodes') and the latest
 * review beneath any of its nodes. {@code editSessionStartedAt} is set while the tree is in
 * edit mode.
 */
public record TreeResponse(
		Long id,
		String title,
		String description,
		String category,
		List<String> tags,
		int readiness,
		Instant lastReviewedAt,
		Instant createdAt,
		Instant updatedAt,
		Instant editSessionStartedAt) {

	static TreeResponse from(Tree tree, ReadinessContext readiness, Instant editSessionStartedAt) {
		return new TreeResponse(tree.getId(), tree.getTitle(), tree.getDescription(), tree.getCategory(),
				List.copyOf(tree.getTags()), readiness.ofTree(tree), readiness.lastReviewedOfTree(tree),
				tree.getCreatedAt(), tree.getUpdatedAt(), editSessionStartedAt);
	}

}
