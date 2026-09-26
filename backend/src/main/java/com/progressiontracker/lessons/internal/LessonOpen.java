package com.progressiontracker.lessons.internal;

import java.time.Instant;

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

/** The user opened a lesson to read it: a review, for "last reviewed". Never changed. */
@Entity
@Table(name = "lesson_opens")
public class LessonOpen {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "lesson_id", nullable = false, foreignKey = @ForeignKey(name = "lesson_opens_lesson_id_fk"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Lesson lesson;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "lesson_opens_user_id_fk"))
	private User user;

	@Column(nullable = false)
	private Instant openedAt;

	protected LessonOpen() {
	}

	public LessonOpen(Lesson lesson, User user, Instant openedAt) {
		this.lesson = lesson;
		this.user = user;
		this.openedAt = openedAt;
	}

	public Instant getOpenedAt() {
		return openedAt;
	}

}
