package com.progressiontracker.tree;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.progressiontracker.node.Node;

public interface TreeNodeRepository extends JpaRepository<TreeNode, Long> {

	@EntityGraph(attributePaths = "node")
	List<TreeNode> findByTreeOrderByIdAsc(Tree tree);

	Optional<TreeNode> findByIdAndTree(Long id, Tree tree);

	boolean existsByTreeAndNode(Tree tree, Node node);

}
