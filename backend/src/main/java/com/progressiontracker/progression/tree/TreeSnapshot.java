package com.progressiontracker.progression.tree;

import java.util.List;

/**
 * A tree as it was when an edit session started: its details, which library nodes were
 * placed where, and its edges. Edges name library nodes rather than tree nodes, so the
 * snapshot can be put back even if a node was removed and placed again (with a new tree node
 * id) during the session.
 */
record TreeSnapshot(
		String title,
		String description,
		String category,
		List<String> tags,
		List<Placement> placements,
		List<Edge> edges) {

	record Placement(Long nodeId, double positionX, double positionY, int aggregateThreshold,
			int individualThreshold) {
	}

	/** A prerequisite edge between two placed library nodes, with its route (null if default). */
	record Edge(Long prerequisiteNodeId, Long dependentNodeId, EdgeRoute route) {
	}

}
