package com.progressiontracker.tree;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Places a library node in a tree. Omitted thresholds use the 80/70 defaults. */
public record TreeNodeCreateRequest(
		@NotNull Long nodeId,
		@NotNull Double positionX,
		@NotNull Double positionY,
		@Min(0) @Max(100) Integer aggregateThreshold,
		@Min(0) @Max(100) Integer individualThreshold) {
}
