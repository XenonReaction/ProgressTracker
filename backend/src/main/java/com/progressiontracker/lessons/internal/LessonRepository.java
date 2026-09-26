package com.progressiontracker.lessons.internal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.progressiontracker.user.User;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

	@EntityGraph(attributePaths = "sections")
	List<Lesson> findByOwnerOrderByTitleAscIdAsc(User owner);

	Optional<Lesson> findByIdAndOwner(Long id, User owner);

	boolean existsByOwner(User owner);

}
