package com.progressiontracker.materials.internal;

import java.time.Instant;

/** One entry in a material's progress history. */
public record ProgressUpdateResponse(Long id, int progress, String note, Instant recordedAt) {

	static ProgressUpdateResponse from(MaterialProgressUpdate update) {
		return new ProgressUpdateResponse(update.getId(), update.getProgress(), update.getNote(), update.getRecordedAt());
	}

}
