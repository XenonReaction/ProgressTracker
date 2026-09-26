package com.progressiontracker.materials;

import java.time.Instant;

/**
 * A material as other modules see it.
 *
 * @param progress the latest progress the user reported, 0–100 (0 if none yet)
 * @param lastReviewedAt when that was reported, or null if never
 */
public record MaterialSummary(Long id, String title, int progress, Instant lastReviewedAt) {
}
