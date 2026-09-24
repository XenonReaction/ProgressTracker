package com.progressiontracker.node;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Body for updating only a node's hand-entered readiness, as the view pages do. */
public record NodeReadinessRequest(@NotNull @Min(0) @Max(100) Integer readiness) {
}
