package com.progressiontracker.tree;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.common.BadRequestException;
import com.progressiontracker.common.ConflictException;
import com.progressiontracker.common.NotFoundException;

/** Adds and removes prerequisite edges, keeping each tree's graph acyclic. */
@Service
@Transactional
public class PrerequisiteService {

	private final PrerequisiteRepository prerequisites;

	private final TreeService treeService;

	private final TreeNodeService treeNodeService;

	public PrerequisiteService(PrerequisiteRepository prerequisites, TreeService treeService,
			TreeNodeService treeNodeService) {
		this.prerequisites = prerequisites;
		this.treeService = treeService;
		this.treeNodeService = treeNodeService;
	}

	@Transactional(readOnly = true)
	public List<PrerequisiteResponse> list(Long treeId) {
		return prerequisites.findByTree(treeService.findOwned(treeId))
			.stream()
			.map(PrerequisiteResponse::from)
			.toList();
	}

	/**
	 * Both ends must be tree nodes of this tree (otherwise 404), distinct (400), not already
	 * linked (409), and the new edge must not close a cycle (409).
	 */
	public PrerequisiteResponse create(Long treeId, PrerequisiteRequest request) {
		if (request.prerequisiteTreeNodeId().equals(request.dependentTreeNodeId())) {
			throw new BadRequestException("A node cannot be its own prerequisite");
		}
		Tree tree = treeService.findOwned(treeId);
		TreeNode prerequisite = treeNodeService.findInTree(tree, request.prerequisiteTreeNodeId());
		TreeNode dependent = treeNodeService.findInTree(tree, request.dependentTreeNodeId());
		if (prerequisites.existsByPrerequisiteAndDependent(prerequisite, dependent)) {
			throw new ConflictException("Tree node " + prerequisite.getId() + " is already a prerequisite of tree node "
					+ dependent.getId());
		}
		List<PrerequisiteGraph.Edge> edges = prerequisites.findByTree(tree)
			.stream()
			.map(edge -> new PrerequisiteGraph.Edge(edge.getPrerequisite().getId(), edge.getDependent().getId()))
			.toList();
		if (PrerequisiteGraph.of(edges).wouldCreateCycle(prerequisite.getId(), dependent.getId())) {
			throw new ConflictException("Making tree node " + prerequisite.getId() + " a prerequisite of tree node "
					+ dependent.getId() + " would create a cycle");
		}
		return PrerequisiteResponse.from(prerequisites.save(new Prerequisite(prerequisite, dependent)));
	}

	public void delete(Long treeId, Long prerequisiteId) {
		Tree tree = treeService.findOwned(treeId);
		Prerequisite edge = prerequisites.findByIdAndDependentTree(prerequisiteId, tree)
			.orElseThrow(() -> new NotFoundException("Prerequisite", prerequisiteId));
		prerequisites.delete(edge);
	}

}
