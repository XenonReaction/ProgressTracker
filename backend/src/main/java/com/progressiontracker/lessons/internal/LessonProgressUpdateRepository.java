package com.progressiontracker.lessons.internal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.progressiontracker.user.User;

public interface LessonProgressUpdateRepository extends JpaRepository<LessonProgressUpdate, Long> {

	Optional<LessonProgressUpdate> findFirstByLessonAndUserOrderByRecordedAtDescIdDesc(Lesson lesson, User user);

}
