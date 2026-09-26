package com.progressiontracker.flashcards.internal;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.flashcards.DeckDeletionCheck;
import com.progressiontracker.flashcards.DeckProgress;
import com.progressiontracker.flashcards.FlashcardReadinessCalculator;
import com.progressiontracker.user.CurrentUserService;

/**
 * CRUD for the current user's decks. Deleting a deck deletes its cards and their answers, and
 * is refused while a node lists the deck.
 */
@Service
@Transactional
public class DeckService {

	private static final Logger log = LoggerFactory.getLogger(DeckService.class);

	private final DeckRepository decks;

	private final FlashcardReadinessCalculator readiness;

	private final CurrentUserService currentUser;

	private final List<DeckDeletionCheck> deletionChecks;

	public DeckService(DeckRepository decks, FlashcardReadinessCalculator readiness, CurrentUserService currentUser,
			List<DeckDeletionCheck> deletionChecks) {
		this.decks = decks;
		this.readiness = readiness;
		this.currentUser = currentUser;
		this.deletionChecks = deletionChecks;
	}

	@Transactional(readOnly = true)
	public List<DeckResponse> list() {
		List<Deck> owned = decks.findByOwnerOrderByTitleAscIdAsc(currentUser.getCurrentUser());
		Map<Long, DeckProgress> progress = readiness.decks(owned);
		return owned.stream().map(deck -> DeckResponse.from(deck, progress.get(deck.getId()))).toList();
	}

	@Transactional(readOnly = true)
	public DeckResponse get(Long id) {
		return toResponse(findOwned(id));
	}

	public DeckResponse create(DeckRequest request) {
		Deck deck = new Deck(currentUser.getCurrentUser(), request.title());
		deck.setDescription(request.description());
		return toResponse(decks.save(deck));
	}

	public DeckResponse update(Long id, DeckRequest request) {
		Deck deck = findOwned(id);
		deck.setTitle(request.title());
		deck.setDescription(request.description());
		decks.flush(); // so the response carries the new updatedAt
		return toResponse(deck);
	}

	/** Refused (by a {@link DeckDeletionCheck}) while another module still refers to the deck. */
	public void delete(Long id) {
		Deck deck = findOwned(id);
		deletionChecks.forEach(check -> check.checkCanDelete(id));
		decks.delete(deck);
		log.info("Deleted deck {}", id);
	}

	/** Looks up one of the current user's decks, or throws 404. */
	@Transactional(readOnly = true)
	public Deck findOwned(Long id) {
		return decks.findByIdAndOwner(id, currentUser.getCurrentUser())
			.orElseThrow(() -> new NotFoundException("Deck", id));
	}

	/** The given deck, or all of the current user's decks when {@code id} is null. */
	@Transactional(readOnly = true)
	public List<Deck> findOwnedOrAll(Long id) {
		return id == null ? decks.findByOwnerOrderByTitleAscIdAsc(currentUser.getCurrentUser()) : List.of(findOwned(id));
	}

	private DeckResponse toResponse(Deck deck) {
		return DeckResponse.from(deck, readiness.deck(deck));
	}

}
