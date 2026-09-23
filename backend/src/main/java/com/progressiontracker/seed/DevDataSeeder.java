package com.progressiontracker.seed;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.node.Node;
import com.progressiontracker.node.NodeLink;
import com.progressiontracker.tree.Prerequisite;
import com.progressiontracker.tree.Tree;
import com.progressiontracker.tree.TreeNode;
import com.progressiontracker.user.User;

/**
 * Loads a small hand-written data set for local development. Runs only with the
 * {@code dev} profile, and does nothing if the demo user already exists, so restarting
 * against the persistent docker-compose database doesn't duplicate data.
 * <p>
 * Contents: one user, a seven-node library (one node not in any tree), and two trees
 * that share the "Object-Oriented Programming" node.
 */
@Component
@Profile("dev")
public class DevDataSeeder implements ApplicationRunner {

	public static final String DEMO_USERNAME = "demo";

	private static final Logger log = LoggerFactory.getLogger(DevDataSeeder.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		boolean alreadySeeded = !em.createQuery("select u.id from User u where u.username = :username", Long.class)
			.setParameter("username", DEMO_USERNAME)
			.getResultList()
			.isEmpty();
		if (alreadySeeded) {
			log.info("Dev seed data already present; skipping");
			return;
		}

		User demo = persist(new User(DEMO_USERNAME));

		Node syntax = node(demo, "Java Syntax Basics", "Variables, types, operators and control flow.", 95,
				new NodeLink("https://dev.java/learn/language-basics/", "dev.java: Language Basics"));
		Node oop = node(demo, "Object-Oriented Programming", "Classes, interfaces, inheritance and polymorphism.", 80,
				new NodeLink("https://dev.java/learn/classes-objects/", "dev.java: Classes and Objects"));
		Node collections = node(demo, "Collections Framework", "List, Set, Map and their common implementations.", 65);
		Node generics = node(demo, "Generics", "Type parameters, bounded types and wildcards.", 40);
		Node streams = node(demo, "Streams API", "Functional-style operations on sequences of elements.", 15);
		Node springCore = node(demo, "Spring Core", "Dependency injection and the application context.", 30,
				new NodeLink("https://docs.spring.io/spring-framework/reference/core.html", "Spring Framework: Core"));
		node(demo, "Maven Basics", "Library-only node: not placed in any tree.", 50);

		Tree javaTree = tree(demo, "Java Fundamentals", "Core language skills, in rough learning order.",
				"Technology", "java", "backend");
		TreeNode jSyntax = persist(new TreeNode(javaTree, syntax, 0, 0));
		TreeNode jOop = persist(new TreeNode(javaTree, oop, 0, 150));
		TreeNode jCollections = persist(new TreeNode(javaTree, collections, -150, 300));
		TreeNode jGenerics = persist(new TreeNode(javaTree, generics, 150, 300));
		TreeNode jStreams = persist(new TreeNode(javaTree, streams, 0, 450));
		// Stricter than the 80/70 defaults, to exercise per-node thresholds
		jStreams.setAggregateThreshold(85);
		jStreams.setIndividualThreshold(75);
		edge(jSyntax, jOop);
		edge(jOop, jCollections);
		edge(jOop, jGenerics);
		edge(jCollections, jStreams);
		edge(jGenerics, jStreams);

		Tree springTree = tree(demo, "Spring Basics", "Getting started with the Spring Framework.", "Technology",
				"spring", "backend");
		TreeNode sOop = persist(new TreeNode(springTree, oop, 0, 0));
		TreeNode sSpringCore = persist(new TreeNode(springTree, springCore, 0, 150));
		edge(sOop, sSpringCore);

		log.info("Seeded dev data for user '{}'", DEMO_USERNAME);
	}

	private Node node(User owner, String title, String description, int readiness, NodeLink... links) {
		Node node = new Node(owner, title);
		node.setDescription(description);
		node.setReadiness(readiness);
		node.getLinks().addAll(List.of(links));
		return persist(node);
	}

	private Tree tree(User owner, String title, String description, String category, String... tags) {
		Tree tree = new Tree(owner, title);
		tree.setDescription(description);
		tree.setCategory(category);
		tree.getTags().addAll(List.of(tags));
		return persist(tree);
	}

	private void edge(TreeNode prerequisite, TreeNode dependent) {
		persist(new Prerequisite(prerequisite, dependent));
	}

	private <T> T persist(T entity) {
		em.persist(entity);
		return entity;
	}

}
