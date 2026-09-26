package com.progressiontracker.materials.internal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

/** Body for creating a material or replacing its details. Editing them isn't a review. */
public record MaterialRequest(
		@NotBlank @Size(max = 200) String title,
		@NotBlank @URL @Pattern(regexp = "(?i)https?://.*", message = "must be an http(s) URL") @Size(max = 2048) String url,
		String notes) {
}
