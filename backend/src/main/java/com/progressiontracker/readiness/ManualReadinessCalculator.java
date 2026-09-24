package com.progressiontracker.readiness;

import org.springframework.stereotype.Component;

import com.progressiontracker.node.Node;
import com.progressiontracker.node.ReadinessSourceType;

/** The value the user entered by hand. */
@Component
class ManualReadinessCalculator implements ReadinessCalculator {

	@Override
	public ReadinessSourceType sourceType() {
		return ReadinessSourceType.MANUAL;
	}

	@Override
	public int readiness(Node node, ReadinessContext context) {
		return node.getReadiness();
	}

}
