package com.progressiontracker.progression.tree;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrerequisiteRepository extends JpaRepository<Prerequisite, Long> {

	/** All edges in a tree. Both ends of an edge are always in the same tree. */
	@Query("select p from Prerequisite p where p.dependent.tree = :tree order by p.id")
	List<Prerequisite> findByTree(Tree tree);

	Optional<Prerequisite> findByIdAndDependentTree(Long id, Tree tree);

	boolean existsByPrerequisiteAndDependent(TreeNode prerequisite, TreeNode dependent);

}
