package com.progressiontracker.tree;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Body for creating a tree or replacing its metadata. Contents are managed separately. */
public record TreeRequest(
		@NotBlank @Size(max = 200) String title,
		String description,
		@Size(max = 100) String category,
		List<@NotBlank @Size(max = 50) String> tags) {

	/** {@code tags} may be omitted; treat that as no tags. */
	public List<String> tagsOrEmpty() {
		return tags == null ? List.of() : tags;
	}

}
