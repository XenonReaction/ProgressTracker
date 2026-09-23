package com.progressiontracker.tree;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Prerequisite edges within a tree. Edges have no editable fields, so there's no PUT. */
@RestController
@RequestMapping("/api/v1/trees/{treeId}/prerequisites")
public class PrerequisiteController {

	private final PrerequisiteService prerequisiteService;

	public PrerequisiteController(PrerequisiteService prerequisiteService) {
		this.prerequisiteService = prerequisiteService;
	}

	@GetMapping
	public List<PrerequisiteResponse> list(@PathVariable Long treeId) {
		return prerequisiteService.list(treeId);
	}

	@PostMapping
	public ResponseEntity<PrerequisiteResponse> create(@PathVariable Long treeId,
			@Valid @RequestBody PrerequisiteRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(prerequisiteService.create(treeId, request));
	}

	@DeleteMapping("/{prerequisiteId}")
	public ResponseEntity<Void> delete(@PathVariable Long treeId, @PathVariable Long prerequisiteId) {
		prerequisiteService.delete(treeId, prerequisiteId);
		return ResponseEntity.noContent().build();
	}

}
