package com.progressiontracker.flashcards.internal;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

	List<Card> findByDeckOrderByIdAsc(Deck deck);

	List<Card> findByDeckInOrderByIdAsc(Collection<Deck> decks);

	Optional<Card> findByIdAndDeck(Long id, Deck deck);

}
