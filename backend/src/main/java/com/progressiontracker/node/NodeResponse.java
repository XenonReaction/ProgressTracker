package com.progressiontracker.node;

import java.time.Instant;
import java.util.List;

import com.progressiontracker.tree.TreeRef;

/**
 * A library node. {@code readiness} is the value to show and use: the hand-entered
 * {@code manualReadiness}, or the linked tree's average when {@code linkedTree} is set. The
 * hand-entered value is still returned while linked so an edit form can keep it.
 */
public record NodeResponse(
		Long id,
		String title,
		String description,
		int readiness,
		int manualReadiness,
		String readinessSourceType,
		TreeRef linkedTree,
		List<Link> links,
		List<String> tags,
		Instant createdAt,
		Instant updatedAt) {

	public record Link(String url, String label) {
	}

	/** @param readiness the node's effective readiness, from a {@code ReadinessContext} */
	static NodeResponse from(Node node, int readiness) {
		return new NodeResponse(node.getId(), node.getTitle(), node.getDescription(), readiness,
				node.getReadiness(), node.getReadinessSourceType().getDbValue(), TreeRef.of(node.getLinkedTree()),
				node.getLinks().stream().map(link -> new Link(link.getUrl(), link.getLabel())).toList(),
				List.copyOf(node.getTags()), node.getCreatedAt(), node.getUpdatedAt());
	}

}
