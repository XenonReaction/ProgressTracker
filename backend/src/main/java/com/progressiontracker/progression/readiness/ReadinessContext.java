package com.progressiontracker.progression.readiness;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
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
 * Effective readiness, "last reviewed" and "review due" for one request. Readiness is worked out when it's
 * read, never stored, and this remembers each node's, tree's and resource's value so a tree
 * or deck that several nodes list is worked out only once. Get one from
 * {@link ReadinessService#context()} and use it inside the service transaction, since it
 * loads trees lazily. A service that shows many nodes calls {@link #prefetch} or
 * {@link #prefetchTrees} first, so their resources are read in one batch per type rather
 * than one at a time.
 */
public class ReadinessContext {

	private static final Logger log = LoggerFactory.getLogger(ReadinessContext.class);

	/** For a node or a tree: readiness, the latest review beneath, and whether a review is due beneath. */
	private record Standing(int readiness, Instant lastReviewedAt, boolean reviewDue) {
	}

	private final Map<NodeResourceType, ReadinessCalculator> calculators;

	private final Function<Tree, List<Node>> nodesInTree;

	private final Map<Long, Standing> nodes = new HashMap<>();

	private final Map<Long, Standing> trees = new HashMap<>();

	private final Map<String, ResourceStatus> resources = new HashMap<>();

	private final Map<Long, List<Node>> nodesByTree = new HashMap<>();

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

	/** Whether anything beneath the node's resources that count is due for review. */
	public boolean reviewDue(Node node) {
		return standing(node).reviewDue();
	}

	/** The average of the tree's nodes' readiness, rounded to a whole percent. An empty tree is 0%. */
	public int ofTree(Tree tree) {
		return standing(tree).readiness();
	}

	/** The latest review beneath any of the tree's nodes, or null if none. */
	public Instant lastReviewedOfTree(Tree tree) {
		return standing(tree).lastReviewedAt();
	}

	/** Whether any of the tree's nodes has a review due. */
	public boolean reviewDueInTree(Tree tree) {
		return standing(tree).reviewDue();
	}

	/**
	 * Works out, in one batch per resource type, every resource these nodes list and every
	 * resource that counts beneath the trees they list, at any depth, so reading them
	 * afterwards needs no more queries.
	 */
	public void prefetch(Collection<Node> listedNodes) {
		prefetch(listedNodes, true);
	}

	/** Like {@link #prefetch}, for the resources that count beneath these trees. */
	public void prefetchTrees(Collection<Tree> treesToRead) {
		List<Node> treeNodes = new ArrayList<>();
		for (Tree tree : treesToRead) {
			if (!trees.containsKey(tree.getId())) {
				treeNodes.addAll(nodesIn(tree));
			}
		}
		prefetch(treeNodes, false);
	}

	/**
	 * Walks down from the nodes level by level, through the trees their resources list,
	 * collecting the resources not worked out yet, then asks each type's calculator for all of
	 * them at once.
	 *
	 * @param allResources whether to include the nodes' resources that don't count (shown, but
	 * not averaged); beneath them only the resources that count are needed
	 */
	private void prefetch(Collection<Node> startNodes, boolean allResources) {
		Map<NodeResourceType, List<NodeResource>> pending = new EnumMap<>(NodeResourceType.class);
		Set<String> collected = new HashSet<>();
		Set<Long> visitedTrees = new HashSet<>();
		Collection<Node> level = startNodes;
		boolean top = allResources;
		while (!level.isEmpty()) {
			List<Node> next = new ArrayList<>();
			for (Node node : level) {
				for (NodeResource resource : top ? node.getResources() : node.countingResources()) {
					if (resource.getType() == NodeResourceType.TREE) {
						Tree tree = resource.getTree();
						if (!trees.containsKey(tree.getId()) && visitedTrees.add(tree.getId())) {
							next.addAll(nodesIn(tree));
						}
					}
					else if (calculators.containsKey(resource.getType()) && !resources.containsKey(key(resource))
							&& collected.add(key(resource))) {
						pending.computeIfAbsent(resource.getType(), type -> new ArrayList<>()).add(resource);
					}
				}
			}
			level = next;
			top = false;
		}
		pending.forEach((type, list) -> calculators.get(type)
			.statuses(list, this)
			.forEach((targetKey, status) -> resources.put(type + ":" + targetKey, status)));
	}

	/** Where one resource stands, or null for a type that can't count (a URL). */
	public ResourceStatus of(NodeResource resource) {
		ReadinessCalculator calculator = calculators.get(resource.getType());
		if (calculator == null) {
			return null;
		}
		ResourceStatus known = resources.get(key(resource));
		if (known == null) {
			known = calculator.status(resource, this);
			resources.put(key(resource), known);
		}
		return known;
	}

	private static String key(NodeResource resource) {
		return resource.getType() + ":" + resource.targetKey();
	}

	/** The tree's nodes, loaded once per request. */
	private List<Node> nodesIn(Tree tree) {
		return nodesByTree.computeIfAbsent(tree.getId(), id -> nodesInTree.apply(tree));
	}

	private Standing standing(Node node) {
		Standing known = nodes.get(node.getId());
		if (known != null) {
			return known;
		}
		List<NodeResource> counting = node.countingResources();
		Standing standing;
		if (counting.isEmpty()) {
			standing = new Standing(node.getReadiness(), null, false);
		}
		else {
			List<ResourceStatus> statuses = counting.stream().map(this::of).toList();
			standing = new Standing(average(statuses.stream().map(ResourceStatus::readiness).toList()),
					latest(statuses.stream().map(ResourceStatus::lastReviewedAt).toList()),
					statuses.stream().anyMatch(ResourceStatus::reviewDue));
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
			return new Standing(0, null, false);
		}
		try {
			prefetchTrees(List.of(tree));
			List<Node> treeNodes = nodesIn(tree);
			Standing standing = new Standing(average(treeNodes.stream().map(this::of).toList()),
					latest(treeNodes.stream().map(this::lastReviewed).toList()),
					treeNodes.stream().anyMatch(this::reviewDue));
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
