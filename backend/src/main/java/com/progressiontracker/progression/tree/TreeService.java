package com.progressiontracker.progression.tree;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.common.ConflictException;
import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.progression.node.Node;
import com.progressiontracker.progression.node.NodeRef;
import com.progressiontracker.progression.node.NodeRepository;
import com.progressiontracker.user.CurrentUserService;
import com.progressiontracker.user.User;

/** CRUD for the current user's trees (metadata only; see TreeNodeService for contents). */
@Service
@Transactional
public class TreeService {

	private static final Logger log = LoggerFactory.getLogger(TreeService.class);

	private final TreeRepository trees;

	private final NodeRepository nodes;

	private final TreeEditSessionRepository editSessions;

	private final CurrentUserService currentUser;

	public TreeService(TreeRepository trees, NodeRepository nodes, TreeEditSessionRepository editSessions,
			CurrentUserService currentUser) {
		this.trees = trees;
		this.nodes = nodes;
		this.editSessions = editSessions;
		this.currentUser = currentUser;
	}

	@Transactional(readOnly = true)
	public List<TreeResponse> list() {
		User owner = currentUser.getCurrentUser();
		Map<Long, Instant> editing = editSessions.findByOwner(owner)
			.stream()
			.collect(Collectors.toMap(session -> session.getTree().getId(), TreeEditSession::getStartedAt));
		return trees.findByOwnerOrderByTitleAsc(owner)
			.stream()
			.map(tree -> TreeResponse.from(tree, editing.get(tree.getId())))
			.toList();
	}

	@Transactional(readOnly = true)
	public TreeResponse get(Long id) {
		return toResponse(findOwned(id));
	}

	public TreeResponse create(TreeRequest request) {
		Tree tree = new Tree(currentUser.getCurrentUser(), request.title());
		apply(request, tree);
		return TreeResponse.from(trees.save(tree), null);
	}

	public TreeResponse update(Long id, TreeRequest request) {
		Tree tree = findOwned(id);
		apply(request, tree);
		trees.flush(); // so the response carries the new updatedAt
		return toResponse(tree);
	}

	/**
	 * Deletes the tree with its tree nodes and edges (database cascade). Library nodes stay.
	 * A tree that nodes list as a resource is refused with 409, listing those nodes.
	 */
	public void delete(Long id) {
		Tree tree = findOwned(id);
		List<Node> linking = nodes.findLinkingTo(tree);
		if (!linking.isEmpty()) {
			String titles = linking.stream().map(node -> "\"" + node.getTitle() + "\"").collect(Collectors.joining(", "));
			throw new ConflictException(
					"Tree " + id + " is linked from " + linking.size() + " node(s): " + titles
							+ ". Unlink them before deleting it",
					Map.of("nodes", linking.stream().map(NodeRef::of).toList()));
		}
		trees.delete(tree);
		log.info("Deleted tree {}", id);
	}

	/** Looks up one of the current user's trees, or throws 404. */
	@Transactional(readOnly = true)
	public Tree findOwned(Long id) {
		return trees.findByIdAndOwner(id, currentUser.getCurrentUser())
			.orElseThrow(() -> new NotFoundException("Tree", id));
	}

	private TreeResponse toResponse(Tree tree) {
		return TreeResponse.from(tree, editSessions.findByTree(tree).map(TreeEditSession::getStartedAt).orElse(null));
	}

	private static void apply(TreeRequest request, Tree tree) {
		tree.setTitle(request.title());
		tree.setDescription(request.description());
		tree.setCategory(request.category());
		tree.getTags().clear();
		tree.getTags().addAll(request.tagsOrEmpty());
	}

}
