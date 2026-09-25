package com.progressiontracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.progressiontracker.seed.DevDataSeeder;

/** The prod profile (application-prod.properties): nothing runs with it yet, so it's checked here. */
class ProdProfileTest {

	@Test
	void refusesToStartWithoutTheDatabaseSettingsFromTheEnvironment() {
		assumeTrue(System.getenv("SPRING_DATASOURCE_URL") == null,
				"SPRING_DATASOURCE_URL is set in this environment");

		assertThatThrownBy(() -> new SpringApplicationBuilder(ProgressionTrackerBackendApplication.class)
			.profiles("prod")
			.web(WebApplicationType.NONE)
			.run()
			.close()).hasStackTraceContaining(
					"The prod profile needs these settings from the environment: SPRING_DATASOURCE_URL, "
							+ "SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD");
	}

	/** With the settings supplied (the container's, through @ServiceConnection) it starts. */
	@Nested
	@SpringBootTest(properties = { "SPRING_DATASOURCE_URL=jdbc:postgresql://replaced-by-testcontainers/db",
			"SPRING_DATASOURCE_USERNAME=unused", "SPRING_DATASOURCE_PASSWORD=unused" })
	@ActiveProfiles({ "prod", "dev" })
	@Import(TestcontainersConfiguration.class)
	class WithTheSettings {

		@Autowired
		private ApplicationContext context;

		@Test
		void neverLoadsTheDevSampleDataEvenWithTheDevProfile() {
			assertThat(context.getBeanNamesForType(DevDataSeeder.class)).isEmpty();
		}

	}

}
