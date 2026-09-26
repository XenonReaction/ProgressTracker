package com.progressiontracker.progression.tree;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.common.BadRequestException;
import com.progressiontracker.common.ConflictException;
import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.progression.node.Node;
import com.progressiontracker.progression.node.NodeService;
import com.progressiontracker.progression.readiness.ReadinessContext;
import com.progressiontracker.progression.readiness.ReadinessService;

/** Places library nodes in a tree, moves them, sets their thresholds and removes them. */
@Service
@Transactional
public class TreeNodeService {

	private final TreeNodeRepository treeNodes;

	private final PrerequisiteRepository prerequisites;

	private final TreeService treeService;

	private final NodeService nodeService;

	private final TreeLinks treeLinks;

	private final ReadinessService readiness;

	public TreeNodeService(TreeNodeRepository treeNodes, PrerequisiteRepository prerequisites,
			TreeService treeService, NodeService nodeService, TreeLinks treeLinks, ReadinessService readiness) {
		this.treeNodes = treeNodes;
		this.prerequisites = prerequisites;
		this.treeService = treeService;
		this.nodeService = nodeService;
		this.treeLinks = treeLinks;
		this.readiness = readiness;
	}

	@Transactional(readOnly = true)
	public List<TreeNodeResponse> list(Long treeId) {
		Tree tree = treeService.findOwned(treeId);
		List<Prerequisite> edges = prerequisites.findByTree(tree);
		List<TreeNode> placed = treeNodes.findByTreeOrderByIdAsc(tree);
		ReadinessContext context = readiness.context();
		context.prefetch(placed.stream().map(TreeNode::getNode).toList());
		return placed.stream().map(treeNode -> TreeNodeResponse.from(treeNode, edges, context)).toList();
	}

	@Transactional(readOnly = true)
	public TreeNodeResponse get(Long treeId, Long treeNodeId) {
		Tree tree = treeService.findOwned(treeId);
		return TreeNodeResponse.from(findInTree(tree, treeNodeId), prerequisites.findByTree(tree),
				readiness.context());
	}

	public TreeNodeResponse add(Long treeId, TreeNodeCreateRequest request) {
		Tree tree = treeService.findOwned(treeId);
		Node node = nodeService.findOwned(request.nodeId());
		if (treeNodes.existsByTreeAndNode(tree, node)) {
			throw new ConflictException("Node " + node.getId() + " is already in tree " + treeId);
		}
		for (Tree counted : node.countingTrees()) {
			treeLinks.checkNoLoop(node, List.of(tree), counted);
		}
		TreeNode treeNode = new TreeNode(tree, node, request.positionX(), request.positionY());
		if (request.aggregateThreshold() != null) {
			treeNode.setAggregateThreshold(request.aggregateThreshold());
		}
		if (request.individualThreshold() != null) {
			treeNode.setIndividualThreshold(request.individualThreshold());
		}
		return TreeNodeResponse.from(treeNodes.save(treeNode), List.of(), readiness.context());
	}

	public TreeNodeResponse update(Long treeId, Long treeNodeId, TreeNodeUpdateRequest request) {
		Tree tree = treeService.findOwned(treeId);
		TreeNode treeNode = findInTree(tree, treeNodeId);
		treeNode.setPositionX(request.positionX());
		treeNode.setPositionY(request.positionY());
		treeNode.setAggregateThreshold(request.aggregateThreshold());
		treeNode.setIndividualThreshold(request.individualThreshold());
		return TreeNodeResponse.from(treeNode, prerequisites.findByTree(tree), readiness.context());
	}

	/**
	 * Moves several tree nodes in one transaction. Every id is checked before anything
	 * changes, so an unknown id (404) or a repeated id (400) leaves the whole tree as it was.
	 */
	public List<TreeNodeResponse> updatePositions(Long treeId, TreeLayoutRequest request) {
		Tree tree = treeService.findOwned(treeId);
		Set<Long> seen = new HashSet<>();
		for (TreeLayoutRequest.Position position : request.positions()) {
			if (!seen.add(position.treeNodeId())) {
				throw new BadRequestException("Tree node " + position.treeNodeId() + " is listed more than once");
			}
		}
		List<TreeNode> all = treeNodes.findByTreeOrderByIdAsc(tree);
		Map<Long, TreeNode> byId = all.stream().collect(Collectors.toMap(TreeNode::getId, Function.identity()));
		for (TreeLayoutRequest.Position position : request.positions()) {
			if (!byId.containsKey(position.treeNodeId())) {
				throw new NotFoundException("Tree node", position.treeNodeId());
			}
		}
		for (TreeLayoutRequest.Position position : request.positions()) {
			TreeNode treeNode = byId.get(position.treeNodeId());
			treeNode.setPositionX(position.positionX());
			treeNode.setPositionY(position.positionY());
		}
		List<Prerequisite> edges = prerequisites.findByTree(tree);
		ReadinessContext context = readiness.context();
		return all.stream().map(treeNode -> TreeNodeResponse.from(treeNode, edges, context)).toList();
	}

	/** Removes the node from this tree along with its edges. The library node stays. */
	public void remove(Long treeId, Long treeNodeId) {
		Tree tree = treeService.findOwned(treeId);
		treeNodes.delete(findInTree(tree, treeNodeId));
	}

	/** Looks up a tree node that belongs to the given tree, or throws 404. */
	TreeNode findInTree(Tree tree, Long treeNodeId) {
		return treeNodes.findByIdAndTree(treeNodeId, tree)
			.orElseThrow(() -> new NotFoundException("Tree node", treeNodeId));
	}

}
