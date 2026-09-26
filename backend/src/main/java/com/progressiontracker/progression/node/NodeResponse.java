package com.progressiontracker.progression.node;

import java.time.Instant;
import java.util.List;

import com.progressiontracker.progression.readiness.ReadinessContext;
import com.progressiontracker.progression.readiness.ResourceStatus;
import com.progressiontracker.progression.tree.TreeRef;

/**
 * A library node. {@code readiness} is the value to show and use: the average of the
 * resources that count, or the hand-entered {@code manualReadiness} when none do. The
 * hand-entered value is always returned so an edit form can keep it. {@code lastReviewedAt}
 * is the latest review beneath the resources that count, or null.
 */
public record NodeResponse(
		Long id,
		String title,
		String description,
		int readiness,
		int manualReadiness,
		Instant lastReviewedAt,
		List<Resource> resources,
		List<String> tags,
		Instant createdAt,
		Instant updatedAt) {

	/**
	 * A resource in the node's order. Exactly one of {@code url}, {@code tree} and
	 * {@code deck} is set, matching {@code type}. {@code label} is as the user entered it, or
	 * null to show the target's title. {@code readiness} and {@code lastReviewedAt} are the
	 * resource's own (null for a URL), whether it counts or not, so a page can show what
	 * each contributes.
	 */
	public record Resource(String type, String url, TreeRef tree, DeckRef deck, String label, boolean counts,
			Integer readiness, Instant lastReviewedAt) {

		static Resource of(NodeResource resource, ReadinessContext context) {
			ResourceStatus status = context.of(resource);
			DeckRef deck = resource.getDeckId() == null ? null
					: new DeckRef(resource.getDeckId(), status == null ? null : status.title());
			return new Resource(resource.getType().getDbValue(), resource.getUrl(), TreeRef.of(resource.getTree()), deck,
					resource.getLabel(), resource.counts(), status == null ? null : status.readiness(),
					status == null ? null : status.lastReviewedAt());
		}

		public static List<Resource> of(Node node, ReadinessContext context) {
			return node.getResources().stream().map(resource -> of(resource, context)).toList();
		}

	}

	static NodeResponse from(Node node, ReadinessContext context) {
		return new NodeResponse(node.getId(), node.getTitle(), node.getDescription(), context.of(node),
				node.getReadiness(), context.lastReviewed(node), Resource.of(node, context), List.copyOf(node.getTags()),
				node.getCreatedAt(), node.getUpdatedAt());
	}

}
