package com.progressiontracker.materials.internal;

import java.time.Instant;
import java.util.List;

/**
 * A material with where it stands: {@code progress} is the latest the user reported (0 if
 * none yet), and {@code lastReviewedAt} when. {@code updateCount} is how many updates there
 * are in its history.
 */
public record MaterialResponse(
		Long id,
		String title,
		String url,
		String notes,
		int progress,
		Instant lastReviewedAt,
		int updateCount,
		Instant createdAt,
		Instant updatedAt) {

	/** @param history the material's updates, newest first */
	static MaterialResponse from(Material material, List<MaterialProgressUpdate> history) {
		MaterialProgressUpdate latest = history.isEmpty() ? null : history.get(0);
		return new MaterialResponse(material.getId(), material.getTitle(), material.getUrl(), material.getNotes(),
				latest == null ? 0 : latest.getProgress(), latest == null ? null : latest.getRecordedAt(),
				history.size(), material.getCreatedAt(), material.getUpdatedAt());
	}

}
