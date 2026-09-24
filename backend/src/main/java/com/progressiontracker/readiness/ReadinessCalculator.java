package com.progressiontracker.readiness;

import com.progressiontracker.node.Node;
import com.progressiontracker.node.ReadinessSourceType;

/**
 * Works out a node's readiness (0–100) for one {@link ReadinessSourceType}. There's one
 * calculator per source type; a new source type adds a calculator, and
 * {@link ReadinessService} refuses to start if any type is missing one.
 */
public interface ReadinessCalculator {

	ReadinessSourceType sourceType();

	/**
	 * @param context gives the readiness of other nodes and trees, for sources that are
	 * derived from them
	 */
	int readiness(Node node, ReadinessContext context);

}
