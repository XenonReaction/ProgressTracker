package com.progressiontracker.progression.readiness;

import com.progressiontracker.progression.node.NodeResource;
import com.progressiontracker.progression.node.NodeResourceType;

/**
 * Works out the readiness (0–100) of one kind of node resource. Every
 * {@link NodeResourceType} that can count has exactly one calculator, and
 * {@link ReadinessService} refuses to start if one is missing.
 */
public interface ReadinessCalculator {

	NodeResourceType resourceType();

	/**
	 * @param context gives the readiness of other nodes and trees, for resources that are
	 * derived from them
	 */
	int readiness(NodeResource resource, ReadinessContext context);

}
