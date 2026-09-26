package com.progressiontracker.coding;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.progressiontracker.coding.internal.AttemptAction;
import com.progressiontracker.coding.internal.CodingQuestion;
import com.progressiontracker.coding.internal.CodingQuestionRepository;
import com.progressiontracker.coding.internal.QuestionAttempt;
import com.progressiontracker.coding.internal.QuestionAttemptRepository;
import com.progressiontracker.coding.internal.QuestionSet;
import com.progressiontracker.coding.internal.QuestionSetRepository;
import com.progressiontracker.user.CurrentUserService;
import com.progressiontracker.user.User;

/**
 * Readiness and "last reviewed" for coding questions and their sets, worked out from the
 * current user's attempts when asked, never stored. The rules are in {@link SetProgress}.
 * Call it inside a transaction.
 */
@Component
public class QuestionSetReadinessCalculator {

	private final QuestionSetRepository sets;

	private final CodingQuestionRepository questions;

	private final QuestionAttemptRepository attempts;

	private final CurrentUserService currentUser;

	public QuestionSetReadinessCalculator(QuestionSetRepository sets, CodingQuestionRepository questions,
			QuestionAttemptRepository attempts, CurrentUserService currentUser) {
		this.sets = sets;
		this.questions = questions;
		this.attempts = attempts;
		this.currentUser = currentUser;
	}

	/** One of the current user's question sets by id. Empty if there's no such set or it's someone else's. */
	public Optional<QuestionSetSummary> questionSet(Long setId) {
		return sets.findByIdAndOwner(setId, currentUser.getCurrentUser())
			.map(set -> new QuestionSetSummary(set.getId(), set.getTitle(), set(set)));
	}

	/**
	 * The batch form of {@link #questionSet(Long)}: the current user's question sets among
	 * these ids, by id, in a fixed number of queries. Ids that aren't the user's sets are left
	 * out.
	 */
	public Map<Long, QuestionSetSummary> questionSets(Collection<Long> setIds) {
		if (setIds.isEmpty()) {
			return Map.of();
		}
		List<QuestionSet> found = sets.findByIdInAndOwner(setIds, currentUser.getCurrentUser());
		Map<Long, SetProgress> progress = sets(found);
		Map<Long, QuestionSetSummary> summaries = new HashMap<>();
		for (QuestionSet set : found) {
			summaries.put(set.getId(), new QuestionSetSummary(set.getId(), set.getTitle(), progress.get(set.getId())));
		}
		return summaries;
	}

	public SetProgress set(QuestionSet set) {
		return sets(List.of(set)).get(set.getId());
	}

	/** Each set's progress, by set id. */
	public Map<Long, SetProgress> sets(Collection<QuestionSet> setsToCheck) {
		if (setsToCheck.isEmpty()) {
			return Map.of();
		}
		List<CodingQuestion> all = questions.findBySetInOrderByIdAsc(setsToCheck);
		Map<Long, QuestionProgress> progress = questions(all);
		Map<Long, List<QuestionProgress>> bySet = new HashMap<>();
		for (CodingQuestion question : all) {
			bySet.computeIfAbsent(question.getSet().getId(), id -> new ArrayList<>()).add(progress.get(question.getId()));
		}
		Map<Long, SetProgress> result = new LinkedHashMap<>();
		for (QuestionSet set : setsToCheck) {
			result.put(set.getId(), SetProgress.of(bySet.getOrDefault(set.getId(), List.of())));
		}
		return result;
	}

	/** Each question's progress, by question id. */
	public Map<Long, QuestionProgress> questions(Collection<CodingQuestion> questionsToCheck) {
		if (questionsToCheck.isEmpty()) {
			return Map.of();
		}
		User user = currentUser.getCurrentUser();
		Map<Long, List<QuestionAttempt>> byQuestion = new HashMap<>();
		for (QuestionAttempt attempt : attempts.findByUserAndQuestionInOrderByRecordedAtAscIdAsc(user,
				questionsToCheck)) {
			byQuestion.computeIfAbsent(attempt.getQuestion().getId(), id -> new ArrayList<>()).add(attempt);
		}
		Map<Long, QuestionProgress> result = new LinkedHashMap<>();
		for (CodingQuestion question : questionsToCheck) {
			result.put(question.getId(), progressOf(byQuestion.getOrDefault(question.getId(), List.of())));
		}
		return result;
	}

	/** @param oldestFirst one question's attempts */
	private static QuestionProgress progressOf(List<QuestionAttempt> oldestFirst) {
		if (oldestFirst.isEmpty()) {
			return QuestionProgress.UNTOUCHED;
		}
		boolean revealed = false;
		boolean solved = false;
		boolean revealedFirst = false;
		for (QuestionAttempt attempt : oldestFirst) {
			if (attempt.getAction() == AttemptAction.REVEALED) {
				revealed = true;
			}
			else if (!solved) {
				solved = true;
				revealedFirst = revealed;
			}
		}
		Instant last = oldestFirst.get(oldestFirst.size() - 1).getRecordedAt();
		return new QuestionProgress(solved, revealed, revealedFirst, last);
	}

}
