package com.progressiontracker.tree;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * A tree in edit mode, with the restore point taken when "Edit" was clicked. "Done" deletes
 * it and keeps the changes; "Discard changes" puts the snapshot back. See
 * {@link TreeEditSessionService}.
 */
@Entity
@Table(name = "tree_edit_sessions",
		uniqueConstraints = @UniqueConstraint(name = "tree_edit_sessions_tree_id_unique", columnNames = "tree_id"))
public class TreeEditSession {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "tree_id", nullable = false, foreignKey = @ForeignKey(name = "tree_edit_sessions_tree_id_fk"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Tree tree;

	@Column(nullable = false)
	private Instant startedAt;

	/** {@link TreeSnapshot} as JSON. */
	@Column(nullable = false, columnDefinition = "text")
	private String snapshot;

	protected TreeEditSession() {
	}

	public TreeEditSession(Tree tree, Instant startedAt, String snapshot) {
		this.tree = tree;
		this.startedAt = startedAt;
		this.snapshot = snapshot;
	}

	public Long getId() {
		return id;
	}

	public Tree getTree() {
		return tree;
	}

	public Instant getStartedAt() {
		return startedAt;
	}

	public String getSnapshot() {
		return snapshot;
	}

}
