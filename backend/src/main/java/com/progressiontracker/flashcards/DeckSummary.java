package com.progressiontracker.flashcards;

/** A deck as other modules see it: its title and where it stands for the current user. */
public record DeckSummary(Long id, String title, DeckProgress progress) {
}
