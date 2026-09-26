package com.progressiontracker.progression.tree;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Replaces a tree node's position and thresholds. Which library node it is can't change. */
public record TreeNodeUpdateRequest(
		@NotNull Double positionX,
		@NotNull Double positionY,
		@NotNull @Min(0) @Max(100) Integer aggregateThreshold,
		@NotNull @Min(0) @Max(100) Integer individualThreshold) {
}
