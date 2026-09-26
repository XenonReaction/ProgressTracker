package com.progressiontracker.coding.internal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Body for creating a question set or replacing its details. Questions are added separately. */
public record QuestionSetRequest(@NotBlank @Size(max = 200) String title, String description) {
}
