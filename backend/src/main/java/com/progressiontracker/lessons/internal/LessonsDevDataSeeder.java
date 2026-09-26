package com.progressiontracker.lessons.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.user.User;

/**
 * A sample lesson for local development. Runs only with the {@code dev} profile, never with
 * {@code prod}, and does nothing if the default user already has a lesson.
 * <p>
 * Contents: "Flexbox in Ten Minutes", three sections, 30% progress entered and never opened.
 */
@Component
@Profile("dev & !prod")
public class LessonsDevDataSeeder implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(LessonsDevDataSeeder.class);

	private final LessonRepository lessons;

	private final LessonActivity activity;

	public LessonsDevDataSeeder(LessonRepository lessons, LessonActivity activity) {
		this.lessons = lessons;
		this.activity = activity;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		User user = activity.user();
		if (lessons.existsByOwner(user)) {
			log.info("User '{}' already has lessons; skipping lessons dev seed", user.getUsername());
			return;
		}
		Lesson lesson = new Lesson(user, "Flexbox in Ten Minutes");
		lesson.setSummary("The few properties that do most of the work.");
		lesson.getSections().add(new LessonSection("The container",
				"Make an element a flex container with `display: flex`. Its children become **flex items**."));
		lesson.getSections().add(new LessonSection("The axes",
				"`flex-direction` sets the *main axis*:\n\n- `row` (the default)\n- `column`"));
		lesson.getSections().add(new LessonSection("Alignment",
				"`justify-content` aligns items along the main axis, and `align-items` across it."));
		lessons.save(lesson);
		activity.recordProgress(lesson, 30);
		log.info("Seeded lessons dev data for user '{}'", user.getUsername());
	}

}
