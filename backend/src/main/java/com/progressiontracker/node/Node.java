package com.progressiontracker.node;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CheckConstraint;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.progressiontracker.tree.Tree;
import com.progressiontracker.user.User;

/**
 * A skill in a user's node library. Nodes exist independently of trees; a tree places
 * library nodes via {@link com.progressiontracker.tree.TreeNode}, so the same node can
 * appear in several trees. Position and prerequisites live on the tree side, not here.
 */
@Entity
@Table(name = "nodes", check = {
		@CheckConstraint(name = "nodes_readiness_range", constraint = "readiness between 0 and 100"),
		@CheckConstraint(name = "nodes_readiness_source_type_check",
				constraint = "readiness_source_type in ('manual', 'linked_tree')"),
		@CheckConstraint(name = "nodes_linked_tree_matches_source",
				constraint = "(readiness_source_type = 'linked_tree') = (linked_tree_id is not null)") })
public class Node {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "nodes_user_id_fk"))
	private User owner;

	@Column(nullable = false, length = 200)
	private String title;

	@Column(columnDefinition = "text")
	private String description;

	/** The hand-entered value. It's kept, but not used, while the node is linked to a tree. */
	@Column(nullable = false)
	private int readiness = 0;

	@Column(nullable = false, length = 50)
	private ReadinessSourceType readinessSourceType = ReadinessSourceType.MANUAL;

	/** The tree this node's readiness comes from, or null for a hand-entered value. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "linked_tree_id", foreignKey = @ForeignKey(name = "nodes_linked_tree_id_fk"))
	private Tree linkedTree;

	@ElementCollection
	@CollectionTable(name = "node_links", joinColumns = @JoinColumn(name = "node_id"),
			foreignKey = @ForeignKey(name = "node_links_node_id_fk"))
	@OrderColumn(name = "position")
	private List<NodeLink> links = new ArrayList<>();

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private Instant updatedAt;

	protected Node() {
	}

	public Node(User owner, String title) {
		this.owner = owner;
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public User getOwner() {
		return owner;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getReadiness() {
		return readiness;
	}

	public void setReadiness(int readiness) {
		this.readiness = readiness;
	}

	public ReadinessSourceType getReadinessSourceType() {
		return readinessSourceType;
	}

	public Tree getLinkedTree() {
		return linkedTree;
	}

	/** Links the node to a tree, or back to its hand-entered value with {@code null}. */
	public void setLinkedTree(Tree linkedTree) {
		this.linkedTree = linkedTree;
		this.readinessSourceType = linkedTree == null ? ReadinessSourceType.MANUAL : ReadinessSourceType.LINKED_TREE;
	}

	public List<NodeLink> getLinks() {
		return links;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

}
