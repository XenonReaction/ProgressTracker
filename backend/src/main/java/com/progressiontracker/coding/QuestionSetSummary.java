package com.progressiontracker.coding;

/** A question set as other modules see it: its title and where it stands for the current user. */
public record QuestionSetSummary(Long id, String title, SetProgress progress) {
}
