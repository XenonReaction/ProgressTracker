package com.progressiontracker.coding.internal;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.progressiontracker.user.User;

public interface QuestionAttemptRepository extends JpaRepository<QuestionAttempt, Long> {

	/** The user's attempts on these questions, oldest first. */
	List<QuestionAttempt> findByUserAndQuestionInOrderByRecordedAtAscIdAsc(User user,
			Collection<CodingQuestion> questions);

}
