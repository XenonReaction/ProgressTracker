package com.progressiontracker.progression.node;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.common.BadRequestException;
import com.progressiontracker.common.ConflictException;
import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.flashcards.FlashcardReadinessCalculator;
import com.progressiontracker.progression.readiness.ReadinessContext;
import com.progressiontracker.progression.readiness.ReadinessService;
import com.progressiontracker.progression.tree.Tree;
import com.progressiontracker.progression.tree.TreeLinks;
import com.progressiontracker.progression.tree.TreeRef;
import com.progressiontracker.progression.tree.TreeRepository;
import com.progressiontracker.user.CurrentUserService;

/** CRUD for the current user's node library, including a node's resources. */
@Service
@Transactional
public class NodeService {

	private static final Logger log = LoggerFactory.getLogger(NodeService.class);

	private final NodeRepository nodes;

	private final TreeRepository trees;

	private final TreeLinks treeLinks;

	private final ReadinessService readiness;

	private final CurrentUserService currentUser;

	private final FlashcardReadinessCalculator flashcards;

	public NodeService(NodeRepository nodes, TreeRepository trees, TreeLinks treeLinks, ReadinessService readiness,
			CurrentUserService currentUser, FlashcardReadinessCalculator flashcards) {
		this.nodes = nodes;
		this.trees = trees;
		this.treeLinks = treeLinks;
		this.readiness = readiness;
		this.currentUser = currentUser;
		this.flashcards = flashcards;
	}

	@Transactional(readOnly = true)
	public List<NodeResponse> list() {
		ReadinessContext context = readiness.context();
		return nodes.findByOwnerOrderByTitleAsc(currentUser.getCurrentUser())
			.stream()
			.map(node -> NodeResponse.from(node, context))
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
	 * Sets the hand-entered readiness. A node with a resource that counts is refused with 409,
	 * since its readiness comes from those resources; it would only change the hidden value.
	 */
	public NodeResponse updateReadiness(Long id, NodeReadinessRequest request) {
		Node node = findOwned(id);
		if (!node.countingResources().isEmpty()) {
			throw new ConflictException("\"" + node.getTitle()
					+ "\" takes its readiness from its resources, so it can't be set by hand");
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
		return NodeResponse.from(node, readiness.context());
	}

	private void apply(NodeRequest request, Node node) {
		node.setTitle(request.title());
		node.setDescription(request.description());
		node.setReadiness(request.readiness());
		node.getTags().clear();
		node.getTags().addAll(request.tagsOrEmpty());
		List<NodeResource> resources = request.resourcesOrEmpty().stream().map(this::toResource).toList();
		checkNoTargetTwice(resources);
		node.getResources().clear();
		node.getResources().addAll(resources);
		List<Tree> containing = node.getId() == null ? List.of() : trees.findTreesContaining(node);
		for (Tree counted : node.countingTrees()) {
			treeLinks.checkNoLoop(node, containing, counted);
		}
	}

	/** Checks one requested resource (400, or 404 for a tree that isn't the user's). */
	private NodeResource toResource(NodeRequest.Resource resource) {
		String type = resource.type().trim().toLowerCase();
		if (type.equals(NodeResourceType.URL.getDbValue())) {
			if (resource.url() == null || resource.url().isBlank() || resource.treeId() != null
					|| resource.deckId() != null) {
				throw new BadRequestException("A url resource needs a url and nothing else to point to");
			}
			if (resource.countsOrFalse()) {
				throw new BadRequestException("A url resource is for reading only and can't count toward readiness");
			}
			return NodeResource.url(resource.url(), blankToNull(resource.label()));
		}
		if (type.equals(NodeResourceType.DECK.getDbValue())) {
			if (resource.deckId() == null || resource.url() != null || resource.treeId() != null) {
				throw new BadRequestException("A deck resource needs a deckId and nothing else to point to");
			}
			// The Flashcards module confirms it's one of the user's decks
			flashcards.deck(resource.deckId()).orElseThrow(() -> new NotFoundException("Deck", resource.deckId()));
			return NodeResource.deck(resource.deckId(), blankToNull(resource.label()), resource.countsOrFalse());
		}
		if (type.equals(NodeResourceType.TREE.getDbValue())) {
			if (resource.treeId() == null || resource.url() != null || resource.deckId() != null) {
				throw new BadRequestException("A tree resource needs a treeId and nothing else to point to");
			}
			Tree tree = trees.findByIdAndOwner(resource.treeId(), currentUser.getCurrentUser())
				.orElseThrow(() -> new NotFoundException("Tree", resource.treeId()));
			return NodeResource.tree(tree, blankToNull(resource.label()), resource.countsOrFalse());
		}
		throw new BadRequestException("Unknown resource type \"" + resource.type() + "\"; expected url, tree or deck");
	}

	/** A tree or deck listed twice would count twice, so it's refused. */
	private static void checkNoTargetTwice(List<NodeResource> resources) {
		Set<Long> trees = new HashSet<>();
		Set<Long> decks = new HashSet<>();
		for (NodeResource resource : resources) {
			if (resource.getTree() != null && !trees.add(resource.getTree().getId())) {
				throw new BadRequestException("Tree " + resource.getTree().getId() + " is listed more than once");
			}
			if (resource.getDeckId() != null && !decks.add(resource.getDeckId())) {
				throw new BadRequestException("Deck " + resource.getDeckId() + " is listed more than once");
			}
		}
	}

	private static String blankToNull(String text) {
		return text == null || text.isBlank() ? null : text.trim();
	}

}
