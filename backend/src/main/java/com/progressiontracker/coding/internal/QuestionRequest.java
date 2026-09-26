package com.progressiontracker.coding.internal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Body for creating a question or replacing it. {@code language} is {@code "html"} or
 * {@code "css"}. {@code problem} and {@code examples} are Markdown; {@code solution} is shown
 * as written.
 */
public record QuestionRequest(
		@NotBlank @Size(max = 200) String title,
		@NotBlank String language,
		@NotBlank @Size(max = 50000) String problem,
		@Size(max = 50000) String examples,
		@NotBlank @Size(max = 50000) String solution) {
}
