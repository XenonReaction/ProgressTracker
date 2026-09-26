package com.progressiontracker.coding.internal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.coding.QuestionProgress;
import com.progressiontracker.coding.QuestionSetReadinessCalculator;
import com.progressiontracker.common.BadRequestException;
import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.user.CurrentUserService;

/** A question set's questions, and the user's attempts on them: revealing a solution and marking solved. */
@Service
@Transactional
public class CodingQuestionService {

	private static final Logger log = LoggerFactory.getLogger(CodingQuestionService.class);

	private final CodingQuestionRepository questions;

	private final QuestionAttemptRepository attempts;

	private final QuestionSetService setService;

	private final QuestionSetReadinessCalculator readiness;

	private final CurrentUserService currentUser;

	public CodingQuestionService(CodingQuestionRepository questions, QuestionAttemptRepository attempts,
			QuestionSetService setService, QuestionSetReadinessCalculator readiness, CurrentUserService currentUser) {
		this.questions = questions;
		this.attempts = attempts;
		this.setService = setService;
		this.readiness = readiness;
		this.currentUser = currentUser;
	}

	/** The set's questions, solutions hidden unless revealed. */
	@Transactional(readOnly = true)
	public List<QuestionResponse> list(Long setId) {
		List<CodingQuestion> inSet = questions.findBySetOrderByIdAsc(setService.findOwned(setId));
		Map<Long, QuestionProgress> progress = readiness.questions(inSet);
		return inSet.stream().map(q -> QuestionResponse.from(q, progress.get(q.getId()), false)).toList();
	}

	/**
	 * One question. The solution is hidden unless revealed, or {@code includeSolution} asks for
	 * it (the edit form); either way, reading isn't recorded.
	 */
	@Transactional(readOnly = true)
	public QuestionResponse get(Long setId, Long questionId, boolean includeSolution) {
		return toResponse(findInSet(setId, questionId), includeSolution);
	}

	public QuestionResponse create(Long setId, QuestionRequest request) {
		CodingQuestion question = new CodingQuestion(setService.findOwned(setId), request.title(),
				languageOf(request), request.problem(), request.solution());
		question.setExamples(blankToNull(request.examples()));
		return toResponse(questions.save(question), true);
	}

	public QuestionResponse update(Long setId, Long questionId, QuestionRequest request) {
		CodingQuestion question = findInSet(setId, questionId);
		question.setTitle(request.title());
		question.setLanguage(languageOf(request));
		question.setProblem(request.problem());
		question.setExamples(blankToNull(request.examples()));
		question.setSolution(request.solution());
		questions.flush(); // so the response carries the new updatedAt
		return toResponse(question, true);
	}

	public void delete(Long setId, Long questionId) {
		questions.delete(findInSet(setId, questionId));
		log.info("Deleted coding question {} from set {}", questionId, setId);
	}

	/** Shows the solution, recording that the user asked to see it. */
	public QuestionResponse reveal(Long setId, Long questionId) {
		return record(findInSet(setId, questionId), AttemptAction.REVEALED);
	}

	/** Marks the question solved (the user solved it on their own machine). */
	public QuestionResponse markSolved(Long setId, Long questionId) {
		return record(findInSet(setId, questionId), AttemptAction.SOLVED);
	}

	private QuestionResponse record(CodingQuestion question, AttemptAction action) {
		Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS); // the column's precision
		attempts.save(new QuestionAttempt(question, currentUser.getCurrentUser(), now, action));
		return toResponse(question, false);
	}

	private CodingQuestion findInSet(Long setId, Long questionId) {
		return questions.findByIdAndSet(questionId, setService.findOwned(setId))
			.orElseThrow(() -> new NotFoundException("Question", questionId));
	}

	private QuestionResponse toResponse(CodingQuestion question, boolean includeSolution) {
		return QuestionResponse.from(question, readiness.questions(List.of(question)).get(question.getId()),
				includeSolution);
	}

	private static CodingLanguage languageOf(QuestionRequest request) {
		CodingLanguage language = CodingLanguage.fromDbValue(request.language().trim().toLowerCase());
		if (language == null) {
			throw new BadRequestException("Unknown language \"" + request.language() + "\"; expected html or css");
		}
		return language;
	}

	private static String blankToNull(String text) {
		return text == null || text.isBlank() ? null : text;
	}

}
