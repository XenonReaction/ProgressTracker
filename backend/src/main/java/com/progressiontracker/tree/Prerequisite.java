package com.progressiontracker.tree;

import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * A directed edge within one tree: {@code prerequisite} must be learned before
 * {@code dependent}. The database blocks self-edges and duplicates. Keeping both ends in
 * the same tree and the graph acyclic are service-layer rules (Phase 2).
 */
@Entity
@Table(name = "prerequisites",
		uniqueConstraints = @UniqueConstraint(name = "prerequisites_edge_unique",
				columnNames = { "prerequisite_tree_node_id", "dependent_tree_node_id" }),
		check = @CheckConstraint(name = "prerequisites_no_self_edge",
				constraint = "prerequisite_tree_node_id <> dependent_tree_node_id"))
public class Prerequisite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "prerequisite_tree_node_id", nullable = false,
			foreignKey = @ForeignKey(name = "prerequisites_prerequisite_tree_node_id_fk"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private TreeNode prerequisite;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "dependent_tree_node_id", nullable = false,
			foreignKey = @ForeignKey(name = "prerequisites_dependent_tree_node_id_fk"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private TreeNode dependent;

	protected Prerequisite() {
	}

	public Prerequisite(TreeNode prerequisite, TreeNode dependent) {
		this.prerequisite = prerequisite;
		this.dependent = dependent;
	}

	public Long getId() {
		return id;
	}

	public TreeNode getPrerequisite() {
		return prerequisite;
	}

	public TreeNode getDependent() {
		return dependent;
	}

}
