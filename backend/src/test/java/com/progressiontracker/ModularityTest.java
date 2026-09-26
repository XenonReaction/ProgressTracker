package com.progressiontracker;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

/**
 * Each top-level package is a module. A module may use only the top-level package of another
 * (its public API), and no two modules may depend on each other in a circle.
 */
class ModularityTest {

	private final ApplicationModules modules = ApplicationModules.of(ProgressionTrackerBackendApplication.class);

	@Test
	void modulesRespectTheirBoundaries() {
		modules.verify();
	}

}
