package com.progressiontracker.tree;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.databind.json.JsonMapper;

import com.progressiontracker.common.ConflictException;
import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.node.Node;
import com.progressiontracker.node.NodeRepository;

/**
 * Edit mode for a tree. Starting a session saves a restore point; changes are still saved as
 * they're made. "Done" ({@link #finish}) keeps them and drops the restore point, and "Discard
 * changes" ({@link #discard}) puts the tree back as it was.
 */
@Service
@Transactional
public class TreeEditSessionService {

	private static final Logger log = LoggerFactory.getLogger(TreeEditSessionService.class);

	private final TreeEditSessionRepository sessions;

	private final TreeService treeService;

	private final TreeNodeRepository treeNodes;

	private final PrerequisiteRepository prerequisites;

	private final NodeRepository nodes;

	private final JsonMapper json;

	public TreeEditSessionService(TreeEditSessionRepository sessions, TreeService treeService,
			TreeNodeRepository treeNodes, PrerequisiteRepository prerequisites, NodeRepository nodes, JsonMapper json) {
		this.sessions = sessions;
		this.treeService = treeService;
		this.treeNodes = treeNodes;
		this.prerequisites = prerequisites;
		this.nodes = nodes;
		this.json = json;
	}

	/** Saves a restore point. A tree already in edit mode is refused with 409. */
	public TreeEditSessionResponse start(Long treeId) {
		Tree tree = treeService.findOwned(treeId);
		if (sessions.existsByTree(tree)) {
			throw new ConflictException("Tree " + treeId + " is already being edited; finish or discard that session first");
		}
		Instant startedAt = Instant.now().truncatedTo(ChronoUnit.MICROS); // the column's precision
		TreeEditSession session = sessions
			.save(new TreeEditSession(tree, startedAt, json.writeValueAsString(snapshotOf(tree))));
		log.info("Edit session started for tree {}", treeId);
		return new TreeEditSessionResponse(session.getStartedAt());
	}

	/** "Done": keeps every change and drops the restore point. */
	public void finish(Long treeId) {
		sessions.delete(findSession(treeService.findOwned(treeId)));
		log.info("Edit session finished for tree {}; changes kept", treeId);
	}

	/**
	 * "Discard changes": puts back the tree's details, placements, thresholds and edges as
	 * they were when the session started, then drops the restore point. Library nodes that
	 * were created during the session and are now in no tree are deleted too. Nodes deleted
	 * from the library in the meantime can't come back, so they're skipped.
	 */
	public void discard(Long treeId) {
		Tree tree = treeService.findOwned(treeId);
		TreeEditSession session = findSession(tree);
		TreeSnapshot snapshot = json.readValue(session.getSnapshot(), TreeSnapshot.class);

		tree.setTitle(snapshot.title());
		tree.setDescription(snapshot.description());
		tree.setCategory(snapshot.category());
		tree.getTags().clear();
		tree.getTags().addAll(snapshot.tags());

		// Edges are rebuilt from the snapshot, so clear them before touching the tree nodes
		prerequisites.deleteAll(prerequisites.findByTree(tree));
		prerequisites.flush();

		Set<Long> snapshotNodeIds = snapshot.placements()
			.stream()
			.map(TreeSnapshot.Placement::nodeId)
			.collect(Collectors.toSet());
		Map<Long, TreeNode> placedByNodeId = new HashMap<>();
		List<Node> createdDuringSession = new ArrayList<>();
		for (TreeNode treeNode : treeNodes.findByTreeOrderByIdAsc(tree)) {
			Node node = treeNode.getNode();
			if (snapshotNodeIds.contains(node.getId())) {
				placedByNodeId.put(node.getId(), treeNode);
				continue;
			}
			if (!node.getCreatedAt().isBefore(session.getStartedAt())) {
				createdDuringSession.add(node);
			}
			treeNodes.delete(treeNode);
		}
		treeNodes.flush();

		for (TreeSnapshot.Placement placement : snapshot.placements()) {
			TreeNode treeNode = placedByNodeId.get(placement.nodeId());
			if (treeNode == null) {
				// Removed during the session: place it again, if it's still in the library
				Node node = nodes.findByIdAndOwner(placement.nodeId(), tree.getOwner()).orElse(null);
				if (node == null) {
					continue;
				}
				treeNode = treeNodes.save(new TreeNode(tree, node, placement.positionX(), placement.positionY()));
				placedByNodeId.put(node.getId(), treeNode);
			}
			treeNode.setPositionX(placement.positionX());
			treeNode.setPositionY(placement.positionY());
			treeNode.setAggregateThreshold(placement.aggregateThreshold());
			treeNode.setIndividualThreshold(placement.individualThreshold());
		}
		treeNodes.flush();

		for (TreeSnapshot.Edge edge : snapshot.edges()) {
			TreeNode prerequisite = placedByNodeId.get(edge.prerequisiteNodeId());
			TreeNode dependent = placedByNodeId.get(edge.dependentNodeId());
			if (prerequisite != null && dependent != null) {
				Prerequisite restored = new Prerequisite(prerequisite, dependent);
				restored.setRoute(edge.route());
				prerequisites.save(restored);
			}
		}

		for (Node node : createdDuringSession) {
			if (!treeNodes.existsByNode(node)) {
				nodes.delete(node);
			}
		}
		sessions.delete(session);
		log.info("Edit session discarded for tree {}; restored its restore point", treeId);
	}

	private TreeEditSession findSession(Tree tree) {
		return sessions.findByTree(tree)
			.orElseThrow(() -> new NotFoundException("Edit session for tree", tree.getId()));
	}

	private TreeSnapshot snapshotOf(Tree tree) {
		List<TreeSnapshot.Placement> placements = treeNodes.findByTreeOrderByIdAsc(tree)
			.stream()
			.map(treeNode -> new TreeSnapshot.Placement(treeNode.getNode().getId(), treeNode.getPositionX(),
					treeNode.getPositionY(), treeNode.getAggregateThreshold(), treeNode.getIndividualThreshold()))
			.toList();
		List<TreeSnapshot.Edge> edges = prerequisites.findByTree(tree)
			.stream()
			.map(edge -> new TreeSnapshot.Edge(edge.getPrerequisite().getNode().getId(),
					edge.getDependent().getNode().getId(), edge.getRoute()))
			.toList();
		return new TreeSnapshot(tree.getTitle(), tree.getDescription(), tree.getCategory(), List.copyOf(tree.getTags()),
				placements, edges);
	}

}
