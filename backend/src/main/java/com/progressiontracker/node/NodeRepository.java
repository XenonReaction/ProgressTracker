package com.progressiontracker.node;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.progressiontracker.tree.Tree;
import com.progressiontracker.user.User;

public interface NodeRepository extends JpaRepository<Node, Long> {

	@EntityGraph(attributePaths = "links")
	List<Node> findByOwnerOrderByTitleAsc(User owner);

	Optional<Node> findByIdAndOwner(Long id, User owner);

	boolean existsByOwner(User owner);

	/** The nodes whose readiness comes from this tree. */
	List<Node> findByLinkedTreeOrderByTitleAsc(Tree linkedTree);

}
