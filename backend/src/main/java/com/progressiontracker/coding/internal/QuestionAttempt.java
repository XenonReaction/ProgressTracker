package com.progressiontracker.coding.internal;

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

/** Something the user did on a question: revealed its solution, or marked it solved. Never changed. */
@Entity
@Table(name = "question_attempts", check = @CheckConstraint(name = "question_attempts_action_check",
		constraint = "action in ('revealed', 'solved')"))
public class QuestionAttempt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "question_id", nullable = false,
			foreignKey = @ForeignKey(name = "question_attempts_question_id_fk"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private CodingQuestion question;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "question_attempts_user_id_fk"))
	private User user;

	@Column(nullable = false)
	private Instant recordedAt;

	@Column(nullable = false, length = 20)
	private AttemptAction action;

	protected QuestionAttempt() {
	}

	public QuestionAttempt(CodingQuestion question, User user, Instant recordedAt, AttemptAction action) {
		this.question = question;
		this.user = user;
		this.recordedAt = recordedAt;
		this.action = action;
	}

	public CodingQuestion getQuestion() {
		return question;
	}

	public Instant getRecordedAt() {
		return recordedAt;
	}

	public AttemptAction getAction() {
		return action;
	}

}
