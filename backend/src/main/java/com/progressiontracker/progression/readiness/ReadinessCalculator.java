package com.progressiontracker.progression.readiness;

import com.progressiontracker.progression.node.NodeResource;
import com.progressiontracker.progression.node.NodeResourceType;

/**
 * Works out where one kind of node resource stands: its title, readiness (0–100) and when
 * it was last reviewed. Every {@link NodeResourceType} that can count has exactly one
 * calculator, and {@link ReadinessService} refuses to start if one is missing. A calculator
 * for another module's resource asks that module through its public API.
 */
public interface ReadinessCalculator {

	NodeResourceType resourceType();

	/**
	 * @param context gives the readiness of other nodes and trees, for resources that are
	 * derived from them
	 */
	ResourceStatus status(NodeResource resource, ReadinessContext context);

}
