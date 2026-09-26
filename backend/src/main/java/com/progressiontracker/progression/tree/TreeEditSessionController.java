package com.progressiontracker.progression.tree;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Edit mode for a tree: start it (saving a restore point), finish it, or discard it. */
@RestController
@RequestMapping("/api/v1/trees/{treeId}/edit-session")
public class TreeEditSessionController {

	private final TreeEditSessionService editSessions;

	public TreeEditSessionController(TreeEditSessionService editSessions) {
		this.editSessions = editSessions;
	}

	@PostMapping
	public ResponseEntity<TreeEditSessionResponse> start(@PathVariable Long treeId) {
		return ResponseEntity.created(URI.create("/api/v1/trees/" + treeId + "/edit-session"))
			.body(editSessions.start(treeId));
	}

	/** "Done": keep the changes. */
	@DeleteMapping
	public ResponseEntity<Void> finish(@PathVariable Long treeId) {
		editSessions.finish(treeId);
		return ResponseEntity.noContent().build();
	}

	/** "Discard changes": put the tree back as it was when the session started. */
	@PostMapping("/discard")
	public ResponseEntity<Void> discard(@PathVariable Long treeId) {
		editSessions.discard(treeId);
		return ResponseEntity.noContent().build();
	}

}
