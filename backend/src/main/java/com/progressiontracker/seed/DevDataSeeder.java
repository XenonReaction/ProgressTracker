package com.progressiontracker.seed;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.node.Node;
import com.progressiontracker.node.NodeLink;
import com.progressiontracker.node.NodeRepository;
import com.progressiontracker.tree.Prerequisite;
import com.progressiontracker.tree.PrerequisiteRepository;
import com.progressiontracker.tree.Tree;
import com.progressiontracker.tree.TreeNode;
import com.progressiontracker.tree.TreeNodeRepository;
import com.progressiontracker.tree.TreeRepository;
import com.progressiontracker.user.CurrentUserService;
import com.progressiontracker.user.User;

/**
 * Loads a small hand-written data set for local development, owned by the default user
 * the API acts as. Runs only with the {@code dev} profile, and never alongside {@code prod}
 * even if both are switched on. It does nothing if that user already has any nodes or
 * trees, so restarting against the persistent docker-compose database doesn't duplicate
 * data.
 * <p>
 * Contents: a ten-node library (one node not in any tree) and three trees. Two of them share
 * the "Object-Oriented Programming" node, and "Collections Framework" takes its readiness
 * from the third.
 */
@Component
@Profile("dev & !prod")
public class DevDataSeeder implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(DevDataSeeder.class);

	private final CurrentUserService currentUser;

	private final NodeRepository nodes;

	private final TreeRepository trees;

	private final TreeNodeRepository treeNodes;

	private final PrerequisiteRepository prerequisites;

	public DevDataSeeder(CurrentUserService currentUser, NodeRepository nodes, TreeRepository trees,
			TreeNodeRepository treeNodes, PrerequisiteRepository prerequisites) {
		this.currentUser = currentUser;
		this.nodes = nodes;
		this.trees = trees;
		this.treeNodes = treeNodes;
		this.prerequisites = prerequisites;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		User user = currentUser.getCurrentUser();
		if (nodes.existsByOwner(user) || trees.existsByOwner(user)) {
			log.info("User '{}' already has data; skipping dev seed", user.getUsername());
			return;
		}

		Node syntax = node(user, "Java Syntax Basics", "Variables, types, operators and control flow.", 95,
				new NodeLink("https://dev.java/learn/language-basics/", "dev.java: Language Basics"));
		Node oop = node(user, "Object-Oriented Programming", "Classes, interfaces, inheritance and polymorphism.", 80,
				new NodeLink("https://dev.java/learn/classes-objects/", "dev.java: Classes and Objects"));
		Node collections = node(user, "Collections Framework", "List, Set, Map and their common implementations.", 65);
		Node generics = node(user, "Generics", "Type parameters, bounded types and wildcards.", 40);
		Node streams = node(user, "Streams API", "Functional-style operations on sequences of elements.", 15);
		Node springCore = node(user, "Spring Core", "Dependency injection and the application context.", 30,
				new NodeLink("https://docs.spring.io/spring-framework/reference/core.html", "Spring Framework: Core"));
		node(user, "Maven Basics", "Library-only node: not placed in any tree.", 50);

		Tree javaTree = tree(user, "Java Fundamentals", "Core language skills, in rough learning order.",
				"Technology", "java", "backend");
		TreeNode jSyntax = place(javaTree, syntax, 0, 0);
		TreeNode jOop = place(javaTree, oop, 0, 150);
		TreeNode jCollections = place(javaTree, collections, -150, 300);
		TreeNode jGenerics = place(javaTree, generics, 150, 300);
		TreeNode jStreams = place(javaTree, streams, 0, 450);
		// Stricter than the 80/70 defaults, to exercise per-node thresholds
		jStreams.setAggregateThreshold(85);
		jStreams.setIndividualThreshold(75);
		edge(jSyntax, jOop);
		edge(jOop, jCollections);
		edge(jOop, jGenerics);
		edge(jCollections, jStreams);
		edge(jGenerics, jStreams);

		Tree springTree = tree(user, "Spring Basics", "Getting started with the Spring Framework.", "Technology",
				"spring", "backend");
		TreeNode sOop = place(springTree, oop, 0, 0);
		TreeNode sSpringCore = place(springTree, springCore, 0, 150);
		edge(sOop, sSpringCore);

		// Collections Framework takes its readiness from a tree of its own: (90 + 70 + 50) / 3 = 70,
		// in place of its hand-entered 65
		Tree collectionsTree = tree(user, "Collections in Depth", "The main collection types, one by one.",
				"Technology", "java");
		TreeNode cList = place(collectionsTree, node(user, "Lists", "ArrayList and LinkedList.", 90), 0, 0);
		TreeNode cMap = place(collectionsTree, node(user, "Maps", "HashMap, TreeMap and LinkedHashMap.", 70), -150, 150);
		TreeNode cSet = place(collectionsTree, node(user, "Sets", "HashSet and TreeSet.", 50), 150, 150);
		edge(cList, cMap);
		edge(cList, cSet);
		collections.setLinkedTree(collectionsTree);

		log.info("Seeded dev data for user '{}'", user.getUsername());
	}

	private Node node(User owner, String title, String description, int readiness, NodeLink... links) {
		Node node = new Node(owner, title);
		node.setDescription(description);
		node.setReadiness(readiness);
		node.getLinks().addAll(List.of(links));
		return nodes.save(node);
	}

	private Tree tree(User owner, String title, String description, String category, String... tags) {
		Tree tree = new Tree(owner, title);
		tree.setDescription(description);
		tree.setCategory(category);
		tree.getTags().addAll(List.of(tags));
		return trees.save(tree);
	}

	private TreeNode place(Tree tree, Node node, double x, double y) {
		return treeNodes.save(new TreeNode(tree, node, x, y));
	}

	private void edge(TreeNode prerequisite, TreeNode dependent) {
		prerequisites.save(new Prerequisite(prerequisite, dependent));
	}

}
