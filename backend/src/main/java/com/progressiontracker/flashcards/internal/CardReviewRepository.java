package com.progressiontracker.flashcards.internal;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.progressiontracker.user.User;

public interface CardReviewRepository extends JpaRepository<CardReview, Long> {

	/** The user's answers to these cards, newest first. */
	List<CardReview> findByUserAndCardInOrderByReviewedAtDescIdDesc(User user, Collection<Card> cards);

}
