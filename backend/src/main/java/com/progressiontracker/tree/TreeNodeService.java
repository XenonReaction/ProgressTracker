package com.progressiontracker.tree;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.common.ConflictException;
import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.node.Node;
import com.progressiontracker.node.NodeService;

/** Places library nodes in a tree, moves them, sets their thresholds and removes them. */
@Service
@Transactional
public class TreeNodeService {

	private final TreeNodeRepository treeNodes;

	private final PrerequisiteRepository prerequisites;

	private final TreeService treeService;

	private final NodeService nodeService;

	public TreeNodeService(TreeNodeRepository treeNodes, PrerequisiteRepository prerequisites,
			TreeService treeService, NodeService nodeService) {
		this.treeNodes = treeNodes;
		this.prerequisites = prerequisites;
		this.treeService = treeService;
		this.nodeService = nodeService;
	}

	@Transactional(readOnly = true)
	public List<TreeNodeResponse> list(Long treeId) {
		Tree tree = treeService.findOwned(treeId);
		List<Prerequisite> edges = prerequisites.findByTree(tree);
		return treeNodes.findByTreeOrderByIdAsc(tree)
			.stream()
			.map(treeNode -> TreeNodeResponse.from(treeNode, edges))
			.toList();
	}

	@Transactional(readOnly = true)
	public TreeNodeResponse get(Long treeId, Long treeNodeId) {
		Tree tree = treeService.findOwned(treeId);
		return TreeNodeResponse.from(findInTree(tree, treeNodeId), prerequisites.findByTree(tree));
	}

	public TreeNodeResponse add(Long treeId, TreeNodeCreateRequest request) {
		Tree tree = treeService.findOwned(treeId);
		Node node = nodeService.findOwned(request.nodeId());
		if (treeNodes.existsByTreeAndNode(tree, node)) {
			throw new ConflictException("Node " + node.getId() + " is already in tree " + treeId);
		}
		TreeNode treeNode = new TreeNode(tree, node, request.positionX(), request.positionY());
		if (request.aggregateThreshold() != null) {
			treeNode.setAggregateThreshold(request.aggregateThreshold());
		}
		if (request.individualThreshold() != null) {
			treeNode.setIndividualThreshold(request.individualThreshold());
		}
		return TreeNodeResponse.from(treeNodes.save(treeNode), List.of());
	}

	public TreeNodeResponse update(Long treeId, Long treeNodeId, TreeNodeUpdateRequest request) {
		Tree tree = treeService.findOwned(treeId);
		TreeNode treeNode = findInTree(tree, treeNodeId);
		treeNode.setPositionX(request.positionX());
		treeNode.setPositionY(request.positionY());
		treeNode.setAggregateThreshold(request.aggregateThreshold());
		treeNode.setIndividualThreshold(request.individualThreshold());
		return TreeNodeResponse.from(treeNode, prerequisites.findByTree(tree));
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
