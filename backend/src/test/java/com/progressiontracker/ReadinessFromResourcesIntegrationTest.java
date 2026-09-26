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
 * Phases 7.3 to 7.6: readiness from several resources across modules (trees, decks, materials,
 * lessons and coding question sets), through the whole HTTP stack against real Postgres. Holds the Phase 7.0
 * examples.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class ReadinessFromResourcesIntegrationTest {

	@Autowired
	private MockMvcTester mvc;

	@Autowired
	private JdbcTemplate jdbc;

	@AfterEach
	void emptyTables() {
		jdbc.execute("truncate table question_attempts, coding_questions, question_sets, lesson_progress_updates, lesson_opens, "
				+ "lesson_sections, lessons, material_progress_updates, "
				+ "materials, card_reviews, cards, decks, tree_edit_sessions, "
				+ "prerequisites, tree_nodes, tree_tags, trees, node_tags, node_resources, nodes, users cascade");
	}

	@Test
	void example1NothingCountsSoTheHandEnteredValueIsUsed() {
		long selectors = createNode("CSS Selectors", 40);

		assertThat(readinessOfNode(selectors)).isEqualTo(40);
	}

	@Test
	void examples2And3ADeckAndAnArticleAreAveragedUntilTheArticleIsOnlyAReference() {
		long deck = deckWithCards("Flexbox cards", 20, 15);
		long article = materialAt("Flexbox article", 60);
		long flexbox = createNode("CSS Flexbox", 0);

		// Example 2: (75 + 60) / 2 = 67.5, rounded to 68
		MvcTestResult both = setResources(flexbox, "CSS Flexbox", """
				{"type": "deck", "deckId": %d, "counts": true},
				{"type": "material", "materialId": %d, "counts": true}""".formatted(deck, article));
		assertThat(both).hasStatusOk();
		assertThat(both).bodyJson().extractingPath("$.readiness").isEqualTo(68);
		assertThat(both).bodyJson().extractingPath("$.resources[0].deck.title").isEqualTo("Flexbox cards");
		assertThat(both).bodyJson().extractingPath("$.resources[0].readiness").isEqualTo(75);
		assertThat(both).bodyJson().extractingPath("$.resources[1].readiness").isEqualTo(60);
		assertThat(both).bodyJson().extractingPath("$.resources[1].material.title").isEqualTo("Flexbox article");

		// Example 3: the article marked as reference only leaves the deck
		MvcTestResult deckOnly = setResources(flexbox, "CSS Flexbox", """
				{"type": "deck", "deckId": %d, "counts": true},
				{"type": "material", "materialId": %d, "counts": false}""".formatted(deck, article));
		assertThat(deckOnly).bodyJson().extractingPath("$.readiness").isEqualTo(75);
		assertThat(deckOnly).bodyJson().extractingPath("$.resources[1].readiness").isEqualTo(60);
	}

	@Test
	void example4ALinkedTreeAndALessonAreAveraged() {
		long css = id(post("/api/v1/trees", "{\"title\": \"CSS\"}"));
		placeNode(css, createNode("Selectors", 50));
		placeNode(css, createNode("Flexbox", 58));
		long lesson = lessonAt("Front-end lesson", 100);
		long basics = createNode("Front-end Basics", 0);

		// (54 + 100) / 2 = 77
		MvcTestResult saved = setResources(basics, "Front-end Basics", """
				{"type": "tree", "treeId": %d, "counts": true},
				{"type": "lesson", "lessonId": %d, "counts": true}""".formatted(css, lesson));
		assertThat(saved).bodyJson().extractingPath("$.readiness").isEqualTo(77);
		assertThat(saved).bodyJson().extractingPath("$.resources[1].lesson.title").isEqualTo("Front-end lesson");

		// Opening the lesson is when it was last reviewed
		assertThat(saved).bodyJson().extractingPath("$.lastReviewedAt").isNull();
		assertThat(post("/api/v1/lessons/" + lesson + "/opens", "")).hasStatusOk();
		assertThat(mvc.get().uri("/api/v1/nodes/{id}", basics)).bodyJson()
			.extractingPath("$.lastReviewedAt")
			.isNotNull();

		// A lesson that a node lists can't be deleted
		assertThat(mvc.delete().uri("/api/v1/lessons/{id}", lesson)).hasStatus(HttpStatus.CONFLICT);
	}

	@Test
	void reviewingACardChangesTheDeckNodeTreeAndEveryTreeAbove() {
		long deck = deckWithCards("Flexbox cards", 2, 0);
		long card = firstCard(deck);
		long flexbox = createNode("CSS Flexbox", 0);
		setResources(flexbox, "CSS Flexbox", "{\"type\": \"deck\", \"deckId\": %d, \"counts\": true}".formatted(deck));
		long css = id(post("/api/v1/trees", "{\"title\": \"CSS\"}"));
		placeNode(css, flexbox);
		placeNode(css, createNode("Selectors", 50));
		long frontEnd = createNode("Front end", 0);
		setResources(frontEnd, "Front end", "{\"type\": \"tree\", \"treeId\": %d, \"counts\": true}".formatted(css));
		long web = id(post("/api/v1/trees", "{\"title\": \"Web\"}"));
		placeNode(web, frontEnd);

		// Nothing answered: deck 0%, CSS (0 + 50) / 2 = 25%, and Front end and Web follow it
		assertThat(readinessOfNode(flexbox)).isZero();
		assertThat(treeField(css, "readiness")).isEqualTo(25);
		assertThat(treeField(web, "readiness")).isEqualTo(25);
		assertThat(treeField(web, "lastReviewedAt")).isNull();

		for (int i = 0; i < 3; i++) {
			assertThat(post("/api/v1/decks/" + deck + "/cards/" + card + "/reviews", "{\"correct\": true}"))
				.hasStatusOk();
		}

		// One of two cards passed: deck 50%, CSS (50 + 50) / 2 = 50%, Front end 50%, Web 50%
		assertThat(mvc.get().uri("/api/v1/decks/{id}", deck)).bodyJson().extractingPath("$.readiness").isEqualTo(50);
		MvcTestResult node = mvc.get().uri("/api/v1/nodes/{id}", flexbox).exchange();
		assertThat(node).bodyJson().extractingPath("$.readiness").isEqualTo(50);
		assertThat(node).bodyJson().extractingPath("$.lastReviewedAt").isNotNull();
		assertThat(treeField(css, "readiness")).isEqualTo(50);
		assertThat(readinessOfNode(frontEnd)).isEqualTo(50);
		assertThat(treeField(web, "readiness")).isEqualTo(50);
		assertThat(treeField(web, "lastReviewedAt")).isNotNull();
		MvcTestResult list = mvc.get().uri("/api/v1/trees").exchange();
		assertThat(list).bodyJson().extractingPath("$[?(@.title == 'Web')].readiness").asArray().containsExactly(50);
		MvcTestResult inCss = mvc.get().uri("/api/v1/trees/{id}/nodes", css).exchange();
		assertThat(inCss).bodyJson().extractingPath("$[0].readiness").isEqualTo(50);
		assertThat(inCss).bodyJson().extractingPath("$[0].lastReviewedAt").isNotNull();
		assertThat(inCss).bodyJson().extractingPath("$[0].resources[0].deck.title").isEqualTo("Flexbox cards");
	}

	@Test
	void updatingProgressOnAMaterialChangesTheNodeAndItsTree() {
		long guide = materialAt("Flexbox guide", 40);
		long flexbox = createNode("CSS Flexbox", 0);
		setResources(flexbox, "CSS Flexbox",
				"{\"type\": \"material\", \"materialId\": %d, \"counts\": true}".formatted(guide));
		long css = id(post("/api/v1/trees", "{\"title\": \"CSS\"}"));
		placeNode(css, flexbox);
		placeNode(css, createNode("Selectors", 80));
		assertThat(treeField(css, "readiness")).isEqualTo(60); // (40 + 80) / 2

		assertThat(post("/api/v1/materials/" + guide + "/progress", "{\"progress\": 100, \"note\": \"Finished\"}"))
			.hasStatusOk();

		assertThat(readinessOfNode(flexbox)).isEqualTo(100);
		assertThat(treeField(css, "readiness")).isEqualTo(90); // (100 + 80) / 2
		assertThat(treeField(css, "lastReviewedAt")).isNotNull();

		// A material that a node lists can't be deleted
		MvcTestResult refused = mvc.delete().uri("/api/v1/materials/{id}", guide).exchange();
		assertThat(refused).hasStatus(HttpStatus.CONFLICT);
		assertThat(refused).bodyJson().extractingPath("$.nodes[0].title").isEqualTo("CSS Flexbox");
	}

	@Test
	void markingACodingQuestionSolvedChangesTheNodeAndItsTree() {
		long set = id(post("/api/v1/question-sets", "{\"title\": \"Flexbox exercises\"}"));
		long question = 0;
		for (String title : new String[] { "Centre a box", "Spread a nav bar" }) {
			question = id(post("/api/v1/question-sets/" + set + "/questions", """
					{"title": "%s", "language": "css", "problem": "P", "solution": "S"}""".formatted(title)));
		}
		long flexbox = createNode("CSS Flexbox", 0);
		setResources(flexbox, "CSS Flexbox",
				"{\"type\": \"question_set\", \"questionSetId\": %d, \"counts\": true}".formatted(set));
		long css = id(post("/api/v1/trees", "{\"title\": \"CSS\"}"));
		placeNode(css, flexbox);
		placeNode(css, createNode("Selectors", 100));
		assertThat(treeField(css, "readiness")).isEqualTo(50); // (0 + 100) / 2

		assertThat(post("/api/v1/question-sets/" + set + "/questions/" + question + "/solved", "")).hasStatusOk();

		// 1 of 2 questions solved: 50%, and CSS is (50 + 100) / 2 = 75%
		assertThat(readinessOfNode(flexbox)).isEqualTo(50);
		assertThat(treeField(css, "readiness")).isEqualTo(75);
		assertThat(mvc.delete().uri("/api/v1/question-sets/{id}", set)).hasStatus(HttpStatus.CONFLICT);
	}

	@Test
	void aDeckThatNodesListCannotBeDeletedAndOnlyTheUsersDecksCanBeListed() {
		long deck = deckWithCards("Flexbox cards", 1, 0);
		long flexbox = createNode("CSS Flexbox", 0);
		setResources(flexbox, "CSS Flexbox", "{\"type\": \"deck\", \"deckId\": %d, \"counts\": false}".formatted(deck));

		MvcTestResult refused = mvc.delete().uri("/api/v1/decks/{id}", deck).exchange();
		assertThat(refused).hasStatus(HttpStatus.CONFLICT);
		assertThat(refused).bodyJson().extractingPath("$.nodes[0].title").isEqualTo("CSS Flexbox");

		assertThat(setResources(flexbox, "CSS Flexbox", "{\"type\": \"deck\", \"deckId\": 999999}"))
			.hasStatus(HttpStatus.NOT_FOUND);

		setResources(flexbox, "CSS Flexbox", "");
		assertThat(mvc.delete().uri("/api/v1/decks/{id}", deck)).hasStatus(HttpStatus.NO_CONTENT);
	}

	/** A deck with {@code cards} cards, the first {@code passed} of them answered right 3 times. */
	private long deckWithCards(String title, int cards, int passed) {
		long deck = id(post("/api/v1/decks", "{\"title\": \"%s\"}".formatted(title)));
		for (int i = 0; i < cards; i++) {
			long card = id(post("/api/v1/decks/" + deck + "/cards", "{\"front\": \"Q%d\", \"back\": \"A\"}".formatted(i)));
			for (int answer = 0; i < passed && answer < 3; answer++) {
				post("/api/v1/decks/" + deck + "/cards/" + card + "/reviews", "{\"correct\": true}");
			}
		}
		return deck;
	}

	/** A lesson with one section and {@code progress} entered, never opened. */
	private long lessonAt(String title, int progress) {
		long lesson = id(post("/api/v1/lessons", """
				{"title": "%s", "sections": [{"title": "Intro", "body": "Some *Markdown*."}]}""".formatted(title)));
		assertThat(post("/api/v1/lessons/" + lesson + "/progress", "{\"progress\": %d}".formatted(progress)))
			.hasStatusOk();
		return lesson;
	}

	/** A material whose latest reported progress is {@code progress}, after an earlier, lower one. */
	private long materialAt(String title, int progress) {
		long material = id(post("/api/v1/materials", """
				{"title": "%s", "url": "https://example.com/article"}""".formatted(title)));
		assertThat(post("/api/v1/materials/" + material + "/progress", "{\"progress\": 10}")).hasStatusOk();
		assertThat(post("/api/v1/materials/" + material + "/progress", "{\"progress\": %d}".formatted(progress)))
			.hasStatusOk();
		return material;
	}

	private long firstCard(long deck) {
		return ((Number) JsonPath.read(body(mvc.get().uri("/api/v1/decks/{id}/cards", deck).exchange()), "$[0].id"))
			.longValue();
	}

	private MvcTestResult setResources(long node, String title, String resources) {
		return mvc.put()
			.uri("/api/v1/nodes/{id}", node)
			.contentType(MediaType.APPLICATION_JSON)
			.content("{\"title\": \"%s\", \"readiness\": 0, \"resources\": [%s]}".formatted(title, resources))
			.exchange();
	}

	private int readinessOfNode(long node) {
		return JsonPath.read(body(mvc.get().uri("/api/v1/nodes/{id}", node).exchange()), "$.readiness");
	}

	private Object treeField(long tree, String field) {
		return JsonPath.read(body(mvc.get().uri("/api/v1/trees/{id}", tree).exchange()), "$." + field);
	}

	private long createNode(String title, int readiness) {
		return id(post("/api/v1/nodes", "{\"title\": \"%s\", \"readiness\": %d}".formatted(title, readiness)));
	}

	private void placeNode(long tree, long node) {
		id(post("/api/v1/trees/" + tree + "/nodes", "{\"nodeId\": %d, \"positionX\": 0, \"positionY\": 0}".formatted(node)));
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
