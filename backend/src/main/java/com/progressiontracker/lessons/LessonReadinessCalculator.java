package com.progressiontracker.lessons;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.progressiontracker.lessons.internal.LessonActivity;
import com.progressiontracker.lessons.internal.LessonRepository;

/**
 * How other modules read a lesson: its title, the latest progress the user entered, and when
 * they last opened it. Worked out from the user's activity when asked, never stored. Call it
 * inside a transaction.
 */
@Component
public class LessonReadinessCalculator {

	private final LessonRepository lessons;

	private final LessonActivity activity;

	public LessonReadinessCalculator(LessonRepository lessons, LessonActivity activity) {
		this.lessons = lessons;
		this.activity = activity;
	}

	/** One of the current user's lessons by id. Empty if there's no such lesson or it's someone else's. */
	public Optional<LessonSummary> lesson(Long lessonId) {
		return lessons.findByIdAndOwner(lessonId, activity.user())
			.map(lesson -> new LessonSummary(lesson.getId(), lesson.getTitle(), activity.progress(lesson),
					activity.lastOpened(lesson)));
	}

}
