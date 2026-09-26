package com.progressiontracker.materials.internal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.progressiontracker.user.User;

public interface MaterialRepository extends JpaRepository<Material, Long> {

	List<Material> findByOwnerOrderByTitleAscIdAsc(User owner);

	Optional<Material> findByIdAndOwner(Long id, User owner);

	boolean existsByOwner(User owner);

}
