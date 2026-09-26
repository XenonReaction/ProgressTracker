package com.progressiontracker.materials.internal;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialProgressUpdateRepository extends JpaRepository<MaterialProgressUpdate, Long> {

	/** The material's current progress: its latest update. */
	Optional<MaterialProgressUpdate> findFirstByMaterialOrderByRecordedAtDescIdDesc(Material material);

	/** Every update, newest first. */
	List<MaterialProgressUpdate> findByMaterialOrderByRecordedAtDescIdDesc(Material material);

	/** Every update to these materials, newest first, for a list. */
	List<MaterialProgressUpdate> findByMaterialInOrderByRecordedAtDescIdDesc(Collection<Material> materials);

}
