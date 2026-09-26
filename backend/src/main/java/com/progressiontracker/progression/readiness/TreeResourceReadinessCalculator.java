package com.progressiontracker.progression.readiness;

import org.springframework.stereotype.Component;

import com.progressiontracker.progression.node.NodeResource;
import com.progressiontracker.progression.node.NodeResourceType;

/**
 * A tree resource: the average readiness of the tree's nodes. Nodes in that tree that take
 * their readiness from trees of their own count with that derived value, so readiness flows
 * up through every level.
 */
@Component
class TreeResourceReadinessCalculator implements ReadinessCalculator {

	@Override
	public NodeResourceType resourceType() {
		return NodeResourceType.TREE;
	}

	@Override
	public int readiness(NodeResource resource, ReadinessContext context) {
		return context.ofTree(resource.getTree());
	}

}
