package com.progressiontracker.node;

/** A node named in another response, such as a node that blocks a tree delete. */
public record NodeRef(Long id, String title) {

	public static NodeRef of(Node node) {
		return new NodeRef(node.getId(), node.getTitle());
	}

}
