package com.progressiontracker.lessons.internal;

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
@RequestMapping("/api/v1/lessons")
public class LessonController {

	private final LessonService lessonService;

	public LessonController(LessonService lessonService) {
		this.lessonService = lessonService;
	}

	@GetMapping
	public List<LessonResponse> list() {
		return lessonService.list();
	}

	/** Reads the lesson without counting as a review (the edit form uses this too). */
	@GetMapping("/{id}")
	public LessonResponse get(@PathVariable Long id) {
		return lessonService.get(id);
	}

	@PostMapping
	public ResponseEntity<LessonResponse> create(@Valid @RequestBody LessonRequest request) {
		LessonResponse created = lessonService.create(request);
		return ResponseEntity.created(URI.create("/api/v1/lessons/" + created.id())).body(created);
	}

	@PutMapping("/{id}")
	public LessonResponse update(@PathVariable Long id, @Valid @RequestBody LessonRequest request) {
		return lessonService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		lessonService.delete(id);
		return ResponseEntity.noContent().build();
	}

	/** Records that the user opened the lesson to read it; returns the lesson. */
	@PostMapping("/{id}/opens")
	public LessonResponse recordOpen(@PathVariable Long id) {
		return lessonService.recordOpen(id);
	}

	/** Records the user's progress on the lesson; returns the lesson. */
	@PostMapping("/{id}/progress")
	public LessonResponse recordProgress(@PathVariable Long id, @Valid @RequestBody LessonProgressRequest request) {
		return lessonService.recordProgress(id, request);
	}

}
