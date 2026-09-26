package com.progressiontracker.lessons;

/**
 * Lets another module refuse to have a lesson deleted while it still refers to it, without the
 * Lessons module depending on that module. Implementations throw (such as a
 * {@code ConflictException}) to refuse; every implementation runs before a lesson is deleted.
 */
public interface LessonDeletionCheck {

	void checkCanDelete(Long lessonId);

}
