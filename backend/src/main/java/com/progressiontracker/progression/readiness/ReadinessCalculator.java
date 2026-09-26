package com.progressiontracker.progression.readiness;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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

	/**
	 * The batch form of {@link #status}, by {@link NodeResource#targetKey()}, used by
	 * {@link ReadinessContext#prefetch} so a page reads each module a fixed number of times
	 * instead of once per resource. The default asks {@link #status} for each.
	 */
	default Map<String, ResourceStatus> statuses(Collection<NodeResource> resources, ReadinessContext context) {
		return byTargetKey(resources, resource -> status(resource, context));
	}

	/** Each resource's status, by target, working out a target listed twice only once. */
	static Map<String, ResourceStatus> byTargetKey(Collection<NodeResource> resources,
			Function<NodeResource, ResourceStatus> status) {
		Map<String, ResourceStatus> statuses = new HashMap<>();
		for (NodeResource resource : resources) {
			statuses.computeIfAbsent(resource.targetKey(), key -> status.apply(resource));
		}
		return statuses;
	}

}
