package com.progressiontracker.flashcards.internal;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardController {

	private final CardService cardService;

	public CardController(CardService cardService) {
		this.cardService = cardService;
	}

	@GetMapping("/api/v1/decks/{deckId}/cards")
	public List<CardResponse> list(@PathVariable Long deckId) {
		return cardService.list(deckId);
	}

	@PostMapping("/api/v1/decks/{deckId}/cards")
	public ResponseEntity<CardResponse> create(@PathVariable Long deckId, @Valid @RequestBody CardRequest request) {
		CardResponse created = cardService.create(deckId, request);
		return ResponseEntity.created(URI.create("/api/v1/decks/" + deckId + "/cards/" + created.id())).body(created);
	}

	@PutMapping("/api/v1/decks/{deckId}/cards/{cardId}")
	public CardResponse update(@PathVariable Long deckId, @PathVariable Long cardId,
			@Valid @RequestBody CardRequest request) {
		return cardService.update(deckId, cardId, request);
	}

	@DeleteMapping("/api/v1/decks/{deckId}/cards/{cardId}")
	public ResponseEntity<Void> delete(@PathVariable Long deckId, @PathVariable Long cardId) {
		cardService.delete(deckId, cardId);
		return ResponseEntity.noContent().build();
	}

	/** Records one answer; returns the card with its new standing. */
	@PostMapping("/api/v1/decks/{deckId}/cards/{cardId}/reviews")
	public CardResponse review(@PathVariable Long deckId, @PathVariable Long cardId,
			@Valid @RequestBody ReviewRequest request) {
		return cardService.review(deckId, cardId, request);
	}

	/** Cards not yet passed, across all decks or in one, in the order to study them. */
	@GetMapping("/api/v1/review-queue")
	public List<CardResponse> reviewQueue(@RequestParam(required = false) Long deckId) {
		return cardService.reviewQueue(deckId);
	}

}
