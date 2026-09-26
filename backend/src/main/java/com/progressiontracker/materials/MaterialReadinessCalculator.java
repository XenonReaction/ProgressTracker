package com.progressiontracker.materials;

import java.util.Optional;

import org.springframework.stereotype.Component;

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

}
