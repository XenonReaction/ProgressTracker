package com.progressiontracker.progression.tree;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.progressiontracker.TestcontainersConfiguration;
import com.progressiontracker.progression.node.Node;
import com.progressiontracker.user.User;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class TreeMappingTest {

	@Autowired
	private TestEntityManager em;

	private User owner;

	private Node oop;

	private Node generics;

	private Tree tree;

	@BeforeEach
	void setUp() {
		owner = em.persist(new User("alice"));
		oop = em.persist(new Node(owner, "Object-Oriented Programming"));
		generics = em.persist(new Node(owner, "Generics"));
		tree = em.persist(new Tree(owner, "Java Fundamentals"));
	}

	@Test
	void treeMetadataAndTagsRoundTrip() {
		Tree javaTree = new Tree(owner, "Java Deep Dive");
		javaTree.setDescription("Beyond the basics");
		javaTree.setCategory("Technology");
		javaTree.getTags().add("java");
		javaTree.getTags().add("backend");
		Long id = em.persistAndGetId(javaTree, Long.class);
		em.flush();
		em.clear();

		Tree reloaded = em.find(Tree.class, id);

		assertThat(reloaded.getTitle()).isEqualTo("Java Deep Dive");
		assertThat(reloaded.getDescription()).isEqualTo("Beyond the basics");
		assertThat(reloaded.getCategory()).isEqualTo("Technology");
		assertThat(reloaded.getTags()).containsExactly("java", "backend");
	}

	@Test
	void treeNodeStoresPositionAndDefaultsThresholdsTo80And70() {
		Long id = em.persistAndGetId(new TreeNode(tree, oop, 12.5, -40), Long.class);
		em.flush();
		em.clear();

		TreeNode reloaded = em.find(TreeNode.class, id);

		assertThat(reloaded.getPositionX()).isEqualTo(12.5);
		assertThat(reloaded.getPositionY()).isEqualTo(-40);
		assertThat(reloaded.getAggregateThreshold()).isEqualTo(80);
		assertThat(reloaded.getIndividualThreshold()).isEqualTo(70);
	}

	@Test
	void sameLibraryNodeCanAppearInSeveralTrees() {
		Tree otherTree = em.persist(new Tree(owner, "Spring Basics"));

		em.persist(new TreeNode(tree, oop, 0, 0));
		em.persist(new TreeNode(otherTree, oop, 100, 100));
		em.flush();

		assertThat(countTreeNodesFor(oop)).isEqualTo(2);
	}

	@Test
	void rejectsSameNodeTwiceInOneTree() {
		em.persistAndFlush(new TreeNode(tree, oop, 0, 0));

		assertThatThrownBy(() -> em.persistAndFlush(new TreeNode(tree, oop, 50, 50)))
			.isInstanceOf(ConstraintViolationException.class)
			.extracting("constraintName")
			.isEqualTo("tree_nodes_tree_node_unique");
	}

	@Test
	void rejectsAggregateThresholdOutOfRange() {
		TreeNode treeNode = new TreeNode(tree, oop, 0, 0);
		treeNode.setAggregateThreshold(101);

		assertThatThrownBy(() -> em.persistAndFlush(treeNode)).isInstanceOf(ConstraintViolationException.class)
			.extracting("constraintName")
			.isEqualTo("tree_nodes_aggregate_threshold_range");
	}

	@Test
	void rejectsIndividualThresholdOutOfRange() {
		TreeNode treeNode = new TreeNode(tree, oop, 0, 0);
		treeNode.setIndividualThreshold(-1);

		assertThatThrownBy(() -> em.persistAndFlush(treeNode)).isInstanceOf(ConstraintViolationException.class)
			.extracting("constraintName")
			.isEqualTo("tree_nodes_individual_threshold_range");
	}

	@Test
	void prerequisiteRoundTrip() {
		TreeNode from = em.persist(new TreeNode(tree, oop, 0, 0));
		TreeNode to = em.persist(new TreeNode(tree, generics, 0, 150));
		Long id = em.persistAndGetId(new Prerequisite(from, to), Long.class);
		em.flush();
		em.clear();

		Prerequisite reloaded = em.find(Prerequisite.class, id);

		assertThat(reloaded.getPrerequisite().getId()).isEqualTo(from.getId());
		assertThat(reloaded.getDependent().getId()).isEqualTo(to.getId());
	}

	@Test
	void rejectsSelfEdge() {
		TreeNode treeNode = em.persist(new TreeNode(tree, oop, 0, 0));

		assertThatThrownBy(() -> em.persistAndFlush(new Prerequisite(treeNode, treeNode)))
			.isInstanceOf(ConstraintViolationException.class)
			.extracting("constraintName")
			.isEqualTo("prerequisites_no_self_edge");
	}

	@Test
	void rejectsDuplicateEdge() {
		TreeNode from = em.persist(new TreeNode(tree, oop, 0, 0));
		TreeNode to = em.persist(new TreeNode(tree, generics, 0, 150));
		em.persistAndFlush(new Prerequisite(from, to));

		assertThatThrownBy(() -> em.persistAndFlush(new Prerequisite(from, to)))
			.isInstanceOf(ConstraintViolationException.class)
			.extracting("constraintName")
			.isEqualTo("prerequisites_edge_unique");
	}

	@Test
	void deletingTreeRemovesItsTreeNodesAndEdgesButKeepsLibraryNodes() {
		TreeNode from = em.persist(new TreeNode(tree, oop, 0, 0));
		TreeNode to = em.persist(new TreeNode(tree, generics, 0, 150));
		em.persist(new Prerequisite(from, to));
		em.flush();
		em.clear();

		em.remove(em.find(Tree.class, tree.getId()));
		em.flush();
		em.clear();

		assertThat(count("select count(tn) from TreeNode tn")).isZero();
		assertThat(count("select count(p) from Prerequisite p")).isZero();
		assertThat(count("select count(n) from Node n")).isEqualTo(2);
	}

	@Test
	void deletingTreeNodeRemovesEdgesOnBothSides() {
		Node streams = em.persist(new Node(owner, "Streams API"));
		TreeNode first = em.persist(new TreeNode(tree, oop, 0, 0));
		TreeNode middle = em.persist(new TreeNode(tree, generics, 0, 150));
		TreeNode last = em.persist(new TreeNode(tree, streams, 0, 300));
		em.persist(new Prerequisite(first, middle));
		em.persist(new Prerequisite(middle, last));
		em.flush();
		em.clear();

		em.remove(em.find(TreeNode.class, middle.getId()));
		em.flush();
		em.clear();

		assertThat(count("select count(p) from Prerequisite p")).isZero();
		assertThat(count("select count(tn) from TreeNode tn")).isEqualTo(2);
	}

	private long countTreeNodesFor(Node node) {
		return em.getEntityManager()
			.createQuery("select count(tn) from TreeNode tn where tn.node = :node", Long.class)
			.setParameter("node", node)
			.getSingleResult();
	}

	private long count(String jpql) {
		return em.getEntityManager().createQuery(jpql, Long.class).getSingleResult();
	}

}
