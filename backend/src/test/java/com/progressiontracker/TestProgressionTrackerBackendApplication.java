package com.progressiontracker;

import org.springframework.boot.SpringApplication;

public class TestProgressionTrackerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProgressionTrackerBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
