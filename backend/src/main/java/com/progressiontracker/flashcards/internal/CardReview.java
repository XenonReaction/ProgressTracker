package com.progressiontracker.flashcards.internal;

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

/** One answer to a card: who gave it, when, and whether they marked it correct. Never changed. */
@Entity
@Table(name = "card_reviews")
public class CardReview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "card_id", nullable = false, foreignKey = @ForeignKey(name = "card_reviews_card_id_fk"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Card card;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "card_reviews_user_id_fk"))
	private User user;

	@Column(nullable = false)
	private Instant reviewedAt;

	@Column(nullable = false)
	private boolean correct;

	protected CardReview() {
	}

	public CardReview(Card card, User user, Instant reviewedAt, boolean correct) {
		this.card = card;
		this.user = user;
		this.reviewedAt = reviewedAt;
		this.correct = correct;
	}

	public Long getId() {
		return id;
	}

	public Card getCard() {
		return card;
	}

	public User getUser() {
		return user;
	}

	public Instant getReviewedAt() {
		return reviewedAt;
	}

	public boolean isCorrect() {
		return correct;
	}

}
