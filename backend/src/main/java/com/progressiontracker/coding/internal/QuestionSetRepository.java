package com.progressiontracker.coding.internal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.progressiontracker.user.User;

public interface QuestionSetRepository extends JpaRepository<QuestionSet, Long> {

	List<QuestionSet> findByOwnerOrderByTitleAscIdAsc(User owner);

	Optional<QuestionSet> findByIdAndOwner(Long id, User owner);

	boolean existsByOwner(User owner);

}
