package com.progressiontracker.node;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

/** Body for creating a node or replacing all of its editable fields. */
public record NodeRequest(
		@NotBlank @Size(max = 200) String title,
		String description,
		@NotNull @Min(0) @Max(100) Integer readiness,
		List<@Valid @NotNull Link> links) {

	public record Link(@NotBlank @URL @Size(max = 2048) String url, @Size(max = 200) String label) {
	}

	/** {@code links} may be omitted; treat that as no links. */
	public List<Link> linksOrEmpty() {
		return links == null ? List.of() : links;
	}

}
