package com.progressiontracker.progression.tree;

import java.util.List;

import com.progressiontracker.progression.node.NodeResponse;
import com.progressiontracker.progression.readiness.ReadinessContext;

/**
 * A node as placed in a tree. {@code id} is the tree node's id (used in tree URLs and
 * edges); {@code nodeId} is the library node's id. Title, description, resources and
 * readiness are copied from the library node so a tree can be rendered from this one
 * response; {@code readiness} is the effective value (derived when a resource counts).
 * {@code prerequisiteIds} and {@code dependentIds} are tree node ids: what this node needs
 * and what it unlocks.
 */
public record TreeNodeResponse(
		Long id,
		Long treeId,
		Long nodeId,
		String title,
		String description,
		List<NodeResponse.Resource> resources,
		int readiness,
		double positionX,
		double positionY,
		int aggregateThreshold,
		int individualThreshold,
		List<Long> prerequisiteIds,
		List<Long> dependentIds) {

	static TreeNodeResponse from(TreeNode treeNode, List<Prerequisite> treeEdges, ReadinessContext readiness) {
		Long id = treeNode.getId();
		return new TreeNodeResponse(id, treeNode.getTree().getId(), treeNode.getNode().getId(),
				treeNode.getNode().getTitle(), treeNode.getNode().getDescription(),
				NodeResponse.Resource.of(treeNode.getNode()), readiness.of(treeNode.getNode()), treeNode.getPositionX(),
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
