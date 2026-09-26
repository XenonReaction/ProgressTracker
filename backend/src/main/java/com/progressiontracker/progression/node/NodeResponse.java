package com.progressiontracker.progression.node;

import java.time.Instant;
import java.util.List;

import com.progressiontracker.progression.tree.TreeRef;

/**
 * A library node. {@code readiness} is the value to show and use: the average of the
 * resources that count, or the hand-entered {@code manualReadiness} when none do. The
 * hand-entered value is always returned so an edit form can keep it.
 */
public record NodeResponse(
		Long id,
		String title,
		String description,
		int readiness,
		int manualReadiness,
		List<Resource> resources,
		List<String> tags,
		Instant createdAt,
		Instant updatedAt) {

	/**
	 * A resource in the node's order. {@code tree} is set for a tree and {@code url} for a
	 * URL. {@code label} is as the user entered it, or null to show the target's title.
	 */
	public record Resource(String type, String url, TreeRef tree, String label, boolean counts) {

		public static Resource of(NodeResource resource) {
			return new Resource(resource.getType().getDbValue(), resource.getUrl(), TreeRef.of(resource.getTree()),
					resource.getLabel(), resource.counts());
		}

		public static List<Resource> of(Node node) {
			return node.getResources().stream().map(Resource::of).toList();
		}

	}

	/** @param readiness the node's effective readiness, from a {@code ReadinessContext} */
	static NodeResponse from(Node node, int readiness) {
		return new NodeResponse(node.getId(), node.getTitle(), node.getDescription(), readiness,
				node.getReadiness(), Resource.of(node), List.copyOf(node.getTags()), node.getCreatedAt(),
				node.getUpdatedAt());
	}

}
