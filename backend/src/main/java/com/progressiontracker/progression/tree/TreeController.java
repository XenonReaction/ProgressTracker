package com.progressiontracker.progression.tree;

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
@RequestMapping("/api/v1/trees")
public class TreeController {

	private final TreeService treeService;

	public TreeController(TreeService treeService) {
		this.treeService = treeService;
	}

	@GetMapping
	public List<TreeResponse> list() {
		return treeService.list();
	}

	@GetMapping("/{id}")
	public TreeResponse get(@PathVariable Long id) {
		return treeService.get(id);
	}

	@PostMapping
	public ResponseEntity<TreeResponse> create(@Valid @RequestBody TreeRequest request) {
		TreeResponse created = treeService.create(request);
		return ResponseEntity.created(URI.create("/api/v1/trees/" + created.id())).body(created);
	}

	@PutMapping("/{id}")
	public TreeResponse update(@PathVariable Long id, @Valid @RequestBody TreeRequest request) {
		return treeService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		treeService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
