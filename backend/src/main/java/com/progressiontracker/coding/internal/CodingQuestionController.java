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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/question-sets/{setId}/questions")
public class CodingQuestionController {

	private final CodingQuestionService questionService;

	public CodingQuestionController(CodingQuestionService questionService) {
		this.questionService = questionService;
	}

	@GetMapping
	public List<QuestionResponse> list(@PathVariable Long setId) {
		return questionService.list(setId);
	}

	/** Solution hidden unless revealed; {@code includeSolution=true} is for the edit form and isn't recorded. */
	@GetMapping("/{questionId}")
	public QuestionResponse get(@PathVariable Long setId, @PathVariable Long questionId,
			@RequestParam(defaultValue = "false") boolean includeSolution) {
		return questionService.get(setId, questionId, includeSolution);
	}

	@PostMapping
	public ResponseEntity<QuestionResponse> create(@PathVariable Long setId,
			@Valid @RequestBody QuestionRequest request) {
		QuestionResponse created = questionService.create(setId, request);
		return ResponseEntity.created(URI.create("/api/v1/question-sets/" + setId + "/questions/" + created.id()))
			.body(created);
	}

	@PutMapping("/{questionId}")
	public QuestionResponse update(@PathVariable Long setId, @PathVariable Long questionId,
			@Valid @RequestBody QuestionRequest request) {
		return questionService.update(setId, questionId, request);
	}

	@DeleteMapping("/{questionId}")
	public ResponseEntity<Void> delete(@PathVariable Long setId, @PathVariable Long questionId) {
		questionService.delete(setId, questionId);
		return ResponseEntity.noContent().build();
	}

	/** Shows the solution and records that it was revealed. */
	@PostMapping("/{questionId}/reveal")
	public QuestionResponse reveal(@PathVariable Long setId, @PathVariable Long questionId) {
		return questionService.reveal(setId, questionId);
	}

	/** Marks the question solved. */
	@PostMapping("/{questionId}/solved")
	public QuestionResponse markSolved(@PathVariable Long setId, @PathVariable Long questionId) {
		return questionService.markSolved(setId, questionId);
	}

}
