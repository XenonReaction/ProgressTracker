package com.progressiontracker.progression.tree;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Prerequisite edges within a tree. An edge's only editable field is its route. */
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

	@PutMapping("/{prerequisiteId}/route")
	public PrerequisiteResponse updateRoute(@PathVariable Long treeId, @PathVariable Long prerequisiteId,
			@Valid @RequestBody PrerequisiteRouteRequest request) {
		return prerequisiteService.updateRoute(treeId, prerequisiteId, request);
	}

	/** Resets every edge to its default route (used by auto-layout). */
	@DeleteMapping("/routes")
	public ResponseEntity<Void> resetRoutes(@PathVariable Long treeId) {
		prerequisiteService.resetRoutes(treeId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{prerequisiteId}")
	public ResponseEntity<Void> delete(@PathVariable Long treeId, @PathVariable Long prerequisiteId) {
		prerequisiteService.delete(treeId, prerequisiteId);
		return ResponseEntity.noContent().build();
	}

}
