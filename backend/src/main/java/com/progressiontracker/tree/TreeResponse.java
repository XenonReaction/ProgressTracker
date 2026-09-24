package com.progressiontracker.tree;

import java.time.Instant;
import java.util.List;

/** Tree metadata. {@code editSessionStartedAt} is set while the tree is in edit mode. */
public record TreeResponse(
		Long id,
		String title,
		String description,
		String category,
		List<String> tags,
		Instant createdAt,
		Instant updatedAt,
		Instant editSessionStartedAt) {

	static TreeResponse from(Tree tree, Instant editSessionStartedAt) {
		return new TreeResponse(tree.getId(), tree.getTitle(), tree.getDescription(), tree.getCategory(),
				List.copyOf(tree.getTags()), tree.getCreatedAt(), tree.getUpdatedAt(), editSessionStartedAt);
	}

}
