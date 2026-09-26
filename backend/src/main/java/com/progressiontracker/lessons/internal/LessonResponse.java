package com.progressiontracker.lessons.internal;

import java.time.Instant;
import java.util.List;

/**
 * A lesson with where it stands: {@code progress} is the latest the user entered (0 if none),
 * and {@code lastReviewedAt} when they last opened it.
 */
public record LessonResponse(
		Long id,
		String title,
		String summary,
		List<Section> sections,
		int progress,
		Instant lastReviewedAt,
		Instant createdAt,
		Instant updatedAt) {

	public record Section(String title, String body) {
	}

	static LessonResponse from(Lesson lesson, LessonActivity activity) {
		return new LessonResponse(lesson.getId(), lesson.getTitle(), lesson.getSummary(),
				lesson.getSections().stream().map(s -> new Section(s.getTitle(), s.getBody())).toList(),
				activity.progress(lesson), activity.lastOpened(lesson), lesson.getCreatedAt(), lesson.getUpdatedAt());
	}

}
