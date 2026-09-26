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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/decks")
public class DeckController {

	private final DeckService deckService;

	public DeckController(DeckService deckService) {
		this.deckService = deckService;
	}

	@GetMapping
	public List<DeckResponse> list() {
		return deckService.list();
	}

	@GetMapping("/{id}")
	public DeckResponse get(@PathVariable Long id) {
		return deckService.get(id);
	}

	@PostMapping
	public ResponseEntity<DeckResponse> create(@Valid @RequestBody DeckRequest request) {
		DeckResponse created = deckService.create(request);
		return ResponseEntity.created(URI.create("/api/v1/decks/" + created.id())).body(created);
	}

	@PutMapping("/{id}")
	public DeckResponse update(@PathVariable Long id, @Valid @RequestBody DeckRequest request) {
		return deckService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		deckService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
