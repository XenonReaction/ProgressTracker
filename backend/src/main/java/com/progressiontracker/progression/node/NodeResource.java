package com.progressiontracker.progression.node;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.progressiontracker.progression.tree.Tree;

/**
 * Something a node points to: another tree, a flashcard deck, an external material, a lesson,
 * or a plain URL. It has an optional label (the target's title is shown when it's null) and says
 * whether it counts toward the node's readiness. A URL never counts. Decks, materials and
 * lessons are in other modules, so they're referred to by plain id ({@code target_id}), with no foreign
 * key.
 */
@Embeddable
public class NodeResource {

	@Column(name = "resource_type", nullable = false, length = 50)
	private NodeResourceType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tree_id", foreignKey = @ForeignKey(name = "node_resources_tree_id_fk"))
	private Tree tree;

	/** The id of a deck, material or lesson in another module; otherwise null. */
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

	public static NodeResource material(Long materialId, String label, boolean counts) {
		return new NodeResource(NodeResourceType.MATERIAL, null, materialId, null, label, counts);
	}

	public static NodeResource lesson(Long lessonId, String label, boolean counts) {
		return new NodeResource(NodeResourceType.LESSON, null, lessonId, null, label, counts);
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

	/** The material's id, for a {@link NodeResourceType#MATERIAL} resource; otherwise null. */
	public Long getMaterialId() {
		return type == NodeResourceType.MATERIAL ? targetId : null;
	}

	/** The lesson's id, for a {@link NodeResourceType#LESSON} resource; otherwise null. */
	public Long getLessonId() {
		return type == NodeResourceType.LESSON ? targetId : null;
	}

	/** Identifies the target within its type: a tree's id, another module's id, or the URL. */
	public String targetKey() {
		return tree != null ? tree.getId().toString() : targetId != null ? targetId.toString() : url;
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
