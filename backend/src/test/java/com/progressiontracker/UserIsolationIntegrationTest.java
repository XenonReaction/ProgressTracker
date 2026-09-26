package com.progressiontracker;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
 * Phase 7.7: one user never sees, uses or changes another's data, in any module, and another
 * user's activity never counts toward your readiness.
 * <p>
 * Every request acts as the configured default user, so the other user's data is made by
 * that user and then handed to "alice" by renaming them; the next request creates a new
 * default user who owns nothing.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class UserIsolationIntegrationTest {

	@Autowired
	private MockMvcTester mvc;

	@Autowired
	private JdbcTemplate jdbc;

	@AfterEach
	void emptyTables() {
		jdbc.execute("truncate table question_attempts, coding_questions, question_sets, lesson_progress_updates, "
				+ "lesson_opens, lesson_sections, lessons, material_progress_updates, materials, card_reviews, cards, "
				+ "decks, tree_edit_sessions, prerequisites, tree_nodes, tree_tags, trees, node_tags, node_resources, "
				+ "nodes, users cascade");
	}

	@Test
	void anotherUsersDataCannotBeSeenUsedOrChangedInAnyModule() {
		// Alice's library: a tree of two linked nodes, and one of each learning resource
		long tree = id(post("/api/v1/trees", "{\"title\": \"Alice's tree\"}"));
		long node = createNode("Alice's node");
		long treeNode = place(tree, node);
		long edge = id(post("/api/v1/trees/" + tree + "/prerequisites",
				"{\"prerequisiteTreeNodeId\": %d, \"dependentTreeNodeId\": %d}".formatted(treeNode,
						place(tree, createNode("Alice's other node")))));
		long deck = id(post("/api/v1/decks", "{\"title\": \"Alice's deck\"}"));
		long card = id(post("/api/v1/decks/" + deck + "/cards", "{\"front\": \"Q\", \"back\": \"A\"}"));
		long material = id(post("/api/v1/materials", "{\"title\": \"Alice's guide\", \"url\": \"https://example.com\"}"));
		long lesson = id(post("/api/v1/lessons", "{\"title\": \"Alice's lesson\"}"));
		long set = id(post("/api/v1/question-sets", "{\"title\": \"Alice's set\"}"));
		long question = id(post("/api/v1/question-sets/" + set + "/questions", """
				{"title": "Q", "language": "css", "problem": "P", "solution": "S"}"""));
		String[] reads = { "/api/v1/nodes/" + node, "/api/v1/nodes/" + node + "/trees", "/api/v1/trees/" + tree,
				"/api/v1/trees/" + tree + "/nodes", "/api/v1/trees/" + tree + "/nodes/" + treeNode,
				"/api/v1/trees/" + tree + "/prerequisites", "/api/v1/decks/" + deck, "/api/v1/decks/" + deck + "/cards",
				"/api/v1/review-queue?deckId=" + deck, "/api/v1/materials/" + material,
				"/api/v1/materials/" + material + "/progress", "/api/v1/lessons/" + lesson, "/api/v1/question-sets/" + set,
				"/api/v1/question-sets/" + set + "/questions/" + question };
		for (String uri : reads) {
			assertThat(mvc.get().uri(uri)).as("Alice: GET " + uri).hasStatusOk();
		}
		handToAlice();

		// Nothing of hers is listed
		for (String list : new String[] { "/api/v1/nodes", "/api/v1/trees", "/api/v1/decks", "/api/v1/review-queue",
				"/api/v1/materials", "/api/v1/lessons", "/api/v1/question-sets" }) {
			assertThat(mvc.get().uri(list)).as(list).bodyJson().extractingPath("$").asArray().isEmpty();
		}

		// Nothing of hers can be read: each is "not found", not "forbidden"
		for (String uri : reads) {
			assertNotFound(mvc.get().uri(uri).exchange());
		}

		// Nothing of hers can be recorded against, changed or deleted
		assertNotFound(post("/api/v1/decks/" + deck + "/cards/" + card + "/reviews", "{\"correct\": true}"));
		assertNotFound(post("/api/v1/decks/" + deck + "/cards", "{\"front\": \"Q\", \"back\": \"A\"}"));
		assertNotFound(post("/api/v1/materials/" + material + "/progress", "{\"progress\": 50}"));
		assertNotFound(post("/api/v1/lessons/" + lesson + "/opens", ""));
		assertNotFound(post("/api/v1/lessons/" + lesson + "/progress", "{\"progress\": 50}"));
		assertNotFound(post("/api/v1/question-sets/" + set + "/questions/" + question + "/reveal", ""));
		assertNotFound(post("/api/v1/question-sets/" + set + "/questions/" + question + "/solved", ""));
		assertNotFound(post("/api/v1/trees/" + tree + "/nodes",
				"{\"nodeId\": %d, \"positionX\": 0, \"positionY\": 0}".formatted(createNode("Mine"))));
		assertNotFound(post("/api/v1/trees/" + tree + "/edit-session", ""));
		assertNotFound(put("/api/v1/nodes/" + node, "{\"title\": \"Taken\", \"readiness\": 0}"));
		assertNotFound(put("/api/v1/nodes/" + node + "/readiness", "{\"readiness\": 100}"));
		assertNotFound(put("/api/v1/trees/" + tree, "{\"title\": \"Taken\"}"));
		assertNotFound(put("/api/v1/trees/" + tree + "/nodes/" + treeNode, """
				{"positionX": 0, "positionY": 0, "aggregateThreshold": 80, "individualThreshold": 70}"""));
		assertNotFound(put("/api/v1/trees/" + tree + "/prerequisites/" + edge + "/route",
				"{\"route\": {\"segments\": 3, \"offsets\": [10]}}"));
		assertNotFound(put("/api/v1/decks/" + deck, "{\"title\": \"Taken\"}"));
		assertNotFound(put("/api/v1/decks/" + deck + "/cards/" + card, "{\"front\": \"Q\", \"back\": \"A\"}"));
		assertNotFound(put("/api/v1/materials/" + material, "{\"title\": \"Taken\", \"url\": \"https://example.com\"}"));
		assertNotFound(put("/api/v1/lessons/" + lesson, "{\"title\": \"Taken\"}"));
		assertNotFound(put("/api/v1/question-sets/" + set, "{\"title\": \"Taken\"}"));
		for (String uri : new String[] { "/api/v1/trees/" + tree + "/prerequisites/" + edge,
				"/api/v1/trees/" + tree + "/nodes/" + treeNode, "/api/v1/decks/" + deck + "/cards/" + card,
				"/api/v1/question-sets/" + set + "/questions/" + question, "/api/v1/nodes/" + node, "/api/v1/trees/" + tree,
				"/api/v1/decks/" + deck, "/api/v1/materials/" + material, "/api/v1/lessons/" + lesson,
				"/api/v1/question-sets/" + set }) {
			assertNotFound(mvc.delete().uri(uri).exchange());
		}

		// None of hers can be a resource of one of my nodes
		long mine = createNode("My node");
		for (String resource : new String[] { "{\"type\": \"tree\", \"treeId\": %d}".formatted(tree),
				"{\"type\": \"deck\", \"deckId\": %d}".formatted(deck),
				"{\"type\": \"material\", \"materialId\": %d}".formatted(material),
				"{\"type\": \"lesson\", \"lessonId\": %d}".formatted(lesson),
				"{\"type\": \"question_set\", \"questionSetId\": %d}".formatted(set) }) {
			assertNotFound(put("/api/v1/nodes/" + mine,
					"{\"title\": \"My node\", \"readiness\": 0, \"resources\": [%s]}".formatted(resource)));
		}

		// And all of it is still there, unchanged
		assertThat(jdbc.queryForObject("select count(*) from card_reviews", Integer.class)).isZero();
		assertThat(jdbc.queryForObject("select title from nodes where id = ?", String.class, node))
			.isEqualTo("Alice's node");
		assertThat(jdbc.queryForObject("select count(*) from tree_edit_sessions", Integer.class)).isZero();
	}

	@Test
	void anotherUsersActivityNeverCountsTowardMyReadiness() {
		long deck = id(post("/api/v1/decks", "{\"title\": \"Flexbox cards\"}"));
		long card = id(post("/api/v1/decks/" + deck + "/cards", "{\"front\": \"Q\", \"back\": \"A\"}"));
		long lesson = id(post("/api/v1/lessons", "{\"title\": \"Flexbox lesson\"}"));
		long set = id(post("/api/v1/question-sets", "{\"title\": \"Flexbox exercises\"}"));
		long question = id(post("/api/v1/question-sets/" + set + "/questions", """
				{"title": "Q", "language": "css", "problem": "P", "solution": "S"}"""));
		long node = id(post("/api/v1/nodes", """
				{"title": "CSS Flexbox", "readiness": 0, "resources": [
				 {"type": "deck", "deckId": %d, "counts": true},
				 {"type": "lesson", "lessonId": %d, "counts": true},
				 {"type": "question_set", "questionSetId": %d, "counts": true}]}""".formatted(deck, lesson, set)));

		// Alice answers my card right three times, finishes my lesson and solves my question
		long alice = jdbc.queryForObject("insert into users (username, created_at) values ('alice', now()) returning id",
				Long.class);
		Timestamp earlier = Timestamp.from(Instant.now().minus(1, ChronoUnit.HOURS));
		for (int i = 0; i < 3; i++) {
			jdbc.update("insert into card_reviews (card_id, user_id, reviewed_at, correct) values (?, ?, ?, true)", card,
					alice, earlier);
		}
		jdbc.update("insert into lesson_progress_updates (lesson_id, user_id, recorded_at, progress) values (?, ?, ?, 100)",
				lesson, alice, earlier);
		jdbc.update("insert into lesson_opens (lesson_id, user_id, opened_at) values (?, ?, ?)", lesson, alice, earlier);
		jdbc.update("insert into question_attempts (question_id, user_id, recorded_at, action) values (?, ?, ?, 'solved')",
				question, alice, earlier);

		// None of it is mine: nothing passed, entered, opened or solved
		MvcTestResult cards = mvc.get().uri("/api/v1/decks/{id}/cards", deck).exchange();
		assertThat(cards).bodyJson().extractingPath("$[0].passed").isEqualTo(false);
		assertThat(cards).bodyJson().extractingPath("$[0].lastReviewedAt").isNull();
		assertThat(mvc.get().uri("/api/v1/review-queue")).bodyJson().extractingPath("$[*].id").asArray()
			.containsExactly((int) card);
		assertThat(mvc.get().uri("/api/v1/lessons/{id}", lesson)).bodyJson().extractingPath("$.progress").isEqualTo(0);
		assertThat(mvc.get().uri("/api/v1/question-sets/{id}", set)).bodyJson().extractingPath("$.readiness").isEqualTo(0);
		MvcTestResult mine = mvc.get().uri("/api/v1/nodes/{id}", node).exchange();
		assertThat(mine).bodyJson().extractingPath("$.readiness").isEqualTo(0);
		assertThat(mine).bodyJson().extractingPath("$.lastReviewedAt").isNull();
		assertThat(mine).bodyJson().extractingPath("$.resources[*].readiness").asArray().containsExactly(0, 0, 0);
		assertThat(mvc.get().uri("/api/v1/nodes")).bodyJson().extractingPath("$[0].readiness").isEqualTo(0);
	}

	/** Gives everything made so far to "alice": the next request creates a new default user. */
	private void handToAlice() {
		assertThat(jdbc.update("update users set username = 'alice' where username = 'demo'")).isEqualTo(1);
	}

	private long createNode(String title) {
		return id(post("/api/v1/nodes", "{\"title\": \"%s\", \"readiness\": 0}".formatted(title)));
	}

	private long place(long tree, long node) {
		return id(post("/api/v1/trees/" + tree + "/nodes",
				"{\"nodeId\": %d, \"positionX\": 0, \"positionY\": 0}".formatted(node)));
	}

	/**
	 * A 404 from the app's own ownership check ("Deck 7 not found"), not from a route that
	 * doesn't exist.
	 */
	private static void assertNotFound(MvcTestResult result) {
		String request = result.getRequest().getMethod() + " " + result.getRequest().getRequestURI();
		assertThat(result).as(request).hasStatus(HttpStatus.NOT_FOUND);
		assertThat((String) JsonPath.read(body(result), "$.detail")).as(request).matches(".* \\d+ not found");
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
		return ((Number) JsonPath.read(body(result), "$.id")).longValue();
	}

	private static String body(MvcTestResult result) {
		try {
			return result.getResponse().getContentAsString();
		}
		catch (UnsupportedEncodingException ex) {
			throw new IllegalStateException(ex);
		}
	}

}
