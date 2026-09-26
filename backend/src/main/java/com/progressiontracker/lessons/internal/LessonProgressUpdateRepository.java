package com.progressiontracker.lessons.internal;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.progressiontracker.user.User;

public interface LessonProgressUpdateRepository extends JpaRepository<LessonProgressUpdate, Long> {

	Optional<LessonProgressUpdate> findFirstByLessonAndUserOrderByRecordedAtDescIdDesc(Lesson lesson, User user);

	/** The user's progress entries on these lessons, newest first. */
	List<LessonProgressUpdate> findByLessonInAndUserOrderByRecordedAtDescIdDesc(Collection<Lesson> lessons, User user);

}
