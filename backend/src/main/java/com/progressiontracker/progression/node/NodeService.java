package com.progressiontracker.progression.node;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.common.ConflictException;
import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.progression.readiness.ReadinessContext;
import com.progressiontracker.progression.readiness.ReadinessService;
import com.progressiontracker.progression.tree.Tree;
import com.progressiontracker.progression.tree.TreeLinks;
import com.progressiontracker.progression.tree.TreeRef;
import com.progressiontracker.progression.tree.TreeRepository;
import com.progressiontracker.user.CurrentUserService;

/** CRUD for the current user's node library, including linking a node to a tree. */
@Service
@Transactional
public class NodeService {

	private static final Logger log = LoggerFactory.getLogger(NodeService.class);

	private final NodeRepository nodes;

	private final TreeRepository trees;

	private final TreeLinks treeLinks;

	private final ReadinessService readiness;

	private final CurrentUserService currentUser;

	public NodeService(NodeRepository nodes, TreeRepository trees, TreeLinks treeLinks, ReadinessService readiness,
			CurrentUserService currentUser) {
		this.nodes = nodes;
		this.trees = trees;
		this.treeLinks = treeLinks;
		this.readiness = readiness;
		this.currentUser = currentUser;
	}

	@Transactional(readOnly = true)
	public List<NodeResponse> list() {
		ReadinessContext context = readiness.context();
		return nodes.findByOwnerOrderByTitleAsc(currentUser.getCurrentUser())
			.stream()
			.map(node -> NodeResponse.from(node, context.of(node)))
			.toList();
	}

	@Transactional(readOnly = true)
	public NodeResponse get(Long id) {
		return toResponse(findOwned(id));
	}

	public NodeResponse create(NodeRequest request) {
		Node node = new Node(currentUser.getCurrentUser(), request.title());
		apply(request, node);
		return toResponse(nodes.save(node));
	}

	public NodeResponse update(Long id, NodeRequest request) {
		Node node = findOwned(id);
		apply(request, node);
		nodes.flush(); // so the response carries the new updatedAt
		return toResponse(node);
	}

	/**
	 * Sets the hand-entered readiness. A linked node is refused with 409, since its
	 * readiness comes from its tree; it would only change the hidden value.
	 */
	public NodeResponse updateReadiness(Long id, NodeReadinessRequest request) {
		Node node = findOwned(id);
		if (node.getLinkedTree() != null) {
			throw new ConflictException("\"" + node.getTitle() + "\" takes its readiness from the tree \""
					+ node.getLinkedTree().getTitle() + "\", so it can't be set by hand");
		}
		node.setReadiness(request.readiness());
		nodes.flush(); // so the response carries the new updatedAt
		return toResponse(node);
	}

	/** The trees the node is placed in, by title. */
	@Transactional(readOnly = true)
	public List<TreeRef> treesUsing(Long id) {
		return trees.findTreesContaining(findOwned(id)).stream().map(TreeRef::of).toList();
	}

	/**
	 * Refuses to delete a node that any tree still uses, since the node is shared and
	 * deleting it would silently change those trees. The 409 response lists them.
	 */
	public void delete(Long id) {
		Node node = findOwned(id);
		List<Tree> usedIn = trees.findTreesContaining(node);
		if (!usedIn.isEmpty()) {
			throw new ConflictException(
					"Node " + id + " is used in " + usedIn.size() + " tree(s); remove it from them before deleting it",
					Map.of("trees", usedIn.stream().map(TreeRef::of).toList()));
		}
		nodes.delete(node);
		log.info("Deleted node {}", id);
	}

	/** Looks up a node in the current user's library, or throws 404. */
	@Transactional(readOnly = true)
	public Node findOwned(Long id) {
		return nodes.findByIdAndOwner(id, currentUser.getCurrentUser())
			.orElseThrow(() -> new NotFoundException("Node", id));
	}

	private NodeResponse toResponse(Node node) {
		return NodeResponse.from(node, readiness.context().of(node));
	}

	private void apply(NodeRequest request, Node node) {
		node.setTitle(request.title());
		node.setDescription(request.description());
		node.setReadiness(request.readiness());
		node.getLinks().clear();
		request.linksOrEmpty().forEach(link -> node.getLinks().add(new NodeLink(link.url(), link.label())));
		node.getTags().clear();
		node.getTags().addAll(request.tagsOrEmpty());
		node.setLinkedTree(request.linkedTreeId() == null ? null : linkableTree(node, request.linkedTreeId()));
	}

	/** The current user's tree, if linking the node to it wouldn't make a loop (404 or 409). */
	private Tree linkableTree(Node node, Long treeId) {
		Tree tree = trees.findByIdAndOwner(treeId, currentUser.getCurrentUser())
			.orElseThrow(() -> new NotFoundException("Tree", treeId));
		List<Tree> containing = node.getId() == null ? List.of() : trees.findTreesContaining(node);
		treeLinks.checkNoLoop(node, containing, tree);
		return tree;
	}

}
