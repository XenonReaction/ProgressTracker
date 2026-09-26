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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

/** A flashcard: a question on the front and its answer on the back. */
@Entity
@Table(name = "cards")
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "deck_id", nullable = false, foreignKey = @ForeignKey(name = "cards_deck_id_fk"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Deck deck;

	@Column(nullable = false, columnDefinition = "text")
	private String front;

	@Column(nullable = false, columnDefinition = "text")
	private String back;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private Instant updatedAt;

	protected Card() {
	}

	public Card(Deck deck, String front, String back) {
		this.deck = deck;
		this.front = front;
		this.back = back;
	}

	public Long getId() {
		return id;
	}

	public Deck getDeck() {
		return deck;
	}

	public String getFront() {
		return front;
	}

	public void setFront(String front) {
		this.front = front;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

}
