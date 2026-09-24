package com.progressiontracker.tree;

import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
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

import com.progressiontracker.node.Node;

/**
 * A library node placed in a tree. Holds everything that is per-tree rather than
 * per-node: canvas position, and the two readiness thresholds this node's
 * prerequisites must meet before it shows as ready:
 * <ul>
 * <li>{@code aggregateThreshold}: minimum average readiness across all prerequisites</li>
 * <li>{@code individualThreshold}: minimum readiness of each prerequisite on its own</li>
 * </ul>
 */
@Entity
@Table(name = "tree_nodes",
		uniqueConstraints = @UniqueConstraint(name = "tree_nodes_tree_node_unique",
				columnNames = { "tree_id", "node_id" }),
		check = {
				@CheckConstraint(name = "tree_nodes_aggregate_threshold_range",
						constraint = "aggregate_threshold between 0 and 100"),
				@CheckConstraint(name = "tree_nodes_individual_threshold_range",
						constraint = "individual_threshold between 0 and 100") })
public class TreeNode {

	public static final int DEFAULT_AGGREGATE_THRESHOLD = 80;

	public static final int DEFAULT_INDIVIDUAL_THRESHOLD = 70;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "tree_id", nullable = false, foreignKey = @ForeignKey(name = "tree_nodes_tree_id_fk"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Tree tree;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "node_id", nullable = false, foreignKey = @ForeignKey(name = "tree_nodes_node_id_fk"))
	private Node node;

	@Column(name = "position_x", nullable = false)
	private double positionX;

	@Column(name = "position_y", nullable = false)
	private double positionY;

	@Column(nullable = false)
	private int aggregateThreshold = DEFAULT_AGGREGATE_THRESHOLD;

	@Column(nullable = false)
	private int individualThreshold = DEFAULT_INDIVIDUAL_THRESHOLD;

	protected TreeNode() {
	}

	public TreeNode(Tree tree, Node node, double positionX, double positionY) {
		this.tree = tree;
		this.node = node;
		this.positionX = positionX;
		this.positionY = positionY;
	}

	public Long getId() {
		return id;
	}

	public Tree getTree() {
		return tree;
	}

	public Node getNode() {
		return node;
	}

	public double getPositionX() {
		return positionX;
	}

	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}

	public int getAggregateThreshold() {
		return aggregateThreshold;
	}

	public void setAggregateThreshold(int aggregateThreshold) {
		this.aggregateThreshold = aggregateThreshold;
	}

	public int getIndividualThreshold() {
		return individualThreshold;
	}

	public void setIndividualThreshold(int individualThreshold) {
		this.individualThreshold = individualThreshold;
	}

}
