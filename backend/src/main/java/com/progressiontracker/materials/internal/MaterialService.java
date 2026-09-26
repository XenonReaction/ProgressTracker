package com.progressiontracker.materials.internal;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.materials.MaterialDeletionCheck;
import com.progressiontracker.user.CurrentUserService;

/**
 * CRUD for the current user's materials, and the progress they report on them. Deleting a
 * material deletes its history, and is refused while a node lists it.
 */
@Service
@Transactional
public class MaterialService {

	private static final Logger log = LoggerFactory.getLogger(MaterialService.class);

	private final MaterialRepository materials;

	private final MaterialProgressUpdateRepository updates;

	private final CurrentUserService currentUser;

	private final List<MaterialDeletionCheck> deletionChecks;

	public MaterialService(MaterialRepository materials, MaterialProgressUpdateRepository updates,
			CurrentUserService currentUser, List<MaterialDeletionCheck> deletionChecks) {
		this.materials = materials;
		this.updates = updates;
		this.currentUser = currentUser;
		this.deletionChecks = deletionChecks;
	}

	@Transactional(readOnly = true)
	public List<MaterialResponse> list() {
		List<Material> owned = materials.findByOwnerOrderByTitleAscIdAsc(currentUser.getCurrentUser());
		Map<Long, List<MaterialProgressUpdate>> byMaterial = new HashMap<>();
		if (!owned.isEmpty()) {
			for (MaterialProgressUpdate update : updates.findByMaterialInOrderByRecordedAtDescIdDesc(owned)) {
				byMaterial.computeIfAbsent(update.getMaterial().getId(), id -> new ArrayList<>()).add(update);
			}
		}
		return owned.stream()
			.map(material -> MaterialResponse.from(material, byMaterial.getOrDefault(material.getId(), List.of())))
			.toList();
	}

	@Transactional(readOnly = true)
	public MaterialResponse get(Long id) {
		return toResponse(findOwned(id));
	}

	public MaterialResponse create(MaterialRequest request) {
		Material material = new Material(currentUser.getCurrentUser(), request.title(), request.url());
		material.setNotes(request.notes());
		return toResponse(materials.save(material));
	}

	/** Changes the details only; it isn't a review, so the progress and its date stay. */
	public MaterialResponse update(Long id, MaterialRequest request) {
		Material material = findOwned(id);
		material.setTitle(request.title());
		material.setUrl(request.url());
		material.setNotes(request.notes());
		materials.flush(); // so the response carries the new updatedAt
		return toResponse(material);
	}

	/** Refused (by a {@link MaterialDeletionCheck}) while another module still refers to the material. */
	public void delete(Long id) {
		Material material = findOwned(id);
		deletionChecks.forEach(check -> check.checkCanDelete(id));
		materials.delete(material);
		log.info("Deleted material {}", id);
	}

	/** Records how far through the material the user is now; earlier updates are kept. */
	public MaterialResponse recordProgress(Long id, ProgressUpdateRequest request) {
		Material material = findOwned(id);
		Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS); // the column's precision
		updates.save(new MaterialProgressUpdate(material, currentUser.getCurrentUser(), now, request.progress(),
				request.note() == null || request.note().isBlank() ? null : request.note().trim()));
		return toResponse(material);
	}

	/** Every progress update, newest first. */
	@Transactional(readOnly = true)
	public List<ProgressUpdateResponse> history(Long id) {
		return updates.findByMaterialOrderByRecordedAtDescIdDesc(findOwned(id))
			.stream()
			.map(ProgressUpdateResponse::from)
			.toList();
	}

	/** Looks up one of the current user's materials, or throws 404. */
	private Material findOwned(Long id) {
		return materials.findByIdAndOwner(id, currentUser.getCurrentUser())
			.orElseThrow(() -> new NotFoundException("Material", id));
	}

	private MaterialResponse toResponse(Material material) {
		return MaterialResponse.from(material, updates.findByMaterialOrderByRecordedAtDescIdDesc(material));
	}

}
