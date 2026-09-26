package com.progressiontracker.lessons;

import java.time.Instant;

/**
 * A lesson as other modules see it.
 *
 * @param progress the latest progress the user entered, 0–100 (0 if none yet)
 * @param lastReviewedAt when the user last opened it, or null if never
 */
public record LessonSummary(Long id, String title, int progress, Instant lastReviewedAt) {
}
