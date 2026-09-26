package com.progressiontracker;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
 * Phase 7.7: spaced repetition and "review due", through the whole HTTP stack, moving a
 * {@link TestClock} forward to let reviews come due.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import({ TestcontainersConfiguration.class, TestClock.Config.class })
class ReviewDueIntegrationTest {

	@Autowired
	private MockMvcTester mvc;

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	private TestClock clock;

	@BeforeEach
	void resetClock() {
		clock.reset();
	}

	@AfterEach
	void emptyTables() {
		jdbc.execute("truncate table card_reviews, cards, decks, tree_edit_sessions, prerequisites, tree_nodes, "
				+ "tree_tags, trees, node_tags, node_resources, nodes, users cascade");
	}

	@Test
	void aPassedCardComesBackWhenDueAndItsDeckNodeAndTreesShowReviewDueUntilItIsReviewed() {
		long deck = id(post("/api/v1/decks", "{\"title\": \"Flexbox cards\"}"));
		long card = createCard(deck);
		long flexbox = id(post("/api/v1/nodes", """
				{"title": "CSS Flexbox", "readiness": 0,
				 "resources": [{"type": "deck", "deckId": %d, "counts": true}]}""".formatted(deck)));
		long css = id(post("/api/v1/trees", "{\"title\": \"CSS\"}"));
		place(css, flexbox);
		long frontEnd = id(post("/api/v1/nodes", """
				{"title": "Front end", "readiness": 0,
				 "resources": [{"type": "tree", "treeId": %d, "counts": true}]}""".formatted(css)));
		long web = id(post("/api/v1/trees", "{\"title\": \"Web\"}"));
		place(web, frontEnd);

		answer(deck, card, 3);

		// Passed: off the review page, due in 7 days, nothing due yet
		MvcTestResult passed = mvc.get().uri("/api/v1/decks/{d}/cards", deck).exchange();
		assertThat(passed).bodyJson().extractingPath("$[0].passed").isEqualTo(true);
		assertThat(passed).bodyJson().extractingPath("$[0].due").isEqualTo(false);
		assertThat(Instant.parse(JsonPath.read(body(passed), "$[0].dueAt")))
			.isEqualTo(clock.instant().truncatedTo(ChronoUnit.MICROS).plus(Duration.ofDays(7)));
		assertThat(mvc.get().uri("/api/v1/review-queue")).bodyJson().extractingPath("$").asArray().isEmpty();
		assertThat(field("/api/v1/trees/" + web, "$.reviewDue")).isEqualTo(false);

		clock.advance(Duration.ofDays(7).plusMinutes(1));

		// Due: back on the review page, and every level above says so; readiness is unchanged
		assertThat(mvc.get().uri("/api/v1/review-queue")).bodyJson().extractingPath("$[*].id").asArray()
			.containsExactly((int) card);
		assertThat(field("/api/v1/decks/" + deck, "$.dueCount")).isEqualTo(1);
		assertThat(field("/api/v1/decks/" + deck, "$.readiness")).isEqualTo(100);
		assertThat(field("/api/v1/nodes/" + flexbox, "$.reviewDue")).isEqualTo(true);
		assertThat(field("/api/v1/nodes/" + flexbox, "$.resources[0].reviewDue")).isEqualTo(true);
		assertThat(field("/api/v1/nodes/" + flexbox, "$.readiness")).isEqualTo(100);
		assertThat(field("/api/v1/trees/" + css, "$.reviewDue")).isEqualTo(true);
		assertThat(field("/api/v1/trees/" + css + "/nodes", "$[0].reviewDue")).isEqualTo(true);
		assertThat(field("/api/v1/nodes/" + frontEnd, "$.reviewDue")).isEqualTo(true);
		assertThat(field("/api/v1/trees/" + web, "$.reviewDue")).isEqualTo(true);
		assertThat(field("/api/v1/trees", "$[?(@.title == 'Web')].reviewDue")).asList().containsExactly(true);

		// Reviewing it clears everything, and the next review is 14 days away
		answer(deck, card, 1);
		assertThat(mvc.get().uri("/api/v1/review-queue")).bodyJson().extractingPath("$").asArray().isEmpty();
		assertThat(field("/api/v1/trees/" + web, "$.reviewDue")).isEqualTo(false);
		clock.advance(Duration.ofDays(13));
		assertThat(field("/api/v1/decks/" + deck, "$.dueCount")).isEqualTo(0);
		clock.advance(Duration.ofDays(1).plusMinutes(1));
		assertThat(field("/api/v1/decks/" + deck, "$.dueCount")).isEqualTo(1);
	}

	@Test
	void aWrongAnswerPutsAPassedCardStraightBackWithNothingDue() {
		long deck = id(post("/api/v1/decks", "{\"title\": \"Flexbox cards\"}"));
		long card = createCard(deck);
		answer(deck, card, 3);
		clock.advance(Duration.ofDays(8));

		post("/api/v1/decks/" + deck + "/cards/" + card + "/reviews", "{\"correct\": false}");

		// Not passed any more: on the review page as a card to learn again, not a due review
		MvcTestResult queue = mvc.get().uri("/api/v1/review-queue").exchange();
		assertThat(queue).bodyJson().extractingPath("$[0].passed").isEqualTo(false);
		assertThat(queue).bodyJson().extractingPath("$[0].due").isEqualTo(false);
		assertThat(field("/api/v1/decks/" + deck, "$.dueCount")).isEqualTo(0);
		assertThat(field("/api/v1/decks/" + deck, "$.readiness")).isEqualTo(0);
	}

	private long createCard(long deck) {
		return id(post("/api/v1/decks/" + deck + "/cards", "{\"front\": \"Main axis?\", \"back\": \"flex-direction\"}"));
	}

	private void answer(long deck, long card, int correctTimes) {
		for (int i = 0; i < correctTimes; i++) {
			assertThat(post("/api/v1/decks/" + deck + "/cards/" + card + "/reviews", "{\"correct\": true}"))
				.hasStatusOk();
		}
	}

	private void place(long tree, long node) {
		id(post("/api/v1/trees/" + tree + "/nodes",
				"{\"nodeId\": %d, \"positionX\": 0, \"positionY\": 0}".formatted(node)));
	}

	private Object field(String uri, String path) {
		return JsonPath.read(body(mvc.get().uri(uri).exchange()), path);
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
