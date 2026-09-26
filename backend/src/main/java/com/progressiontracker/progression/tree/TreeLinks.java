package com.progressiontracker.progression.tree;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.progressiontracker.common.ConflictException;
import com.progressiontracker.progression.node.Node;

/**
 * Keeps links between trees free of loops. Tree A "links to" tree B when a node placed in A
 * takes readiness from B (a tree resource that counts). A loop would make a tree's readiness
 * depend on itself, so a change that would close one is refused with 409. Both saving a
 * node's resources and placing a node in a tree can close a loop, so both check here, once
 * per tree the node counts. A tree resource that doesn't count can't make a loop.
 */
@Component
public class TreeLinks {

	private static final Logger log = LoggerFactory.getLogger(TreeLinks.class);

	private final TreeNodeRepository treeNodes;

	public TreeLinks(TreeNodeRepository treeNodes) {
		this.treeNodes = treeNodes;
	}

	/**
	 * Throws 409 if {@code node}, placed in the {@code containing} trees, can't take its
	 * readiness from {@code linkedTree}: that is, if following links from
	 * {@code linkedTree} (itself included) reaches one of the containing trees.
	 */
	public void checkNoLoop(Node node, Collection<Tree> containing, Tree linkedTree) {
		Set<Long> reachable = reachableFrom(linkedTree.getId());
		for (Tree tree : containing) {
			if (tree.getId().equals(linkedTree.getId())) {
				log.info("Refused link: node {} to tree {}, which contains it", node.getId(), tree.getId());
				throw new ConflictException(
						"\"" + node.getTitle() + "\" can't take its readiness from \"" + tree.getTitle()
								+ "\" because it's in that tree");
			}
			if (reachable.contains(tree.getId())) {
				log.info("Refused link: node {} to tree {}, whose links lead back to tree {}", node.getId(),
						linkedTree.getId(), tree.getId());
				throw new ConflictException("\"" + node.getTitle() + "\" can't take its readiness from \""
						+ linkedTree.getTitle() + "\": that tree's links lead back to \"" + tree.getTitle()
						+ "\", which contains it, so its readiness would depend on itself");
			}
		}
	}

	/** Every tree reachable by following links from {@code start}, including {@code start}. */
	Set<Long> reachableFrom(Long start) {
		Set<Long> seen = new LinkedHashSet<>();
		Deque<Long> queue = new ArrayDeque<>();
		seen.add(start);
		queue.add(start);
		while (!queue.isEmpty()) {
			for (Long next : treeNodes.findLinkedTreeIds(queue.remove())) {
				if (seen.add(next)) {
					queue.add(next);
				}
			}
		}
		return seen;
	}

}
