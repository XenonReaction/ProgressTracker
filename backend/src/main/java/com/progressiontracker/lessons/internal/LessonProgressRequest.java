package com.progressiontracker.lessons.internal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** How far through a lesson the user is now (0–100). */
public record LessonProgressRequest(@NotNull @Min(0) @Max(100) Integer progress) {
}
