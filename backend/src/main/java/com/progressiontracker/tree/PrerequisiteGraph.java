package com.progressiontracker.tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** The prerequisite edges of one tree, used to reject edges that would create a cycle. */
final class PrerequisiteGraph {

	record Edge(Long prerequisiteId, Long dependentId) {
	}

	private final Map<Long, List<Long>> dependentsByPrerequisite = new HashMap<>();

	private PrerequisiteGraph(Collection<Edge> edges) {
		edges.forEach(edge -> dependentsByPrerequisite.computeIfAbsent(edge.prerequisiteId(), id -> new ArrayList<>())
			.add(edge.dependentId()));
	}

	static PrerequisiteGraph of(Collection<Edge> edges) {
		return new PrerequisiteGraph(edges);
	}

	/**
	 * Adding {@code prerequisite -> dependent} closes a cycle exactly when
	 * {@code prerequisite} is already reachable from {@code dependent}.
	 */
	boolean wouldCreateCycle(Long prerequisiteId, Long dependentId) {
		Deque<Long> toVisit = new ArrayDeque<>(List.of(dependentId));
		Set<Long> visited = new HashSet<>();
		while (!toVisit.isEmpty()) {
			Long current = toVisit.pop();
			if (current.equals(prerequisiteId)) {
				return true;
			}
			if (visited.add(current)) {
				toVisit.addAll(dependentsByPrerequisite.getOrDefault(current, List.of()));
			}
		}
		return false;
	}

}
