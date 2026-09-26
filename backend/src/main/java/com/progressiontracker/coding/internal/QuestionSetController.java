package com.progressiontracker.coding.internal;

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
@RequestMapping("/api/v1/question-sets")
public class QuestionSetController {

	private final QuestionSetService setService;

	public QuestionSetController(QuestionSetService setService) {
		this.setService = setService;
	}

	@GetMapping
	public List<QuestionSetResponse> list() {
		return setService.list();
	}

	@GetMapping("/{id}")
	public QuestionSetResponse get(@PathVariable Long id) {
		return setService.get(id);
	}

	@PostMapping
	public ResponseEntity<QuestionSetResponse> create(@Valid @RequestBody QuestionSetRequest request) {
		QuestionSetResponse created = setService.create(request);
		return ResponseEntity.created(URI.create("/api/v1/question-sets/" + created.id())).body(created);
	}

	@PutMapping("/{id}")
	public QuestionSetResponse update(@PathVariable Long id, @Valid @RequestBody QuestionSetRequest request) {
		return setService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		setService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
