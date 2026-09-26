package com.progressiontracker.lessons.internal;

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

/** Progress the user entered on a lesson (0–100). Never changed; the latest one is the lesson's progress. */
@Entity
@Table(name = "lesson_progress_updates", check = @CheckConstraint(name = "lesson_progress_updates_progress_range",
		constraint = "progress between 0 and 100"))
public class LessonProgressUpdate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "lesson_id", nullable = false,
			foreignKey = @ForeignKey(name = "lesson_progress_updates_lesson_id_fk"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Lesson lesson;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "lesson_progress_updates_user_id_fk"))
	private User user;

	@Column(nullable = false)
	private Instant recordedAt;

	@Column(nullable = false)
	private int progress;

	protected LessonProgressUpdate() {
	}

	public LessonProgressUpdate(Lesson lesson, User user, Instant recordedAt, int progress) {
		this.lesson = lesson;
		this.user = user;
		this.recordedAt = recordedAt;
		this.progress = progress;
	}

	public Instant getRecordedAt() {
		return recordedAt;
	}

	public int getProgress() {
		return progress;
	}

}
