package com.progressiontracker.materials.internal;

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
 * A sample material for local development. Runs only with the {@code dev} profile, never with
 * {@code prod}, and does nothing if the default user already has a material.
 * <p>
 * Contents: "A Complete Guide to Flexbox", reported at 40% two weeks ago and 60% a week ago.
 */
@Component
@Profile("dev & !prod")
public class MaterialsDevDataSeeder implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(MaterialsDevDataSeeder.class);

	private final CurrentUserService currentUser;

	private final MaterialRepository materials;

	private final MaterialProgressUpdateRepository updates;

	public MaterialsDevDataSeeder(CurrentUserService currentUser, MaterialRepository materials,
			MaterialProgressUpdateRepository updates) {
		this.currentUser = currentUser;
		this.materials = materials;
		this.updates = updates;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		User user = currentUser.getCurrentUser();
		if (materials.existsByOwner(user)) {
			log.info("User '{}' already has materials; skipping materials dev seed", user.getUsername());
			return;
		}
		Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
		Material guide = new Material(user, "A Complete Guide to Flexbox",
				"https://css-tricks.com/snippets/css/a-guide-to-flexbox/");
		guide.setNotes("The container properties first, then the items.");
		materials.save(guide);
		updates.save(new MaterialProgressUpdate(guide, user, now.minus(14, ChronoUnit.DAYS), 40, "Container properties"));
		updates.save(new MaterialProgressUpdate(guide, user, now.minus(7, ChronoUnit.DAYS), 60, "Started on the items"));
		log.info("Seeded materials dev data for user '{}'", user.getUsername());
	}

}
