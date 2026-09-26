package com.progressiontracker.lessons;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.progressiontracker.lessons.internal.Lesson;
import com.progressiontracker.lessons.internal.LessonActivity;
import com.progressiontracker.lessons.internal.LessonRepository;
import com.progressiontracker.user.User;

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

	/**
	 * The batch form of {@link #lesson(Long)}: the current user's lessons among these ids, by
	 * id, in a fixed number of queries. Ids that aren't the user's lessons are left out.
	 */
	public Map<Long, LessonSummary> lessons(Collection<Long> lessonIds) {
		if (lessonIds.isEmpty()) {
			return Map.of();
		}
		User user = activity.user();
		List<Lesson> found = lessons.findByIdInAndOwner(lessonIds, user);
		if (found.isEmpty()) {
			return Map.of();
		}
		Map<Long, Integer> progress = activity.progress(found, user);
		Map<Long, Instant> lastOpened = activity.lastOpened(found, user);
		Map<Long, LessonSummary> summaries = new HashMap<>();
		for (Lesson lesson : found) {
			summaries.put(lesson.getId(), new LessonSummary(lesson.getId(), lesson.getTitle(),
					progress.getOrDefault(lesson.getId(), 0), lastOpened.get(lesson.getId())));
		}
		return summaries;
	}

}
