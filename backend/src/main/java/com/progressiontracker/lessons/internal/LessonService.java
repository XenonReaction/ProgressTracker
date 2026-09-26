package com.progressiontracker.lessons.internal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.lessons.LessonDeletionCheck;

/**
 * CRUD for the current user's lessons, and their activity on them. Reading a lesson through
 * the API isn't a review; the lesson page records an open explicitly. Deleting a lesson
 * deletes its sections and activity, and is refused while a node lists it.
 */
@Service
@Transactional
public class LessonService {

	private static final Logger log = LoggerFactory.getLogger(LessonService.class);

	private final LessonRepository lessons;

	private final LessonActivity activity;

	private final List<LessonDeletionCheck> deletionChecks;

	public LessonService(LessonRepository lessons, LessonActivity activity, List<LessonDeletionCheck> deletionChecks) {
		this.lessons = lessons;
		this.activity = activity;
		this.deletionChecks = deletionChecks;
	}

	@Transactional(readOnly = true)
	public List<LessonResponse> list() {
		return lessons.findByOwnerOrderByTitleAscIdAsc(activity.user())
			.stream()
			.map(lesson -> LessonResponse.from(lesson, activity))
			.toList();
	}

	@Transactional(readOnly = true)
	public LessonResponse get(Long id) {
		return LessonResponse.from(findOwned(id), activity);
	}

	public LessonResponse create(LessonRequest request) {
		Lesson lesson = new Lesson(activity.user(), request.title());
		apply(request, lesson);
		return LessonResponse.from(lessons.save(lesson), activity);
	}

	public LessonResponse update(Long id, LessonRequest request) {
		Lesson lesson = findOwned(id);
		apply(request, lesson);
		lessons.flush(); // so the response carries the new updatedAt
		return LessonResponse.from(lesson, activity);
	}

	/** Refused (by a {@link LessonDeletionCheck}) while another module still refers to the lesson. */
	public void delete(Long id) {
		Lesson lesson = findOwned(id);
		deletionChecks.forEach(check -> check.checkCanDelete(id));
		lessons.delete(lesson);
		log.info("Deleted lesson {}", id);
	}

	/** The user opened the lesson to read it: that's when it was last reviewed. */
	public LessonResponse recordOpen(Long id) {
		Lesson lesson = findOwned(id);
		activity.recordOpen(lesson);
		return LessonResponse.from(lesson, activity);
	}

	/** Records how far through the lesson the user is now; earlier entries are kept. */
	public LessonResponse recordProgress(Long id, LessonProgressRequest request) {
		Lesson lesson = findOwned(id);
		activity.recordProgress(lesson, request.progress());
		return LessonResponse.from(lesson, activity);
	}

	private Lesson findOwned(Long id) {
		return lessons.findByIdAndOwner(id, activity.user()).orElseThrow(() -> new NotFoundException("Lesson", id));
	}

	private static void apply(LessonRequest request, Lesson lesson) {
		lesson.setTitle(request.title());
		lesson.setSummary(request.summary() == null || request.summary().isBlank() ? null : request.summary());
		lesson.getSections().clear();
		request.sectionsOrEmpty()
			.forEach(section -> lesson.getSections().add(new LessonSection(section.title().trim(), section.body())));
	}

}
