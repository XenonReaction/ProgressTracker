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

/** Nodes placed in a tree. {@code treeNodeId} is a tree node id, not a library node id. */
@RestController
@RequestMapping("/api/v1/trees/{treeId}/nodes")
public class TreeNodeController {

	private final TreeNodeService treeNodeService;

	public TreeNodeController(TreeNodeService treeNodeService) {
		this.treeNodeService = treeNodeService;
	}

	@GetMapping
	public List<TreeNodeResponse> list(@PathVariable Long treeId) {
		return treeNodeService.list(treeId);
	}

	@GetMapping("/{treeNodeId}")
	public TreeNodeResponse get(@PathVariable Long treeId, @PathVariable Long treeNodeId) {
		return treeNodeService.get(treeId, treeNodeId);
	}

	@PostMapping
	public ResponseEntity<TreeNodeResponse> add(@PathVariable Long treeId,
			@Valid @RequestBody TreeNodeCreateRequest request) {
		TreeNodeResponse created = treeNodeService.add(treeId, request);
		return ResponseEntity.created(URI.create("/api/v1/trees/" + treeId + "/nodes/" + created.id()))
			.body(created);
	}

	/** Saves many positions at once (auto-layout). Returns every node in the tree. */
	@PutMapping("/positions")
	public List<TreeNodeResponse> updatePositions(@PathVariable Long treeId,
			@Valid @RequestBody TreeLayoutRequest request) {
		return treeNodeService.updatePositions(treeId, request);
	}

	@PutMapping("/{treeNodeId}")
	public TreeNodeResponse update(@PathVariable Long treeId, @PathVariable Long treeNodeId,
			@Valid @RequestBody TreeNodeUpdateRequest request) {
		return treeNodeService.update(treeId, treeNodeId, request);
	}

	@DeleteMapping("/{treeNodeId}")
	public ResponseEntity<Void> remove(@PathVariable Long treeId, @PathVariable Long treeNodeId) {
		treeNodeService.remove(treeId, treeNodeId);
		return ResponseEntity.noContent().build();
	}

}
