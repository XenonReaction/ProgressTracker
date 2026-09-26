package com.progressiontracker.coding;

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
 * The Coding practice module's REST API through the full stack against real Postgres,
 * committing like production and emptying the tables after each test (see ApiIntegrationTest).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@ExtendWith(OutputCaptureExtension.class)
class CodingApiIntegrationTest {

	@Autowired
	private MockMvcTester mvc;

	@Autowired
	private JdbcTemplate jdbc;

	@AfterEach
	void emptyTables() {
		jdbc.execute("truncate table question_attempts, coding_questions, question_sets, users cascade");
	}

	@Test
	void theSolutionStaysHiddenUntilRevealedAndRevealingIsRecorded() {
		long set = id(post("/api/v1/question-sets", "{\"title\": \"Flexbox exercises\"}"));
		long question = createQuestion(set, "Centre a box");

		MvcTestResult hidden = mvc.get().uri("/api/v1/question-sets/{s}/questions/{q}", set, question).exchange();
		assertThat(hidden).bodyJson().extractingPath("$.solution").isNull();
		assertThat(hidden).bodyJson().extractingPath("$.problem").isEqualTo("Centre `.box`.");
		assertThat(hidden).bodyJson().extractingPath("$.language").isEqualTo("css");
		assertThat(mvc.get().uri("/api/v1/question-sets/{s}/questions", set)).bodyJson()
			.extractingPath("$[0].solution")
			.isNull();

		// The edit form can read it without it counting as revealed
		MvcTestResult forEditing = mvc.get()
			.uri("/api/v1/question-sets/{s}/questions/{q}?includeSolution=true", set, question)
			.exchange();
		assertThat(forEditing).bodyJson().extractingPath("$.solution").isEqualTo(".frame { display: flex; }");
		assertThat(forEditing).bodyJson().extractingPath("$.solutionRevealed").isEqualTo(false);

		MvcTestResult revealed = post("/api/v1/question-sets/" + set + "/questions/" + question + "/reveal", "");
		assertThat(revealed).hasStatusOk();
		assertThat(revealed).bodyJson().extractingPath("$.solution").isEqualTo(".frame { display: flex; }");
		assertThat(revealed).bodyJson().extractingPath("$.solutionRevealed").isEqualTo(true);
		assertThat(revealed).bodyJson().extractingPath("$.solved").isEqualTo(false);
		// Once revealed, it stays shown
		assertThat(mvc.get().uri("/api/v1/question-sets/{s}/questions/{q}", set, question)).bodyJson()
			.extractingPath("$.solution")
			.isNotNull();
	}

	@Test
	void markingQuestionsSolvedRaisesTheSetsReadinessAndRecordsWhetherTheSolutionWasSeenFirst() {
		long set = id(post("/api/v1/question-sets", "{\"title\": \"Flexbox exercises\"}"));
		long first = createQuestion(set, "Centre a box");
		long second = createQuestion(set, "Spread a nav bar");

		assertThat(mvc.get().uri("/api/v1/question-sets/{id}", set)).bodyJson()
			.extractingPath("$.readiness")
			.isEqualTo(0);

		MvcTestResult solved = post("/api/v1/question-sets/" + set + "/questions/" + first + "/solved", "");
		assertThat(solved).bodyJson().extractingPath("$.solved").isEqualTo(true);
		assertThat(solved).bodyJson().extractingPath("$.revealedBeforeSolved").isEqualTo(false);

		post("/api/v1/question-sets/" + set + "/questions/" + second + "/reveal", "");
		MvcTestResult afterReveal = post("/api/v1/question-sets/" + set + "/questions/" + second + "/solved", "");
		assertThat(afterReveal).bodyJson().extractingPath("$.revealedBeforeSolved").isEqualTo(true);

		MvcTestResult setNow = mvc.get().uri("/api/v1/question-sets/{id}", set).exchange();
		assertThat(setNow).bodyJson().extractingPath("$.solvedCount").isEqualTo(2);
		assertThat(setNow).bodyJson().extractingPath("$.readiness").isEqualTo(100);
		assertThat(setNow).bodyJson().extractingPath("$.lastReviewedAt").isNotNull();
	}

	@Test
	void editsAndDeletesQuestionsAndSets(CapturedOutput output) {
		long set = id(post("/api/v1/question-sets", "{\"title\": \"Secret set\"}"));
		long question = createQuestion(set, "Centre a box");
		post("/api/v1/question-sets/" + set + "/questions/" + question + "/solved", "");

		MvcTestResult edited = put("/api/v1/question-sets/" + set + "/questions/" + question, """
				{"title": "Centre it", "language": "html", "problem": "New problem", "solution": "<div></div>"}""");
		assertThat(edited).hasStatusOk();
		assertThat(edited).bodyJson().extractingPath("$.language").isEqualTo("html");
		assertThat(edited).bodyJson().extractingPath("$.examples").isNull();
		// Editing keeps the attempts
		assertThat(edited).bodyJson().extractingPath("$.solved").isEqualTo(true);

		assertThat(mvc.delete().uri("/api/v1/question-sets/{s}/questions/{q}", set, question))
			.hasStatus(HttpStatus.NO_CONTENT);
		createQuestion(set, "Another");
		assertThat(mvc.delete().uri("/api/v1/question-sets/{id}", set)).hasStatus(HttpStatus.NO_CONTENT);
		assertThat(jdbc.queryForObject("select count(*) from coding_questions", Integer.class)).isZero();
		assertThat(output).contains("Deleted coding question " + question + " from set " + set)
			.contains("Deleted question set " + set)
			.doesNotContain("Secret set");
	}

	@Test
	void refusesInvalidQuestionsAndUnknownIds() {
		long set = id(post("/api/v1/question-sets", "{\"title\": \"Set\"}"));

		MvcTestResult invalid = post("/api/v1/question-sets/" + set + "/questions", """
				{"title": "", "language": "css", "problem": "", "solution": ""}""");
		assertThat(invalid).hasStatus(HttpStatus.BAD_REQUEST);
		assertThat(invalid).bodyJson().extractingPath("$.errors[*].field").asArray()
			.containsExactlyInAnyOrder("title", "problem", "solution");
		// JavaScript comes later
		assertThat(post("/api/v1/question-sets/" + set + "/questions", """
				{"title": "Loop", "language": "javascript", "problem": "P", "solution": "S"}"""))
			.hasStatus(HttpStatus.BAD_REQUEST);
		assertThat(post("/api/v1/question-sets/999999/questions/1/solved", "")).hasStatus(HttpStatus.NOT_FOUND);
	}

	private long createQuestion(long set, String title) {
		return id(post("/api/v1/question-sets/" + set + "/questions", """
				{"title": "%s", "language": "css", "problem": "Centre `.box`.",
				 "examples": "`<div class=\\"frame\\">`", "solution": ".frame { display: flex; }"}"""
			.formatted(title)));
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
