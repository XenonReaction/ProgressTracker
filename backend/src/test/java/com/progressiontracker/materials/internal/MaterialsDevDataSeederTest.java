package com.progressiontracker.materials.internal;

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
class MaterialsDevDataSeederTest {

	@Autowired
	private MaterialsDevDataSeeder seeder;

	@Autowired
	private MaterialService materials;

	@Test
	void seedsOneMaterialWithTwoUpdatesAndIsIdempotent() {
		assertSeeded();

		seeder.run(new DefaultApplicationArguments());

		assertSeeded();
	}

	private void assertSeeded() {
		assertThat(materials.list()).singleElement().satisfies(material -> {
			assertThat(material.title()).isEqualTo("A Complete Guide to Flexbox");
			assertThat(material.progress()).isEqualTo(60);
			assertThat(material.updateCount()).isEqualTo(2);
		});
	}

}
