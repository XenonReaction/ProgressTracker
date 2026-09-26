package com.progressiontracker.coding.internal;

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
class CodingDevDataSeederTest {

	@Autowired
	private CodingDevDataSeeder seeder;

	@Autowired
	private QuestionSetService sets;

	@Test
	void seedsOneSetWithOneOfTwoQuestionsSolvedAndIsIdempotent() {
		assertSeeded();

		seeder.run(new DefaultApplicationArguments());

		assertSeeded();
	}

	private void assertSeeded() {
		assertThat(sets.list()).singleElement().satisfies(set -> {
			assertThat(set.title()).isEqualTo("CSS Flexbox exercises");
			assertThat(set.questionCount()).isEqualTo(2);
			assertThat(set.solvedCount()).isEqualTo(1);
			assertThat(set.readiness()).isEqualTo(50);
		});
	}

}
