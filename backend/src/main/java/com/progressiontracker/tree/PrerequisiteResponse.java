package com.progressiontracker.tree;

public record PrerequisiteResponse(Long id, Long prerequisiteTreeNodeId, Long dependentTreeNodeId) {

	static PrerequisiteResponse from(Prerequisite edge) {
		return new PrerequisiteResponse(edge.getId(), edge.getPrerequisite().getId(), edge.getDependent().getId());
	}

}
