package com.progressiontracker.progression.readiness;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progressiontracker.progression.node.Node;
import com.progressiontracker.progression.node.NodeResource;
import com.progressiontracker.progression.node.NodeResourceType;
import com.progressiontracker.progression.tree.Tree;

/**
 * Effective readiness for one request. Readiness is worked out when it's read, never
 * stored, and this remembers each node's and tree's value so a tree that several nodes
 * link to is averaged only once. Get one from {@link ReadinessService#context()} and use it
 * inside the service transaction, since it loads linked trees lazily.
 */
public class ReadinessContext {

	private static final Logger log = LoggerFactory.getLogger(ReadinessContext.class);

	private final Map<NodeResourceType, ReadinessCalculator> calculators;

	private final Function<Tree, List<Node>> nodesInTree;

	private final Map<Long, Integer> nodeReadiness = new HashMap<>();

	private final Map<Long, Integer> treeReadiness = new HashMap<>();

	/** Trees being averaged right now, to stop at a loop instead of recursing forever. */
	private final Set<Long> inProgress = new HashSet<>();

	ReadinessContext(Map<NodeResourceType, ReadinessCalculator> calculators,
			Function<Tree, List<Node>> nodesInTree) {
		this.calculators = calculators;
		this.nodesInTree = nodesInTree;
	}

	/**
	 * The node's readiness: the average of its resources that count, rounded to a whole
	 * percent, or its hand-entered value when none do.
	 */
	public int of(Node node) {
		Integer known = nodeReadiness.get(node.getId());
		if (known != null) {
			return known;
		}
		List<NodeResource> counting = node.countingResources();
		int readiness = counting.isEmpty() ? node.getReadiness()
				: (int) Math.round(counting.stream().mapToInt(this::of).average().orElse(0));
		nodeReadiness.put(node.getId(), readiness);
		return readiness;
	}

	/** One resource's readiness, from the calculator for its type. */
	public int of(NodeResource resource) {
		return calculators.get(resource.getType()).readiness(resource, this);
	}

	/**
	 * The average of the tree's nodes' readiness, rounded to a whole percent. An empty tree
	 * is 0%.
	 */
	public int ofTree(Tree tree) {
		Long id = tree.getId();
		Integer known = treeReadiness.get(id);
		if (known != null) {
			return known;
		}
		if (!inProgress.add(id)) {
			// The link checks prevent loops, so this only guards against bad data
			log.warn("Tree {} links back to itself; counting it as 0% readiness", id);
			return 0;
		}
		try {
			List<Node> nodes = nodesInTree.apply(tree);
			int readiness = nodes.isEmpty() ? 0
					: (int) Math.round(nodes.stream().mapToInt(this::of).average().orElse(0));
			treeReadiness.put(id, readiness);
			return readiness;
		}
		finally {
			inProgress.remove(id);
		}
	}

}
