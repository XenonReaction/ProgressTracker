package com.progressiontracker.lessons.internal;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.progressiontracker.user.User;

public interface LessonOpenRepository extends JpaRepository<LessonOpen, Long> {

	Optional<LessonOpen> findFirstByLessonAndUserOrderByOpenedAtDescIdDesc(Lesson lesson, User user);

	/** When the user last opened each of these lessons: one row per lesson opened. */
	@Query("""
			select o.lesson.id as lessonId, max(o.openedAt) as openedAt from LessonOpen o
			where o.lesson in :lessons and o.user = :user group by o.lesson.id""")
	List<LastOpened> findLastOpened(Collection<Lesson> lessons, User user);

	interface LastOpened {

		Long getLessonId();

		Instant getOpenedAt();

	}

}
