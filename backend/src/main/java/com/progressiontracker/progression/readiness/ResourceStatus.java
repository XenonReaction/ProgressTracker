package com.progressiontracker.progression.readiness;

import java.time.Instant;

/**
 * Where one node resource stands.
 *
 * @param title the target's title (a tree's or deck's), shown when the resource has no label
 * @param readiness 0–100
 * @param lastReviewedAt the latest review anywhere beneath it, or null if none
 * @param reviewDue whether anything beneath it is due for review (today only flashcards)
 */
public record ResourceStatus(String title, int readiness, Instant lastReviewedAt, boolean reviewDue) {

	/** For resources that have no review schedule. */
	public ResourceStatus(String title, int readiness, Instant lastReviewedAt) {
		this(title, readiness, lastReviewedAt, false);
	}

}
