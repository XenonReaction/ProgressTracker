package com.progressiontracker.flashcards;

/**
 * Lets another module refuse to have a deck deleted while it still refers to it, without the
 * Flashcards module depending on that module. Implementations throw (such as a
 * {@code ConflictException}) to refuse; every implementation runs before a deck is deleted.
 */
public interface DeckDeletionCheck {

	void checkCanDelete(Long deckId);

}
