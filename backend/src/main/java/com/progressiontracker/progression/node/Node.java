package com.progressiontracker.progression.node;

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

import com.progressiontracker.progression.tree.Tree;
import com.progressiontracker.user.User;

/**
 * A skill in a user's node library. Nodes exist independently of trees; a tree places
 * library nodes via {@link com.progressiontracker.progression.tree.TreeNode}, so the same node can
 * appear in several trees. Position and prerequisites live on the tree side, not here.
 */
@Entity
@Table(name = "nodes",
		check = @CheckConstraint(name = "nodes_readiness_range", constraint = "readiness between 0 and 100"))
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

	/**
	 * The hand-entered value. It's used only while no resource counts; otherwise it's kept for
	 * when none does again.
	 */
	@Column(nullable = false)
	private int readiness = 0;

	/**
	 * In the order the user chose. {@code @CollectionTable} can't declare check constraints, so
	 * they're only in the migrations (V7, widened in V8): node_resources_resource_type_check
	 * (the allowed types), node_resources_target_matches_type (each type has exactly its own
	 * target column set) and node_resources_url_never_counts.
	 */
	@ElementCollection
	@CollectionTable(name = "node_resources", joinColumns = @JoinColumn(name = "node_id"),
			foreignKey = @ForeignKey(name = "node_resources_node_id_fk"))
	@OrderColumn(name = "position")
	private List<NodeResource> resources = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "node_tags", joinColumns = @JoinColumn(name = "node_id"),
			foreignKey = @ForeignKey(name = "node_tags_node_id_fk"))
	@OrderColumn(name = "position")
	@Column(name = "tag", nullable = false, length = 50)
	private List<String> tags = new ArrayList<>();

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

	public List<NodeResource> getResources() {
		return resources;
	}

	/** The resources that count toward readiness. When there are none, the hand-entered value is used. */
	public List<NodeResource> countingResources() {
		return resources.stream().filter(NodeResource::counts).toList();
	}

	/** The trees whose readiness this node's readiness depends on. */
	public List<Tree> countingTrees() {
		return resources.stream()
			.filter(resource -> resource.counts() && resource.getType() == NodeResourceType.TREE)
			.map(NodeResource::getTree)
			.toList();
	}

	public List<String> getTags() {
		return tags;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

}
