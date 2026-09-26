package com.progressiontracker.lessons.internal;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import com.progressiontracker.user.User;

/** A lesson written in the app: a title, an optional summary, and Markdown sections in order. */
@Entity
@Table(name = "lessons")
public class Lesson {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "lessons_user_id_fk"))
	private User owner;

	@Column(nullable = false, length = 200)
	private String title;

	@Column(columnDefinition = "text")
	private String summary;

	@ElementCollection
	@CollectionTable(name = "lesson_sections", joinColumns = @JoinColumn(name = "lesson_id"),
			foreignKey = @ForeignKey(name = "lesson_sections_lesson_id_fk"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	@OrderColumn(name = "position")
	private List<LessonSection> sections = new ArrayList<>();

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private Instant updatedAt;

	protected Lesson() {
	}

	public Lesson(User owner, String title) {
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<LessonSection> getSections() {
		return sections;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

}
