package com.progressiontracker.progression.readiness;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progressiontracker.progression.node.Node;
import com.progressiontracker.progression.node.NodeResource;
import com.progressiontracker.progression.node.NodeResourceType;
import com.progressiontracker.progression.tree.Tree;

/**
 * Effective readiness and "last reviewed" for one request. Readiness is worked out when it's
 * read, never stored, and this remembers each node's, tree's and resource's value so a tree
 * or deck that several nodes list is worked out only once. Get one from
 * {@link ReadinessService#context()} and use it inside the service transaction, since it
 * loads trees lazily.
 */
public class ReadinessContext {

	private static final Logger log = LoggerFactory.getLogger(ReadinessContext.class);

	/** Readiness and the latest review beneath, for a node or a tree. */
	private record Standing(int readiness, Instant lastReviewedAt) {
	}

	private final Map<NodeResourceType, ReadinessCalculator> calculators;

	private final Function<Tree, List<Node>> nodesInTree;

	private final Map<Long, Standing> nodes = new HashMap<>();

	private final Map<Long, Standing> trees = new HashMap<>();

	private final Map<String, ResourceStatus> resources = new HashMap<>();

	/** Trees being averaged right now, to stop at a loop instead of recursing forever. */
	private final Set<Long> inProgress = new HashSet<>();

	ReadinessContext(Map<NodeResourceType, ReadinessCalculator> calculators, Function<Tree, List<Node>> nodesInTree) {
		this.calculators = calculators;
		this.nodesInTree = nodesInTree;
	}

	/**
	 * The node's readiness: the average of its resources that count, rounded to a whole
	 * percent, or its hand-entered value when none do.
	 */
	public int of(Node node) {
		return standing(node).readiness();
	}

	/** The latest review beneath the node's resources that count, or null if none. */
	public Instant lastReviewed(Node node) {
		return standing(node).lastReviewedAt();
	}

	/** The average of the tree's nodes' readiness, rounded to a whole percent. An empty tree is 0%. */
	public int ofTree(Tree tree) {
		return standing(tree).readiness();
	}

	/** The latest review beneath any of the tree's nodes, or null if none. */
	public Instant lastReviewedOfTree(Tree tree) {
		return standing(tree).lastReviewedAt();
	}

	/** Where one resource stands, or null for a type that can't count (a URL). */
	public ResourceStatus of(NodeResource resource) {
		ReadinessCalculator calculator = calculators.get(resource.getType());
		if (calculator == null) {
			return null;
		}
		String key = resource.getType() + ":"
				+ (resource.getTree() != null ? resource.getTree().getId() : resource.getDeckId());
		ResourceStatus known = resources.get(key);
		if (known == null) {
			known = calculator.status(resource, this);
			resources.put(key, known);
		}
		return known;
	}

	private Standing standing(Node node) {
		Standing known = nodes.get(node.getId());
		if (known != null) {
			return known;
		}
		List<NodeResource> counting = node.countingResources();
		Standing standing;
		if (counting.isEmpty()) {
			standing = new Standing(node.getReadiness(), null);
		}
		else {
			List<ResourceStatus> statuses = counting.stream().map(this::of).toList();
			standing = new Standing(average(statuses.stream().map(ResourceStatus::readiness).toList()),
					latest(statuses.stream().map(ResourceStatus::lastReviewedAt).toList()));
		}
		nodes.put(node.getId(), standing);
		return standing;
	}

	private Standing standing(Tree tree) {
		Long id = tree.getId();
		Standing known = trees.get(id);
		if (known != null) {
			return known;
		}
		if (!inProgress.add(id)) {
			// The link checks prevent loops, so this only guards against bad data
			log.warn("Tree {} links back to itself; counting it as 0% readiness", id);
			return new Standing(0, null);
		}
		try {
			List<Node> treeNodes = nodesInTree.apply(tree);
			Standing standing = new Standing(average(treeNodes.stream().map(this::of).toList()),
					latest(treeNodes.stream().map(this::lastReviewed).toList()));
			trees.put(id, standing);
			return standing;
		}
		finally {
			inProgress.remove(id);
		}
	}

	/** Rounded to a whole percent; 0 when there's nothing to average. */
	private static int average(Collection<Integer> values) {
		return values.isEmpty() ? 0 : (int) Math.round(values.stream().mapToInt(Integer::intValue).average().orElse(0));
	}

	private static Instant latest(Collection<Instant> times) {
		return times.stream().filter(Objects::nonNull).max(Comparator.naturalOrder()).orElse(null);
	}

}
