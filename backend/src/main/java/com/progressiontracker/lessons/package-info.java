/**
 * The Lessons module: lessons written in the app as ordered Markdown sections, and the user's
 * activity on them (each time they open one, and each progress they report). Its public API
 * is this package: {@link com.progressiontracker.lessons.LessonReadinessCalculator} and the
 * record it returns, which other modules use to read a lesson by id, and
 * {@link com.progressiontracker.lessons.LessonDeletionCheck}, which they implement to keep a
 * lesson they refer to from being deleted. Everything else is in {@code internal}.
 */
package com.progressiontracker.lessons;
