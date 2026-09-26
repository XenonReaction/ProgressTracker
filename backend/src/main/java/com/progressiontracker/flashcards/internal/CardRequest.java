package com.progressiontracker.flashcards.internal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Body for creating a card or replacing its text. Editing a card keeps its answers. */
public record CardRequest(@NotBlank @Size(max = 5000) String front, @NotBlank @Size(max = 5000) String back) {
}
