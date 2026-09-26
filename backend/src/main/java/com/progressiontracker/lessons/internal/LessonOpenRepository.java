package com.progressiontracker.lessons.internal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.progressiontracker.user.User;

public interface LessonOpenRepository extends JpaRepository<LessonOpen, Long> {

	Optional<LessonOpen> findFirstByLessonAndUserOrderByOpenedAtDescIdDesc(Lesson lesson, User user);

}
