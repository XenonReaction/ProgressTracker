package com.progressiontracker.coding.internal;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CodingQuestionRepository extends JpaRepository<CodingQuestion, Long> {

	List<CodingQuestion> findBySetOrderByIdAsc(QuestionSet set);

	List<CodingQuestion> findBySetInOrderByIdAsc(Collection<QuestionSet> sets);

	Optional<CodingQuestion> findByIdAndSet(Long id, QuestionSet set);

}
