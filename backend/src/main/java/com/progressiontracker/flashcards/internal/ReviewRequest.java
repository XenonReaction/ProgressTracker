package com.progressiontracker.flashcards.internal;

import jakarta.validation.constraints.NotNull;

/** One answer to a card, graded by the user. */
public record ReviewRequest(@NotNull Boolean correct) {
}
