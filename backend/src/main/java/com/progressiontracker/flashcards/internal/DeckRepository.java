package com.progressiontracker.flashcards.internal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.progressiontracker.user.User;

public interface DeckRepository extends JpaRepository<Deck, Long> {

	List<Deck> findByOwnerOrderByTitleAscIdAsc(User owner);

	Optional<Deck> findByIdAndOwner(Long id, User owner);

	boolean existsByOwner(User owner);

}
