package com.progressiontracker.tree;

import java.util.List;

/**
 * A node as placed in a tree. {@code id} is the tree node's id (used in tree URLs and
 * edges); {@code nodeId} is the library node's id. Title and readiness are copied from the
 * library node so a tree can be rendered from this one response. {@code prerequisiteIds}
 * and {@code dependentIds} are tree node ids: what this node needs and what it unlocks.
 */
public record TreeNodeResponse(
		Long id,
		Long treeId,
		Long nodeId,
		String title,
		int readiness,
		double positionX,
		double positionY,
		int aggregateThreshold,
		int individualThreshold,
		List<Long> prerequisiteIds,
		List<Long> dependentIds) {

	static TreeNodeResponse from(TreeNode treeNode, List<Prerequisite> treeEdges) {
		Long id = treeNode.getId();
		return new TreeNodeResponse(id, treeNode.getTree().getId(), treeNode.getNode().getId(),
				treeNode.getNode().getTitle(), treeNode.getNode().getReadiness(), treeNode.getPositionX(),
				treeNode.getPositionY(), treeNode.getAggregateThreshold(), treeNode.getIndividualThreshold(),
				treeEdges.stream()
					.filter(edge -> edge.getDependent().getId().equals(id))
					.map(edge -> edge.getPrerequisite().getId())
					.toList(),
				treeEdges.stream()
					.filter(edge -> edge.getPrerequisite().getId().equals(id))
					.map(edge -> edge.getDependent().getId())
					.toList());
	}

}
