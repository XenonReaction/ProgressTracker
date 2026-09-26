package com.progressiontracker.coding.internal;

import java.time.Instant;

import com.progressiontracker.coding.QuestionProgress;

/**
 * A question with where it stands. {@code solution} is null until the user has revealed it
 * (or when the edit form asks for it explicitly); {@code solutionRevealed} says whether they
 * have. {@code revealedBeforeSolved} is recorded for later; today a solved question counts
 * the same either way.
 */
public record QuestionResponse(
		Long id,
		Long setId,
		String setTitle,
		String title,
		String language,
		String problem,
		String examples,
		String solution,
		boolean solved,
		boolean solutionRevealed,
		boolean revealedBeforeSolved,
		Instant lastAttemptAt,
		Instant createdAt,
		Instant updatedAt) {

	/** @param includeSolution send the solution even if the user hasn't revealed it (for editing) */
	static QuestionResponse from(CodingQuestion question, QuestionProgress progress, boolean includeSolution) {
		boolean showSolution = includeSolution || progress.solutionRevealed();
		return new QuestionResponse(question.getId(), question.getSet().getId(), question.getSet().getTitle(),
				question.getTitle(), question.getLanguage().getDbValue(), question.getProblem(), question.getExamples(),
				showSolution ? question.getSolution() : null, progress.solved(), progress.solutionRevealed(),
				progress.revealedBeforeSolved(), progress.lastAttemptAt(), question.getCreatedAt(),
				question.getUpdatedAt());
	}

}
