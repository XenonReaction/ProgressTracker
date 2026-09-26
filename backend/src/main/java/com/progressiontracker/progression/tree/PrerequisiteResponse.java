package com.progressiontracker.progression.tree;

/** {@code route} is the hand-adjusted shape, or null for the default route. */
public record PrerequisiteResponse(Long id, Long prerequisiteTreeNodeId, Long dependentTreeNodeId, EdgeRoute route) {

	static PrerequisiteResponse from(Prerequisite edge) {
		return new PrerequisiteResponse(edge.getId(), edge.getPrerequisite().getId(), edge.getDependent().getId(),
				edge.getRoute());
	}

}
