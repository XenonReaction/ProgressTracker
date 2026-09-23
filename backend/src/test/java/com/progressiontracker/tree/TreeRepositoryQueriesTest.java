package com.progressiontracker.tree;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.progressiontracker.TestcontainersConfiguration;
import com.progressiontracker.node.Node;
import com.progressiontracker.user.User;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class TreeRepositoryQueriesTest {

	@Autowired
	private TestEntityManager em;

	@Autowired
	private TreeRepository trees;

	@Autowired
	private TreeNodeRepository treeNodes;

	@Autowired
	private PrerequisiteRepository prerequisites;

	private User alice;

	private User bob;

	private Node oop;

	private Node generics;

	@BeforeEach
	void setUp() {
		alice = em.persist(new User("alice"));
		bob = em.persist(new User("bob"));
		oop = em.persist(new Node(alice, "OOP"));
		generics = em.persist(new Node(alice, "Generics"));
	}

	@Test
	void treesAreScopedToTheirOwnerAndSortedByTitle() {
		Tree spring = em.persist(new Tree(alice, "Spring"));
		Tree java = em.persist(new Tree(alice, "Java"));
		Tree bobsTree = em.persist(new Tree(bob, "Bob's tree"));

		assertThat(trees.findByOwnerOrderByTitleAsc(alice)).containsExactly(java, spring);
		assertThat(trees.findByIdAndOwner(java.getId(), alice)).contains(java);
		assertThat(trees.findByIdAndOwner(bobsTree.getId(), alice)).isEmpty();
	}

	@Test
	void findTreesContainingReturnsOnlyTreesThatUseTheNode() {
		Tree spring = em.persist(new Tree(alice, "Spring"));
		Tree java = em.persist(new Tree(alice, "Java"));
		Tree unrelated = em.persist(new Tree(alice, "Unrelated"));
		em.persist(new TreeNode(spring, oop, 0, 0));
		em.persist(new TreeNode(java, oop, 0, 0));
		em.persist(new TreeNode(unrelated, generics, 0, 0));

		assertThat(trees.findTreesContaining(oop)).containsExactly(java, spring);
		assertThat(trees.findTreesContaining(em.persist(new Node(alice, "Unused")))).isEmpty();
	}

	@Test
	void treeNodeLookupIsScopedToItsTree() {
		Tree java = em.persist(new Tree(alice, "Java"));
		Tree spring = em.persist(new Tree(alice, "Spring"));
		TreeNode inJava = em.persist(new TreeNode(java, oop, 0, 0));

		assertThat(treeNodes.findByIdAndTree(inJava.getId(), java)).contains(inJava);
		assertThat(treeNodes.findByIdAndTree(inJava.getId(), spring)).isEmpty();
		assertThat(treeNodes.existsByTreeAndNode(java, oop)).isTrue();
		assertThat(treeNodes.existsByTreeAndNode(java, generics)).isFalse();
	}

	@Test
	void prerequisiteQueriesAreScopedToTheirTree() {
		Tree java = em.persist(new Tree(alice, "Java"));
		Tree spring = em.persist(new Tree(alice, "Spring"));
		Prerequisite javaEdge = em.persist(new Prerequisite(em.persist(new TreeNode(java, oop, 0, 0)),
				em.persist(new TreeNode(java, generics, 0, 100))));
		Prerequisite springEdge = em.persist(new Prerequisite(em.persist(new TreeNode(spring, oop, 0, 0)),
				em.persist(new TreeNode(spring, generics, 0, 100))));

		assertThat(prerequisites.findByTree(java)).containsExactly(javaEdge);
		assertThat(prerequisites.findByTree(spring)).containsExactly(springEdge);
		assertThat(prerequisites.findByIdAndDependentTree(javaEdge.getId(), java)).contains(javaEdge);
		assertThat(prerequisites.findByIdAndDependentTree(javaEdge.getId(), spring)).isEmpty();
		assertThat(prerequisites.existsByPrerequisiteAndDependent(javaEdge.getPrerequisite(), javaEdge.getDependent()))
			.isTrue();
	}

}
