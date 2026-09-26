package com.progressiontracker;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import com.jayway.jsonpath.JsonPath;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import jakarta.persistence.EntityManagerFactory;

/**
 * Phase 7.7 speed check: loads a large library, then times the node list and tree pages and
 * counts their database queries. The numbers are logged and recorded in the Phase 7 plan.
 * The time limit is loose, so the test catches a page becoming far slower, not normal noise;
 * the query limit catches resources being read one at a time again.
 * <p>
 * The data set: {@value #TREES} trees of {@value #NODES_PER_TREE} nodes each, chained by
 * prerequisites. Every node counts one resource, taking turns: a deck of
 * {@value #CARDS_PER_DECK} cards with {@value #ANSWERS_PER_CARD} answers each, a material
 * with {@value #UPDATES} progress reports, a lesson with {@value #UPDATES} progress entries
 * and opens, or a set of {@value #QUESTIONS_PER_SET} coding questions, half solved. The first
 * node of each tree but the last also counts the next tree, so readiness nests
 * {@value #TREES} levels deep.
 */
@SpringBootTest(properties = { "spring.jpa.properties.hibernate.generate_statistics=true",
		"logging.level.org.hibernate.engine.internal.StatisticalLoggingSessionEventListener=WARN" })
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class ReadinessSpeedCheckTest {

	private static final Logger log = LoggerFactory.getLogger(ReadinessSpeedCheckTest.class);

	private static final int TREES = 10;

	private static final int NODES_PER_TREE = 30;

	private static final int CARDS_PER_DECK = 20;

	private static final int ANSWERS_PER_CARD = 5;

	private static final int UPDATES = 5;

	private static final int QUESTIONS_PER_SET = 10;

	private static final int RUNS = 5;

	@Autowired
	private MockMvcTester mvc;

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@AfterEach
	void emptyTables() {
		jdbc.execute("truncate table question_attempts, coding_questions, question_sets, lesson_progress_updates, "
				+ "lesson_opens, lesson_sections, lessons, material_progress_updates, materials, card_reviews, cards, "
				+ "decks, tree_edit_sessions, prerequisites, tree_nodes, tree_tags, trees, node_tags, node_resources, "
				+ "nodes, users cascade");
	}

	@Test
	void theNodeListAndTreePagesStayQuickWithALargeLibrary() {
		List<Long> trees = loadLargeLibrary();
		long top = trees.get(0);

		Measurement nodeList = measure("/api/v1/nodes");
		Measurement treeList = measure("/api/v1/trees");
		Measurement tree = measure("/api/v1/trees/" + top);
		Measurement treeNodes = measure("/api/v1/trees/" + top + "/nodes");
		Measurement edges = measure("/api/v1/trees/" + top + "/prerequisites");
		log.info("Speed check ({} nodes in {} trees): {}", TREES * NODES_PER_TREE, TREES,
				String.join("; ", nodeList.toString(), treeList.toString(), tree.toString(), treeNodes.toString(),
						edges.toString()));

		// Resources are read in a batch per type, so the queries don't grow with the library
		// (about 30 to 40 here, against about 1,500 when each resource was read on its own)
		for (Measurement page : List.of(nodeList, treeList, tree, treeNodes, edges)) {
			assertThat(page.queries()).as(page.uri() + " queries").isLessThan(100);
			assertThat(page.medianMillis()).as(page.uri() + " median ms").isLessThan(2_000);
		}
	}

	@Test
	void theBatchedNodeListAgreesWithEachNodeReadOnItsOwn() {
		loadLargeLibrary();

		List<Object> listed = JsonPath.read(body(mvc.get().uri("/api/v1/nodes").exchange()), "$[*]");
		assertThat(listed).hasSize(TREES * NODES_PER_TREE);
		for (Object node : listed) {
			Object alone = JsonPath.read(body(mvc.get().uri("/api/v1/nodes/{id}", (Object) JsonPath.read(node, "$.id")).exchange()),
					"$");
			for (String field : List.of("$.readiness", "$.lastReviewedAt", "$.reviewDue", "$.resources")) {
				assertThat((Object) JsonPath.read(node, field)).as(field).isEqualTo(JsonPath.read(alone, field));
			}
		}
	}

	/** Builds the library through the API, and the answers and progress with plain inserts. Returns the tree ids. */
	private List<Long> loadLargeLibrary() {
		List<Long> trees = new ArrayList<>();
		for (int t = 0; t < TREES; t++) {
			trees.add(id(post("/api/v1/trees", "{\"title\": \"Tree %d\"}".formatted(t))));
		}
		long user = jdbc.queryForObject("select id from users where username = 'demo'", Long.class);
		Instant start = Instant.now().minus(30, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MICROS);
		for (int t = 0; t < TREES; t++) {
			Long previous = null;
			for (int n = 0; n < NODES_PER_TREE; n++) {
				String label = "%d.%d".formatted(t, n);
				StringJoiner resources = new StringJoiner(", ");
				resources.add(learningResource(t * NODES_PER_TREE + n, label, user, start));
				if (n == 0 && t + 1 < TREES) {
					resources.add("{\"type\": \"tree\", \"treeId\": %d, \"counts\": true}".formatted(trees.get(t + 1)));
				}
				resources.add("{\"type\": \"url\", \"url\": \"https://example.com/%s\"}".formatted(label));
				long node = id(post("/api/v1/nodes", """
						{"title": "Node %s", "readiness": 0, "tags": ["speed"], "resources": [%s]}"""
					.formatted(label, resources)));
				long treeNode = id(post("/api/v1/trees/" + trees.get(t) + "/nodes",
						"{\"nodeId\": %d, \"positionX\": %d, \"positionY\": %d}".formatted(node, n % 5 * 200, n / 5 * 120)));
				if (previous != null) {
					id(post("/api/v1/trees/" + trees.get(t) + "/prerequisites",
							"{\"prerequisiteTreeNodeId\": %d, \"dependentTreeNodeId\": %d}".formatted(previous, treeNode)));
				}
				previous = treeNode;
			}
		}
		return trees;
	}

	/** Creates the index-th node's learning resource, with its history, and returns it as request JSON. */
	private String learningResource(int index, String label, long user, Instant start) {
		switch (index % 4) {
			case 0 -> {
				long deck = id(post("/api/v1/decks", "{\"title\": \"Deck %s\"}".formatted(label)));
				for (int c = 0; c < CARDS_PER_DECK; c++) {
					long card = id(post("/api/v1/decks/" + deck + "/cards",
							"{\"front\": \"Q%d\", \"back\": \"A%d\"}".formatted(c, c)));
					List<Object[]> answers = new ArrayList<>();
					for (int a = 0; a < ANSWERS_PER_CARD; a++) {
						answers.add(new Object[] { card, user, at(start, c * ANSWERS_PER_CARD + a), (c + a) % 3 != 0 });
					}
					jdbc.batchUpdate("insert into card_reviews (card_id, user_id, reviewed_at, correct) values (?, ?, ?, ?)",
							answers);
				}
				return "{\"type\": \"deck\", \"deckId\": %d, \"counts\": true}".formatted(deck);
			}
			case 1 -> {
				long material = id(post("/api/v1/materials",
						"{\"title\": \"Material %s\", \"url\": \"https://example.com/m/%s\"}".formatted(label, label)));
				jdbc.batchUpdate(
						"insert into material_progress_updates (material_id, user_id, recorded_at, progress) values (?, ?, ?, ?)",
						history(material, user, start));
				return "{\"type\": \"material\", \"materialId\": %d, \"counts\": true}".formatted(material);
			}
			case 2 -> {
				long lesson = id(post("/api/v1/lessons", """
						{"title": "Lesson %s", "sections": [{"title": "One", "body": "Text"}]}""".formatted(label)));
				jdbc.batchUpdate(
						"insert into lesson_progress_updates (lesson_id, user_id, recorded_at, progress) values (?, ?, ?, ?)",
						history(lesson, user, start));
				jdbc.batchUpdate("insert into lesson_opens (lesson_id, user_id, opened_at) values (?, ?, ?)",
						history(lesson, user, start).stream().map(row -> Arrays.copyOf(row, 3)).toList());
				return "{\"type\": \"lesson\", \"lessonId\": %d, \"counts\": true}".formatted(lesson);
			}
			default -> {
				long set = id(post("/api/v1/question-sets", "{\"title\": \"Set %s\"}".formatted(label)));
				List<Object[]> attempts = new ArrayList<>();
				for (int q = 0; q < QUESTIONS_PER_SET; q++) {
					long question = id(post("/api/v1/question-sets/" + set + "/questions", """
							{"title": "Q%d", "language": "css", "problem": "Do it", "solution": "Done"}""".formatted(q)));
					if (q % 2 == 0) {
						attempts.add(new Object[] { question, user, at(start, q), "solved" });
					}
				}
				jdbc.batchUpdate(
						"insert into question_attempts (question_id, user_id, recorded_at, action) values (?, ?, ?, ?)",
						attempts);
				return "{\"type\": \"question_set\", \"questionSetId\": %d, \"counts\": true}".formatted(set);
			}
		}
	}

	/** {@value #UPDATES} rising progress reports for a material or lesson. */
	private static List<Object[]> history(long target, long user, Instant start) {
		List<Object[]> rows = new ArrayList<>();
		for (int u = 0; u < UPDATES; u++) {
			rows.add(new Object[] { target, user, at(start, u), (u + 1) * 15 });
		}
		return rows;
	}

	private static Timestamp at(Instant start, int minutes) {
		return Timestamp.from(start.plus(minutes, ChronoUnit.MINUTES));
	}

	/** One warm-up request, then {@value #RUNS} timed ones, counting the queries of the last. */
	private Measurement measure(String uri) {
		assertThat(mvc.get().uri(uri)).hasStatusOk();
		Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
		long[] millis = new long[RUNS];
		long queries = 0;
		for (int run = 0; run < RUNS; run++) {
			statistics.clear();
			long started = System.nanoTime();
			assertThat(mvc.get().uri(uri)).hasStatusOk();
			millis[run] = (System.nanoTime() - started) / 1_000_000;
			queries = statistics.getPrepareStatementCount();
		}
		Arrays.sort(millis);
		return new Measurement(uri, millis[RUNS / 2], queries);
	}

	private record Measurement(String uri, long medianMillis, long queries) {

		@Override
		public String toString() {
			return "%s %d ms, %d queries".formatted(uri, medianMillis, queries);
		}

	}

	private MvcTestResult post(String uri, String json) {
		return mvc.post().uri(uri).contentType(MediaType.APPLICATION_JSON).content(json).exchange();
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
