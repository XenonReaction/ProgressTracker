package com.progressiontracker.lessons.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.progressiontracker.TestcontainersConfiguration;

@SpringBootTest
@ActiveProfiles("dev")
@Import(TestcontainersConfiguration.class)
class LessonsDevDataSeederTest {

	@Autowired
	private LessonsDevDataSeeder seeder;

	@Autowired
	private LessonService lessons;

	@Test
	void seedsOneLessonWithSectionsAndIsIdempotent() {
		assertSeeded();

		seeder.run(new DefaultApplicationArguments());

		assertSeeded();
	}

	private void assertSeeded() {
		assertThat(lessons.list()).singleElement().satisfies(lesson -> {
			assertThat(lesson.title()).isEqualTo("Flexbox in Ten Minutes");
			assertThat(lesson.sections()).hasSize(3);
			assertThat(lesson.progress()).isEqualTo(30);
			assertThat(lesson.lastReviewedAt()).isNull();
		});
	}

}
