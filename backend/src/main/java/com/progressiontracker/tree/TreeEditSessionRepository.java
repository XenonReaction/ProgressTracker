package com.progressiontracker.tree;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.progressiontracker.user.User;

public interface TreeEditSessionRepository extends JpaRepository<TreeEditSession, Long> {

	Optional<TreeEditSession> findByTree(Tree tree);

	boolean existsByTree(Tree tree);

	/** The user's trees that are in edit mode, for the tree list. */
	@Query("select s from TreeEditSession s where s.tree.owner = :owner")
	List<TreeEditSession> findByOwner(User owner);

}
