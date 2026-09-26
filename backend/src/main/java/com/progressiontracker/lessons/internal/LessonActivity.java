package com.progressiontracker.lessons.internal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.progressiontracker.user.CurrentUserService;
import com.progressiontracker.user.User;

/**
 * The current user's activity on lessons: recording opens and progress, and reading back the
 * latest of each. Shared by the module's service and its public calculator.
 */
@Component
public class LessonActivity {

	private final LessonOpenRepository opens;

	private final LessonProgressUpdateRepository updates;

	private final CurrentUserService currentUser;

	public LessonActivity(LessonOpenRepository opens, LessonProgressUpdateRepository updates,
			CurrentUserService currentUser) {
		this.opens = opens;
		this.updates = updates;
		this.currentUser = currentUser;
	}

	public User user() {
		return currentUser.getCurrentUser();
	}

	/** The latest progress the user entered, or 0 if none. */
	public int progress(Lesson lesson) {
		return updates.findFirstByLessonAndUserOrderByRecordedAtDescIdDesc(lesson, user())
			.map(LessonProgressUpdate::getProgress)
			.orElse(0);
	}

	/** When the user last opened the lesson, or null if never. */
	public Instant lastOpened(Lesson lesson) {
		return opens.findFirstByLessonAndUserOrderByOpenedAtDescIdDesc(lesson, user())
			.map(LessonOpen::getOpenedAt)
			.orElse(null);
	}

	/** The batch form of {@link #progress(Lesson)}, by lesson id, leaving out lessons with no entry (0). */
	public Map<Long, Integer> progress(Collection<Lesson> lessons, User user) {
		Map<Long, Integer> latest = new HashMap<>();
		for (LessonProgressUpdate update : updates.findByLessonInAndUserOrderByRecordedAtDescIdDesc(lessons, user)) {
			latest.putIfAbsent(update.getLesson().getId(), update.getProgress()); // newest first
		}
		return latest;
	}

	/** The batch form of {@link #lastOpened(Lesson)}, by lesson id, leaving out lessons never opened. */
	public Map<Long, Instant> lastOpened(Collection<Lesson> lessons, User user) {
		Map<Long, Instant> latest = new HashMap<>();
		for (LessonOpenRepository.LastOpened open : opens.findLastOpened(lessons, user)) {
			latest.put(open.getLessonId(), open.getOpenedAt());
		}
		return latest;
	}

	void recordOpen(Lesson lesson) {
		opens.save(new LessonOpen(lesson, user(), now()));
	}

	void recordProgress(Lesson lesson, int progress) {
		updates.save(new LessonProgressUpdate(lesson, user(), now(), progress));
	}

	private static Instant now() {
		return Instant.now().truncatedTo(ChronoUnit.MICROS); // the columns' precision
	}

}
