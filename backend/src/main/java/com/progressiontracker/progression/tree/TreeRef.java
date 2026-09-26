package com.progressiontracker.progression.tree;

/** A tree named in another response: a node's linked tree, or a tree that blocks a delete. */
public record TreeRef(Long id, String title) {

	public static TreeRef of(Tree tree) {
		return tree == null ? null : new TreeRef(tree.getId(), tree.getTitle());
	}

}
