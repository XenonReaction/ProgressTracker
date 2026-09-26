/**
 * The Materials module: external materials (a URL with a title and notes) and the progress
 * the user reports on them, every update kept. Its public API is this package:
 * {@link com.progressiontracker.materials.MaterialReadinessCalculator} and the record it
 * returns, which other modules use to read a material by id, and
 * {@link com.progressiontracker.materials.MaterialDeletionCheck}, which they implement to keep
 * a material they refer to from being deleted. Everything else is in {@code internal}.
 */
package com.progressiontracker.materials;
