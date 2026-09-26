package com.progressiontracker.progression.node;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.progressiontracker.progression.tree.Tree;

/**
 * Something a node points to: another tree, a flashcard deck, or an external URL. It has an
 * optional label (the target's title is shown when it's null) and says whether it counts
 * toward the node's readiness. A URL never counts. A deck is in another module, so it's
 * referred to by plain id ({@code target_id}), with no foreign key.
 */
@Embeddable
public class NodeResource {

	@Column(name = "resource_type", nullable = false, length = 50)
	private NodeResourceType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tree_id", foreignKey = @ForeignKey(name = "node_resources_tree_id_fk"))
	private Tree tree;

	/** The deck's id, for a {@link NodeResourceType#DECK} resource; otherwise null. */
	@Column(name = "target_id")
	private Long targetId;

	@Column(length = 2048)
	private String url;

	@Column(length = 200)
	private String label;

	@Column(nullable = false)
	private boolean counts;

	protected NodeResource() {
	}

	private NodeResource(NodeResourceType type, Tree tree, Long targetId, String url, String label, boolean counts) {
		this.type = type;
		this.tree = tree;
		this.targetId = targetId;
		this.url = url;
		this.label = label;
		this.counts = counts;
	}

	public static NodeResource url(String url, String label) {
		return new NodeResource(NodeResourceType.URL, null, null, url, label, false);
	}

	public static NodeResource tree(Tree tree, String label, boolean counts) {
		return new NodeResource(NodeResourceType.TREE, tree, null, null, label, counts);
	}

	public static NodeResource deck(Long deckId, String label, boolean counts) {
		return new NodeResource(NodeResourceType.DECK, null, deckId, null, label, counts);
	}

	public NodeResourceType getType() {
		return type;
	}

	/** The tree, for a {@link NodeResourceType#TREE} resource; otherwise null. */
	public Tree getTree() {
		return tree;
	}

	/** The deck's id, for a {@link NodeResourceType#DECK} resource; otherwise null. */
	public Long getDeckId() {
		return type == NodeResourceType.DECK ? targetId : null;
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
