package com.progressiontracker.tree;

import java.time.Instant;
import java.util.List;

public record TreeResponse(
		Long id,
		String title,
		String description,
		String category,
		List<String> tags,
		Instant createdAt,
		Instant updatedAt) {

	static TreeResponse from(Tree tree) {
		return new TreeResponse(tree.getId(), tree.getTitle(), tree.getDescription(), tree.getCategory(),
				List.copyOf(tree.getTags()), tree.getCreatedAt(), tree.getUpdatedAt());
	}

}
