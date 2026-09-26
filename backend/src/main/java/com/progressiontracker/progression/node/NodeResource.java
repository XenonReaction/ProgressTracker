package com.progressiontracker.progression.node;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.progressiontracker.progression.tree.Tree;

/**
 * Something a node points to: another tree, or an external URL. It has an optional label
 * (the target's title is shown when it's null) and says whether it counts toward the node's
 * readiness. A URL never counts.
 */
@Embeddable
public class NodeResource {

	@Column(name = "resource_type", nullable = false, length = 50)
	private NodeResourceType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tree_id", foreignKey = @ForeignKey(name = "node_resources_tree_id_fk"))
	private Tree tree;

	@Column(length = 2048)
	private String url;

	@Column(length = 200)
	private String label;

	@Column(nullable = false)
	private boolean counts;

	protected NodeResource() {
	}

	private NodeResource(NodeResourceType type, Tree tree, String url, String label, boolean counts) {
		this.type = type;
		this.tree = tree;
		this.url = url;
		this.label = label;
		this.counts = counts;
	}

	public static NodeResource url(String url, String label) {
		return new NodeResource(NodeResourceType.URL, null, url, label, false);
	}

	public static NodeResource tree(Tree tree, String label, boolean counts) {
		return new NodeResource(NodeResourceType.TREE, tree, null, label, counts);
	}

	public NodeResourceType getType() {
		return type;
	}

	/** The tree, for a {@link NodeResourceType#TREE} resource; otherwise null. */
	public Tree getTree() {
		return tree;
	}

	/** The address, for a {@link NodeResourceType#URL} resource; otherwise null. */
	public String getUrl() {
		return url;
	}

	public String getLabel() {
		return label;
	}

	public boolean counts() {
		return counts;
	}

}
