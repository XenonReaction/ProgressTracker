package com.progressiontracker.materials;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.progressiontracker.materials.internal.Material;
import com.progressiontracker.materials.internal.MaterialProgressUpdate;
import com.progressiontracker.materials.internal.MaterialProgressUpdateRepository;
import com.progressiontracker.materials.internal.MaterialRepository;
import com.progressiontracker.user.CurrentUserService;

/**
 * How other modules read a material: its title, the latest progress the user reported and
 * when. Worked out from the progress updates when asked, never stored. Call it inside a
 * transaction.
 */
@Component
public class MaterialReadinessCalculator {

	private final MaterialRepository materials;

	private final MaterialProgressUpdateRepository updates;

	private final CurrentUserService currentUser;

	public MaterialReadinessCalculator(MaterialRepository materials, MaterialProgressUpdateRepository updates,
			CurrentUserService currentUser) {
		this.materials = materials;
		this.updates = updates;
		this.currentUser = currentUser;
	}

	/** One of the current user's materials by id. Empty if there's no such material or it's someone else's. */
	public Optional<MaterialSummary> material(Long materialId) {
		return materials.findByIdAndOwner(materialId, currentUser.getCurrentUser())
			.map(material -> updates.findFirstByMaterialOrderByRecordedAtDescIdDesc(material)
				.map(latest -> new MaterialSummary(material.getId(), material.getTitle(), latest.getProgress(),
						latest.getRecordedAt()))
				.orElseGet(() -> new MaterialSummary(material.getId(), material.getTitle(), 0, null)));
	}

	/**
	 * The batch form of {@link #material(Long)}: the current user's materials among these ids,
	 * by id, in a fixed number of queries. Ids that aren't the user's materials are left out.
	 */
	public Map<Long, MaterialSummary> materials(Collection<Long> materialIds) {
		if (materialIds.isEmpty()) {
			return Map.of();
		}
		List<Material> found = materials.findByIdInAndOwner(materialIds, currentUser.getCurrentUser());
		if (found.isEmpty()) {
			return Map.of();
		}
		Map<Long, MaterialProgressUpdate> latest = new HashMap<>();
		for (MaterialProgressUpdate update : updates.findByMaterialInOrderByRecordedAtDescIdDesc(found)) {
			latest.putIfAbsent(update.getMaterial().getId(), update); // newest first
		}
		Map<Long, MaterialSummary> summaries = new HashMap<>();
		for (Material material : found) {
			MaterialProgressUpdate update = latest.get(material.getId());
			summaries.put(material.getId(), new MaterialSummary(material.getId(), material.getTitle(),
					update == null ? 0 : update.getProgress(), update == null ? null : update.getRecordedAt()));
		}
		return summaries;
	}

}
