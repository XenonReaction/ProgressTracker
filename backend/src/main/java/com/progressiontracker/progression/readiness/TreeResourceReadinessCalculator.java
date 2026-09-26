package com.progressiontracker.progression.readiness;

import org.springframework.stereotype.Component;

import com.progressiontracker.progression.node.NodeResource;
import com.progressiontracker.progression.node.NodeResourceType;

/**
 * A tree resource: the average readiness of the tree's nodes, the latest review of any of
 * them, and whether any of them has a review due. Nodes in that tree that take their
 * readiness from resources of their own count with that derived value, so readiness flows up
 * through every level.
 */
@Component
class TreeResourceReadinessCalculator implements ReadinessCalculator {

	@Override
	public NodeResourceType resourceType() {
		return NodeResourceType.TREE;
	}

	@Override
	public ResourceStatus status(NodeResource resource, ReadinessContext context) {
		return new ResourceStatus(resource.getTree().getTitle(), context.ofTree(resource.getTree()),
				context.lastReviewedOfTree(resource.getTree()), context.reviewDueInTree(resource.getTree()));
	}

}
