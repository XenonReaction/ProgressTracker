package com.progressiontracker.common;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The clock that decides "now" for anything that depends on it, such as when a flashcard is
 * due. Tests replace it to move time.
 */
@Configuration
public class ClockConfiguration {

	@Bean
	Clock clock() {
		return Clock.systemUTC();
	}

}
