package com.progressiontracker.materials.internal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** How far through a material the user is now (0–100), and optionally what they covered. */
public record ProgressUpdateRequest(@NotNull @Min(0) @Max(100) Integer progress, @Size(max = 2000) String note) {
}
