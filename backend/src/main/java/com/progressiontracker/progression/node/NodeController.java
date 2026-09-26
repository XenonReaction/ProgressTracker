package com.progressiontracker.progression.node;

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

import com.progressiontracker.progression.tree.TreeRef;

@RestController
@RequestMapping("/api/v1/nodes")
public class NodeController {

	private final NodeService nodeService;

	public NodeController(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	@GetMapping
	public List<NodeResponse> list() {
		return nodeService.list();
	}

	@GetMapping("/{id}")
	public NodeResponse get(@PathVariable Long id) {
		return nodeService.get(id);
	}

	@PostMapping
	public ResponseEntity<NodeResponse> create(@Valid @RequestBody NodeRequest request) {
		NodeResponse created = nodeService.create(request);
		return ResponseEntity.created(URI.create("/api/v1/nodes/" + created.id())).body(created);
	}

	@PutMapping("/{id}")
	public NodeResponse update(@PathVariable Long id, @Valid @RequestBody NodeRequest request) {
		return nodeService.update(id, request);
	}

	@PutMapping("/{id}/readiness")
	public NodeResponse updateReadiness(@PathVariable Long id, @Valid @RequestBody NodeReadinessRequest request) {
		return nodeService.updateReadiness(id, request);
	}

	@GetMapping("/{id}/trees")
	public List<TreeRef> treesUsing(@PathVariable Long id) {
		return nodeService.treesUsing(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		nodeService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
