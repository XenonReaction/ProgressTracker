package com.progressiontracker.lessons.internal;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Body for creating a lesson or replacing its title, summary and sections (in order). */
public record LessonRequest(
		@NotBlank @Size(max = 200) String title,
		String summary,
		List<@Valid @NotNull Section> sections) {

	/** A section: a heading and a Markdown body. */
	public record Section(@NotBlank @Size(max = 200) String title, @NotBlank @Size(max = 50000) String body) {
	}

	/** {@code sections} may be omitted; treat that as none. */
	public List<Section> sectionsOrEmpty() {
		return sections == null ? List.of() : sections;
	}

}
