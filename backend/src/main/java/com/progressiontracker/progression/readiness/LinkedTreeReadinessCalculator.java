package com.progressiontracker.progression.readiness;

import org.springframework.stereotype.Component;

import com.progressiontracker.progression.node.Node;
import com.progressiontracker.progression.node.ReadinessSourceType;

/**
 * The average readiness of the nodes in the linked tree. Nodes in that tree that are
 * linked themselves count with their own derived value, so readiness flows up through
 * every level.
 */
@Component
class LinkedTreeReadinessCalculator implements ReadinessCalculator {

	@Override
	public ReadinessSourceType sourceType() {
		return ReadinessSourceType.LINKED_TREE;
	}

	@Override
	public int readiness(Node node, ReadinessContext context) {
		return context.ofTree(node.getLinkedTree());
	}

}
