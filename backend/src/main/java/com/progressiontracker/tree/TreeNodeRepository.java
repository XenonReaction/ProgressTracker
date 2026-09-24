package com.progressiontracker.tree;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.progressiontracker.node.Node;

public interface TreeNodeRepository extends JpaRepository<TreeNode, Long> {

	@EntityGraph(attributePaths = "node")
	List<TreeNode> findByTreeOrderByIdAsc(Tree tree);

	Optional<TreeNode> findByIdAndTree(Long id, Tree tree);

	boolean existsByTreeAndNode(Tree tree, Node node);

	boolean existsByNode(Node node);

	/** The library nodes placed in a tree, for averaging its readiness. */
	@Query("select tn.node from TreeNode tn where tn.tree = :tree")
	List<Node> findNodesInTree(Tree tree);

	/** The trees that nodes in this tree link to. */
	@Query("""
			select distinct n.linkedTree.id from TreeNode tn join tn.node n
			where tn.tree.id = :treeId and n.linkedTree is not null""")
	List<Long> findLinkedTreeIds(Long treeId);

}
