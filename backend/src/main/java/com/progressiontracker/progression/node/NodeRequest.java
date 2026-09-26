package com.progressiontracker.progression.node;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

/**
 * Body for creating a node or replacing all of its editable fields. {@code readiness} is the
 * hand-entered value: it's used while no resource counts, and kept otherwise.
 * {@code resources} are in the order to show them.
 */
public record NodeRequest(
		@NotBlank @Size(max = 200) String title,
		String description,
		@NotNull @Min(0) @Max(100) Integer readiness,
		List<@Valid @NotNull Resource> resources,
		List<@NotBlank @Size(max = 50) String> tags) {

	/**
	 * One resource: {@code type} {@code "url"} with {@code url}, {@code "tree"} with
	 * {@code treeId}, {@code "deck"} with {@code deckId}, {@code "material"} with
	 * {@code materialId}, {@code "lesson"} with {@code lessonId}, or {@code "question_set"} with
	 * {@code questionSetId}. {@code label} is optional (the
	 * target's title is shown instead). {@code counts} is whether it counts toward readiness;
	 * a URL never does.
	 */
	public record Resource(
			@NotBlank String type,
			@URL @Pattern(regexp = "(?i)https?://.*", message = "must be an http(s) URL") @Size(max = 2048) String url,
			Long treeId,
			Long deckId,
			Long materialId,
			Long lessonId,
			Long questionSetId,
			@Size(max = 200) String label,
			Boolean counts) {

		/** {@code counts} may be omitted; treat that as not counting. */
		public boolean countsOrFalse() {
			return Boolean.TRUE.equals(counts);
		}

	}

	/** {@code resources} may be omitted; treat that as none. */
	public List<Resource> resourcesOrEmpty() {
		return resources == null ? List.of() : resources;
	}

	/** {@code tags} may be omitted; treat that as no tags. */
	public List<String> tagsOrEmpty() {
		return tags == null ? List.of() : tags;
	}

}
