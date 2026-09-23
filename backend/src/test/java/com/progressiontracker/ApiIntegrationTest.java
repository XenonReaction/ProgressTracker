package com.progressiontracker;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;

import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

/**
 * Drives the REST API through the full stack against real Postgres. Not transactional,
 * so every request commits like it would in production; tables are emptied after each
 * test instead.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class ApiIntegrationTest {

	@Autowired
	private MockMvcTester mvc;

	@Autowired
	private JdbcTemplate jdbc;

	@AfterEach
	void emptyTables() {
		jdbc.execute("truncate table prerequisites, tree_nodes, tree_tags, trees, node_links, nodes, users cascade");
	}

	@Test
	void buildsATreeAndEnforcesItsRules() {
		long syntax = createNode("Java Syntax", 90);
		long oop = createNode("OOP", 60);
		long streams = createNode("Streams", 10);
		long tree = id(post("/api/v1/trees", """
				{"title": "Java", "category": "Technology", "tags": ["java", "backend"]}"""));

		long tSyntax = placeNode(tree, syntax, "");
		long tOop = placeNode(tree, oop, ", \"aggregateThreshold\": 90, \"individualThreshold\": 85");
		long tStreams = placeNode(tree, streams, "");
		assertThat(addEdge(tree, tSyntax, tOop)).hasStatus(HttpStatus.CREATED);
		assertThat(addEdge(tree, tOop, tStreams)).hasStatus(HttpStatus.CREATED);

		// Tree view: each tree node carries its library data, thresholds and both edge directions
		MvcTestResult treeNodes = mvc.get().uri("/api/v1/trees/{tree}/nodes", tree).exchange();
		assertThat(treeNodes).hasStatusOk();
		assertThat(treeNodes).bodyJson().extractingPath("$[1].title").isEqualTo("OOP");
		assertThat(treeNodes).bodyJson().extractingPath("$[1].readiness").isEqualTo(60);
		assertThat(treeNodes).bodyJson().extractingPath("$[1].aggregateThreshold").isEqualTo(90);
		assertThat(treeNodes).bodyJson().extractingPath("$[1].individualThreshold").isEqualTo(85);
		assertThat(treeNodes).bodyJson().extractingPath("$[0].aggregateThreshold").isEqualTo(80);
		assertThat(treeNodes).bodyJson().extractingPath("$[1].prerequisiteIds").asArray().containsExactly((int) tSyntax);
		assertThat(treeNodes).bodyJson().extractingPath("$[1].dependentIds").asArray().containsExactly((int) tStreams);

		// Closing the loop Streams -> Syntax is refused
		MvcTestResult cycle = addEdge(tree, tStreams, tSyntax);
		assertThat(cycle).hasStatus(HttpStatus.CONFLICT)
			.hasContentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON);
		assertThat(cycle).bodyJson().extractingPath("$.detail").asString().contains("would create a cycle");

		// A node still used in a tree can't be deleted; the response says where it's used
		MvcTestResult inUse = mvc.delete().uri("/api/v1/nodes/{id}", oop).exchange();
		assertThat(inUse).hasStatus(HttpStatus.CONFLICT);
		assertThat(inUse).bodyJson().extractingPath("$.trees[0].id").isEqualTo((int) tree);
		assertThat(inUse).bodyJson().extractingPath("$.trees[0].title").isEqualTo("Java");

		// Deleting the tree removes its placements and edges, which frees the library node
		assertThat(mvc.delete().uri("/api/v1/trees/{id}", tree)).hasStatus(HttpStatus.NO_CONTENT);
		assertThat(mvc.get().uri("/api/v1/trees/{id}/nodes", tree)).hasStatus(HttpStatus.NOT_FOUND);
		assertThat(mvc.delete().uri("/api/v1/nodes/{id}", oop)).hasStatus(HttpStatus.NO_CONTENT);
		assertThat(mvc.get().uri("/api/v1/nodes")).bodyJson().extractingPath("$[*].title").asArray()
			.containsExactly("Java Syntax", "Streams");
	}

	@Test
	void updatesANodeAndReturnsTheNewState() {
		long node = createNode("Generics", 10);

		MvcTestResult updated = put("/api/v1/nodes/" + node, """
				{"title": "Generics & Wildcards", "readiness": 75,
				 "links": [{"url": "https://dev.java/learn/generics/", "label": "dev.java"}]}""");

		assertThat(updated).hasStatusOk();
		assertThat(updated).bodyJson().extractingPath("$.title").isEqualTo("Generics & Wildcards");
		assertThat(updated).bodyJson().extractingPath("$.readiness").isEqualTo(75);
		assertThat(updated).bodyJson().extractingPath("$.links[0].label").isEqualTo("dev.java");
		assertThat(mvc.get().uri("/api/v1/nodes/{id}", node)).bodyJson().extractingPath("$.readiness").isEqualTo(75);
	}

	@Test
	void invalidRequestsListEveryBadField() {
		MvcTestResult result = post("/api/v1/nodes", """
				{"title": "", "readiness": 150, "links": [{"url": "not a url"}]}""");

		assertThat(result).hasStatus(HttpStatus.BAD_REQUEST)
			.hasContentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON);
		assertThat(result).bodyJson().extractingPath("$.errors[*].field").asArray()
			.containsExactlyInAnyOrder("title", "readiness", "links[0].url");
	}

	@Test
	void malformedJsonIsABadRequest() {
		assertThat(post("/api/v1/trees", "{not json")).hasStatus(HttpStatus.BAD_REQUEST);
	}

	@Test
	void unknownIdsAreNotFound() {
		long tree = id(post("/api/v1/trees", "{\"title\": \"Java\"}"));

		assertThat(mvc.get().uri("/api/v1/nodes/999999")).hasStatus(HttpStatus.NOT_FOUND);
		assertThat(mvc.get().uri("/api/v1/trees/999999")).hasStatus(HttpStatus.NOT_FOUND);
		assertThat(mvc.get().uri("/api/v1/trees/{tree}/nodes/999999", tree)).hasStatus(HttpStatus.NOT_FOUND);
		assertThat(post("/api/v1/trees/" + tree + "/nodes", """
				{"nodeId": 999999, "positionX": 0, "positionY": 0}""")).hasStatus(HttpStatus.NOT_FOUND);
	}

	@Test
	void sameNodeTwiceInATreeIsAConflict() {
		long node = createNode("OOP", 50);
		long tree = id(post("/api/v1/trees", "{\"title\": \"Java\"}"));
		placeNode(tree, node, "");

		assertThat(post("/api/v1/trees/" + tree + "/nodes", """
				{"nodeId": %d, "positionX": 10, "positionY": 10}""".formatted(node))).hasStatus(HttpStatus.CONFLICT);
	}

	@Test
	void edgesCannotCrossTrees() {
		long oop = createNode("OOP", 50);
		long generics = createNode("Generics", 50);
		long java = id(post("/api/v1/trees", "{\"title\": \"Java\"}"));
		long spring = id(post("/api/v1/trees", "{\"title\": \"Spring\"}"));
		long inJava = placeNode(java, oop, "");
		long inSpring = placeNode(spring, generics, "");

		assertThat(addEdge(java, inJava, inSpring)).hasStatus(HttpStatus.NOT_FOUND);
	}

	@Test
	void selfEdgeIsABadRequest() {
		long node = createNode("OOP", 50);
		long tree = id(post("/api/v1/trees", "{\"title\": \"Java\"}"));
		long treeNode = placeNode(tree, node, "");

		assertThat(addEdge(tree, treeNode, treeNode)).hasStatus(HttpStatus.BAD_REQUEST);
	}

	@Test
	void savesManyPositionsAtOnceOrNoneAtAll() {
		long tree = id(post("/api/v1/trees", "{\"title\": \"Java\"}"));
		long first = placeNode(tree, createNode("OOP", 50), "");
		long second = placeNode(tree, createNode("Generics", 50), "");

		MvcTestResult moved = put("/api/v1/trees/" + tree + "/nodes/positions", """
				{"positions": [{"treeNodeId": %d, "positionX": 100, "positionY": 0},
				               {"treeNodeId": %d, "positionX": 100, "positionY": 150}]}""".formatted(first, second));
		assertThat(moved).hasStatusOk();
		assertThat(moved).bodyJson().extractingPath("$[*].positionY").asArray().containsExactly(0.0, 150.0);

		// One unknown id: nothing moves
		MvcTestResult rejected = put("/api/v1/trees/" + tree + "/nodes/positions", """
				{"positions": [{"treeNodeId": %d, "positionX": 999, "positionY": 999},
				               {"treeNodeId": 999999, "positionX": 0, "positionY": 0}]}""".formatted(first));
		assertThat(rejected).hasStatus(HttpStatus.NOT_FOUND);
		assertThat(mvc.get().uri("/api/v1/trees/{tree}/nodes/{id}", tree, first)).bodyJson()
			.extractingPath("$.positionX")
			.isEqualTo(100.0);
	}

	private long createNode(String title, int readiness) {
		return id(post("/api/v1/nodes", """
				{"title": "%s", "readiness": %d}""".formatted(title, readiness)));
	}

	private long placeNode(long tree, long node, String extraFields) {
		return id(post("/api/v1/trees/" + tree + "/nodes", """
				{"nodeId": %d, "positionX": 0, "positionY": 0%s}""".formatted(node, extraFields)));
	}

	private MvcTestResult addEdge(long tree, long prerequisite, long dependent) {
		return post("/api/v1/trees/" + tree + "/prerequisites", """
				{"prerequisiteTreeNodeId": %d, "dependentTreeNodeId": %d}""".formatted(prerequisite, dependent));
	}

	private MvcTestResult post(String uri, String json) {
		return mvc.post().uri(uri).contentType(MediaType.APPLICATION_JSON).content(json).exchange();
	}

	private MvcTestResult put(String uri, String json) {
		return mvc.put().uri(uri).contentType(MediaType.APPLICATION_JSON).content(json).exchange();
	}

	/** Asserts a 201 and returns the created resource's id. */
	private long id(MvcTestResult result) {
		assertThat(result).hasStatus(HttpStatus.CREATED);
		try {
			return ((Number) JsonPath.read(result.getResponse().getContentAsString(), "$.id")).longValue();
		}
		catch (UnsupportedEncodingException ex) {
			throw new IllegalStateException(ex);
		}
	}

}
