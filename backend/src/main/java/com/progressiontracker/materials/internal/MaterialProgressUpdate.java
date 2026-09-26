package com.progressiontracker.materials.internal;

import java.time.Instant;

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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.progressiontracker.user.User;

/**
 * Progress the user reported on a material: how far through it they are (0–100), when, and
 * optionally what they covered. Never changed; the latest one is the material's progress.
 */
@Entity
@Table(name = "material_progress_updates", check = @CheckConstraint(name = "material_progress_updates_progress_range",
		constraint = "progress between 0 and 100"))
public class MaterialProgressUpdate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "material_id", nullable = false,
			foreignKey = @ForeignKey(name = "material_progress_updates_material_id_fk"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Material material;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false,
			foreignKey = @ForeignKey(name = "material_progress_updates_user_id_fk"))
	private User user;

	@Column(nullable = false)
	private Instant recordedAt;

	@Column(nullable = false)
	private int progress;

	@Column(columnDefinition = "text")
	private String note;

	protected MaterialProgressUpdate() {
	}

	public MaterialProgressUpdate(Material material, User user, Instant recordedAt, int progress, String note) {
		this.material = material;
		this.user = user;
		this.recordedAt = recordedAt;
		this.progress = progress;
		this.note = note;
	}

	public Long getId() {
		return id;
	}

	public Material getMaterial() {
		return material;
	}

	public User getUser() {
		return user;
	}

	public Instant getRecordedAt() {
		return recordedAt;
	}

	public int getProgress() {
		return progress;
	}

	public String getNote() {
		return note;
	}

}
