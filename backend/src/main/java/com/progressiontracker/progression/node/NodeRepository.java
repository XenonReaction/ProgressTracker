package com.progressiontracker.progression.node;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.progressiontracker.progression.tree.Tree;
import com.progressiontracker.user.User;

public interface NodeRepository extends JpaRepository<Node, Long> {

	@EntityGraph(attributePaths = { "resources", "tags" })
	List<Node> findByOwnerOrderByTitleAsc(User owner);

	Optional<Node> findByIdAndOwner(Long id, User owner);

	boolean existsByOwner(User owner);

	/** The nodes that list this tree as a resource, whether it counts or not. */
	@Query("select distinct n from Node n join n.resources r where r.tree = :tree order by n.title")
	List<Node> findLinkingTo(Tree tree);

}
