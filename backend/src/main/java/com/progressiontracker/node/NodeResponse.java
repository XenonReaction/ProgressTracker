package com.progressiontracker.node;

import java.time.Instant;
import java.util.List;

public record NodeResponse(
		Long id,
		String title,
		String description,
		int readiness,
		String readinessSourceType,
		List<Link> links,
		Instant createdAt,
		Instant updatedAt) {

	public record Link(String url, String label) {
	}

	static NodeResponse from(Node node) {
		return new NodeResponse(node.getId(), node.getTitle(), node.getDescription(), node.getReadiness(),
				node.getReadinessSourceType().getDbValue(),
				node.getLinks().stream().map(link -> new Link(link.getUrl(), link.getLabel())).toList(),
				node.getCreatedAt(), node.getUpdatedAt());
	}

}
