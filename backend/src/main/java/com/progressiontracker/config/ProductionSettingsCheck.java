package com.progressiontracker.config;

import java.util.List;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * With the {@code prod} profile, refuses to start unless the database settings are supplied
 * from outside (usually as environment variables), so production never falls back to the
 * local development login in application.properties. It runs before any bean is created, so
 * the error names what's missing instead of a connection failure.
 */
@Component
@Profile("prod")
class ProductionSettingsCheck implements BeanFactoryPostProcessor, EnvironmentAware {

	static final List<String> REQUIRED = List.of("SPRING_DATASOURCE_URL", "SPRING_DATASOURCE_USERNAME",
			"SPRING_DATASOURCE_PASSWORD");

	private Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		List<String> missing = REQUIRED.stream().filter(name -> !environment.containsProperty(name)).toList();
		if (!missing.isEmpty()) {
			throw new IllegalStateException(
					"The prod profile needs these settings from the environment: " + String.join(", ", missing));
		}
	}

}
