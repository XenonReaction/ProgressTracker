package com.progressiontracker.materials.internal;

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
@RequestMapping("/api/v1/materials")
public class MaterialController {

	private final MaterialService materialService;

	public MaterialController(MaterialService materialService) {
		this.materialService = materialService;
	}

	@GetMapping
	public List<MaterialResponse> list() {
		return materialService.list();
	}

	@GetMapping("/{id}")
	public MaterialResponse get(@PathVariable Long id) {
		return materialService.get(id);
	}

	@PostMapping
	public ResponseEntity<MaterialResponse> create(@Valid @RequestBody MaterialRequest request) {
		MaterialResponse created = materialService.create(request);
		return ResponseEntity.created(URI.create("/api/v1/materials/" + created.id())).body(created);
	}

	@PutMapping("/{id}")
	public MaterialResponse update(@PathVariable Long id, @Valid @RequestBody MaterialRequest request) {
		return materialService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		materialService.delete(id);
		return ResponseEntity.noContent().build();
	}

	/** The material's progress history, newest first. */
	@GetMapping("/{id}/progress")
	public List<ProgressUpdateResponse> history(@PathVariable Long id) {
		return materialService.history(id);
	}

	/** Records new progress; returns the material with it. */
	@PostMapping("/{id}/progress")
	public MaterialResponse recordProgress(@PathVariable Long id, @Valid @RequestBody ProgressUpdateRequest request) {
		return materialService.recordProgress(id, request);
	}

}
