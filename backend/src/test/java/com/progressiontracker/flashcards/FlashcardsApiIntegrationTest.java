package com.progressiontracker.flashcards;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;

import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import com.progressiontracker.TestcontainersConfiguration;

/**
 * The Flashcards module's REST API through the full stack against real Postgres, committing
 * like production and emptying the tables after each test (see ApiIntegrationTest).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@ExtendWith(OutputCaptureExtension.class)
class FlashcardsApiIntegrationTest {

	@Autowired
	private MockMvcTester mvc;

	@Autowired
	private JdbcTemplate jdbc;

	@AfterEach
	void emptyTables() {
		jdbc.execute("truncate table card_reviews, cards, decks, users cascade");
	}

	@Test
	void studyingADeckPassesCardsAndRaisesItsReadiness() {
		long deck = id(post("/api/v1/decks", """
				{"title": "CSS Flexbox", "description": "One-dimensional layout"}"""));
		long display = createCard(deck, "Flex container?", "display: flex");
		long direction = createCard(deck, "Main axis?", "flex-direction");

		MvcTestResult fresh = mvc.get().uri("/api/v1/decks/{id}", deck).exchange();
		assertThat(fresh).bodyJson().extractingPath("$.cardCount").isEqualTo(2);
		assertThat(fresh).bodyJson().extractingPath("$.readiness").isEqualTo(0);
		assertThat(fresh).bodyJson().extractingPath("$.lastReviewedAt").isNull();

		// Two right answers aren't enough; the third passes the card
		review(deck, display, true);
		MvcTestResult second = review(deck, display, true);
		assertThat(second).bodyJson().extractingPath("$.correctInARow").isEqualTo(2);
		assertThat(second).bodyJson().extractingPath("$.passed").isEqualTo(false);
		MvcTestResult third = review(deck, display, true);
		assertThat(third).hasStatusOk();
		assertThat(third).bodyJson().extractingPath("$.passed").isEqualTo(true);
		assertThat(third).bodyJson().extractingPath("$.lastReviewedAt").isNotNull();

		// 1 of 2 passed: 50%, not yet complete
		MvcTestResult half = mvc.get().uri("/api/v1/decks/{id}", deck).exchange();
		assertThat(half).bodyJson().extractingPath("$.passedCount").isEqualTo(1);
		assertThat(half).bodyJson().extractingPath("$.readiness").isEqualTo(50);
		assertThat(half).bodyJson().extractingPath("$.complete").isEqualTo(false);
		assertThat(half).bodyJson().extractingPath("$.lastReviewedAt").isNotNull();

		review(deck, direction, true);
		review(deck, direction, true);
		review(deck, direction, true);
		MvcTestResult all = mvc.get().uri("/api/v1/decks").exchange();
		assertThat(all).bodyJson().extractingPath("$[0].readiness").isEqualTo(100);
		assertThat(all).bodyJson().extractingPath("$[0].complete").isEqualTo(true);

		// One wrong answer un-passes a card
		review(deck, display, false);
		MvcTestResult cards = mvc.get().uri("/api/v1/decks/{id}/cards", deck).exchange();
		assertThat(cards).bodyJson().extractingPath("$[*].passed").asArray().containsExactly(false, true);
		assertThat(cards).bodyJson().extractingPath("$[0].deckTitle").isEqualTo("CSS Flexbox");
		assertThat(mvc.get().uri("/api/v1/decks/{id}", deck)).bodyJson().extractingPath("$.complete").isEqualTo(false);
	}

	@Test
	void theReviewQueueListsCardsNotYetPassedLeastRecentlyReviewedFirst() {
		long css = id(post("/api/v1/decks", "{\"title\": \"CSS\"}"));
		long html = id(post("/api/v1/decks", "{\"title\": \"HTML\"}"));
		long answeredFirst = createCard(css, "Answered first", "a");
		long passed = createCard(css, "Passed", "b");
		long answeredLater = createCard(html, "Answered later", "c");
		long neverSeen = createCard(html, "Never seen", "d");
		review(css, answeredFirst, false);
		review(css, passed, true);
		review(css, passed, true);
		review(css, passed, true);
		review(html, answeredLater, true);

		MvcTestResult queue = mvc.get().uri("/api/v1/review-queue").exchange();
		assertThat(queue).hasStatusOk();
		assertThat(queue).bodyJson().extractingPath("$[*].id").asArray()
			.containsExactly((int) neverSeen, (int) answeredFirst, (int) answeredLater);
		assertThat(queue).bodyJson().extractingPath("$[0].deckTitle").isEqualTo("HTML");

		// One deck's queue
		assertThat(mvc.get().uri("/api/v1/review-queue?deckId={id}", css)).bodyJson()
			.extractingPath("$[*].id")
			.asArray()
			.containsExactly((int) answeredFirst);
		assertThat(mvc.get().uri("/api/v1/review-queue?deckId=999999")).hasStatus(HttpStatus.NOT_FOUND);
	}

	@Test
	void editsAndDeletesCardsAndDecks(CapturedOutput output) {
		long deck = id(post("/api/v1/decks", "{\"title\": \"Secret deck\"}"));
		long card = createCard(deck, "Front", "Back");
		review(deck, card, true);

		MvcTestResult edited = put("/api/v1/decks/" + deck + "/cards/" + card, """
				{"front": "New front", "back": "New back"}""");
		assertThat(edited).hasStatusOk();
		assertThat(edited).bodyJson().extractingPath("$.front").isEqualTo("New front");
		// Editing a card keeps its answers
		assertThat(edited).bodyJson().extractingPath("$.correctInARow").isEqualTo(1);

		MvcTestResult renamed = put("/api/v1/decks/" + deck, "{\"title\": \"Renamed\", \"description\": \"Now described\"}");
		assertThat(renamed).hasStatusOk();
		assertThat(renamed).bodyJson().extractingPath("$.title").isEqualTo("Renamed");
		assertThat(renamed).bodyJson().extractingPath("$.cardCount").isEqualTo(1);

		assertThat(mvc.delete().uri("/api/v1/decks/{deck}/cards/{card}", deck, card)).hasStatus(HttpStatus.NO_CONTENT);
		assertThat(mvc.get().uri("/api/v1/decks/{id}/cards", deck)).bodyJson().extractingPath("$").asArray().isEmpty();

		createCard(deck, "Another", "card");
		assertThat(mvc.delete().uri("/api/v1/decks/{id}", deck)).hasStatus(HttpStatus.NO_CONTENT);
		assertThat(mvc.get().uri("/api/v1/decks/{id}", deck)).hasStatus(HttpStatus.NOT_FOUND);
		assertThat(jdbc.queryForObject("select count(*) from cards", Integer.class)).isZero();

		assertThat(output).contains("Deleted card " + card + " from deck " + deck)
			.contains("Deleted deck " + deck)
			.doesNotContain("Secret deck");
	}

	@Test
	void refusesInvalidRequestsAndCardsFromAnotherDeck() {
		long deck = id(post("/api/v1/decks", "{\"title\": \"CSS\"}"));
		long other = id(post("/api/v1/decks", "{\"title\": \"HTML\"}"));
		long card = createCard(other, "Front", "Back");

		MvcTestResult invalid = post("/api/v1/decks/" + deck + "/cards", "{\"front\": \"\", \"back\": \" \"}");
		assertThat(invalid).hasStatus(HttpStatus.BAD_REQUEST);
		assertThat(invalid).bodyJson().extractingPath("$.errors[*].field").asArray()
			.containsExactlyInAnyOrder("front", "back");
		assertThat(post("/api/v1/decks", "{\"title\": \"\"}")).hasStatus(HttpStatus.BAD_REQUEST);
		assertThat(post("/api/v1/decks/" + other + "/cards/" + card + "/reviews", "{}"))
			.hasStatus(HttpStatus.BAD_REQUEST);

		// A card is only found through its own deck
		assertThat(post("/api/v1/decks/" + deck + "/cards/" + card + "/reviews", "{\"correct\": true}"))
			.hasStatus(HttpStatus.NOT_FOUND);
		assertThat(mvc.get().uri("/api/v1/decks/999999/cards")).hasStatus(HttpStatus.NOT_FOUND);
	}

	private long createCard(long deck, String front, String back) {
		return id(post("/api/v1/decks/" + deck + "/cards", """
				{"front": "%s", "back": "%s"}""".formatted(front, back)));
	}

	private MvcTestResult review(long deck, long card, boolean correct) {
		MvcTestResult result = post("/api/v1/decks/" + deck + "/cards/" + card + "/reviews",
				"{\"correct\": " + correct + "}");
		assertThat(result).hasStatusOk();
		return result;
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
