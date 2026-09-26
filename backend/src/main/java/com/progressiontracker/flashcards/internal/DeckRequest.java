package com.progressiontracker.flashcards.internal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Body for creating a deck or replacing its details. Cards are added separately. */
public record DeckRequest(@NotBlank @Size(max = 200) String title, String description) {
}
