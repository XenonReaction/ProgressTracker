package com.progressiontracker.progression.tree;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.progressiontracker.progression.node.Node;
import com.progressiontracker.user.User;

public interface TreeRepository extends JpaRepository<Tree, Long> {

	@EntityGraph(attributePaths = "tags")
	List<Tree> findByOwnerOrderByTitleAsc(User owner);

	Optional<Tree> findByIdAndOwner(Long id, User owner);

	boolean existsByOwner(User owner);

	@Query("""
			select t from Tree t
			where exists (select 1 from TreeNode tn where tn.tree = t and tn.node = :node)
			order by t.title""")
	List<Tree> findTreesContaining(Node node);

}
