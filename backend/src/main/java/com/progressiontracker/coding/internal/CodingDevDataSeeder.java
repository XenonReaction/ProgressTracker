package com.progressiontracker.coding.internal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.user.CurrentUserService;
import com.progressiontracker.user.User;

/**
 * A sample question set for local development. Runs only with the {@code dev} profile, never
 * with {@code prod}, and does nothing if the default user already has a question set.
 * <p>
 * Contents: "CSS Flexbox exercises", two questions; the first is solved, so the set is at 50%.
 */
@Component
@Profile("dev & !prod")
public class CodingDevDataSeeder implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(CodingDevDataSeeder.class);

	private final CurrentUserService currentUser;

	private final QuestionSetRepository sets;

	private final CodingQuestionRepository questions;

	private final QuestionAttemptRepository attempts;

	public CodingDevDataSeeder(CurrentUserService currentUser, QuestionSetRepository sets,
			CodingQuestionRepository questions, QuestionAttemptRepository attempts) {
		this.currentUser = currentUser;
		this.sets = sets;
		this.questions = questions;
		this.attempts = attempts;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		User user = currentUser.getCurrentUser();
		if (sets.existsByOwner(user)) {
			log.info("User '{}' already has question sets; skipping coding dev seed", user.getUsername());
			return;
		}
		QuestionSet flexbox = new QuestionSet(user, "CSS Flexbox exercises");
		flexbox.setDescription("Small layouts to build with flexbox.");
		sets.save(flexbox);

		CodingQuestion centre = new CodingQuestion(flexbox, "Centre a box", CodingLanguage.CSS,
				"Centre `.box` both ways inside `.frame`, which is `300px` square.",
				".frame {\n  display: flex;\n  justify-content: center;\n  align-items: center;\n}");
		centre.setExamples("```html\n<div class=\"frame\"><div class=\"box\"></div></div>\n```");
		questions.save(centre);
		CodingQuestion nav = new CodingQuestion(flexbox, "Spread a navigation bar", CodingLanguage.CSS,
				"Put the first link on the left and the rest on the right of `nav`.",
				"nav {\n  display: flex;\n  gap: 1rem;\n}\n\nnav a:first-child {\n  margin-right: auto;\n}");
		questions.save(nav);

		attempts.save(new QuestionAttempt(centre, user, Instant.now().minus(3, ChronoUnit.DAYS).truncatedTo(
				ChronoUnit.MICROS), AttemptAction.SOLVED));
		log.info("Seeded coding dev data for user '{}'", user.getUsername());
	}

}
