/**
 * The Flashcards module: decks, cards and a record of every answer. Its public API is this
 * package: {@link com.progressiontracker.flashcards.FlashcardReadinessCalculator} and the
 * records it returns, which other modules use to read a deck by id, and
 * {@link com.progressiontracker.flashcards.DeckDeletionCheck}, which they implement to keep a
 * deck they refer to from being deleted. Everything else is in {@code internal}, which other
 * modules can't use (see ModularityTest).
 */
package com.progressiontracker.flashcards;
