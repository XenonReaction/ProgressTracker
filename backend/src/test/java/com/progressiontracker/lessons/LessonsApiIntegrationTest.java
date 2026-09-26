package com.progressiontracker.lessons;

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
 * The Lessons module's REST API through the full stack against real Postgres, committing like
 * production and emptying the tables after each test (see ApiIntegrationTest).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@ExtendWith(OutputCaptureExtension.class)
class LessonsApiIntegrationTest {

	@Autowired
	private MockMvcTester mvc;

	@Autowired
	private JdbcTemplate jdbc;

	@AfterEach
	void emptyTables() {
		jdbc.execute("truncate table lesson_progress_updates, lesson_opens, lesson_sections, lessons, users cascade");
	}

	@Test
	void writesALessonWithSectionsInOrderAndReplacesThemOnEdit() {
		long lesson = id(post("/api/v1/lessons", """
				{"title": "Flexbox", "summary": "The basics",
				 "sections": [{"title": "Container", "body": "Use `display: flex`."},
				              {"title": "Axes", "body": "- row\\n- column"}]}"""));

		MvcTestResult read = mvc.get().uri("/api/v1/lessons/{id}", lesson).exchange();
		assertThat(read).bodyJson().extractingPath("$.sections[*].title").asArray().containsExactly("Container", "Axes");
		assertThat(read).bodyJson().extractingPath("$.sections[1].body").isEqualTo("- row\n- column");
		assertThat(read).bodyJson().extractingPath("$.summary").isEqualTo("The basics");
		assertThat(read).bodyJson().extractingPath("$.progress").isEqualTo(0);

		MvcTestResult edited = put("/api/v1/lessons/" + lesson, """
				{"title": "Flexbox basics", "sections": [{"title": "Axes", "body": "Main and cross."}]}""");
		assertThat(edited).hasStatusOk();
		assertThat(edited).bodyJson().extractingPath("$.title").isEqualTo("Flexbox basics");
		assertThat(edited).bodyJson().extractingPath("$.sections[*].title").asArray().containsExactly("Axes");
		assertThat(edited).bodyJson().extractingPath("$.summary").isNull();
	}

	@Test
	void openingALessonIsAReviewButReadingItThroughTheApiIsNot() {
		long lesson = id(post("/api/v1/lessons", "{\"title\": \"Flexbox\"}"));

		mvc.get().uri("/api/v1/lessons/{id}", lesson).exchange();
		assertThat(mvc.get().uri("/api/v1/lessons/{id}", lesson)).bodyJson()
			.extractingPath("$.lastReviewedAt")
			.isNull();

		MvcTestResult opened = post("/api/v1/lessons/" + lesson + "/opens", "");
		assertThat(opened).hasStatusOk();
		assertThat(opened).bodyJson().extractingPath("$.lastReviewedAt").isNotNull();
		assertThat(mvc.get().uri("/api/v1/lessons")).bodyJson().extractingPath("$[0].lastReviewedAt").isNotNull();
	}

	@Test
	void theLatestProgressEnteredIsTheLessonsProgress() {
		long lesson = id(post("/api/v1/lessons", "{\"title\": \"Flexbox\"}"));

		assertThat(post("/api/v1/lessons/" + lesson + "/progress", "{\"progress\": 40}")).hasStatusOk();
		MvcTestResult latest = post("/api/v1/lessons/" + lesson + "/progress", "{\"progress\": 25}");

		assertThat(latest).bodyJson().extractingPath("$.progress").isEqualTo(25);
		// Entering progress isn't opening the lesson
		assertThat(latest).bodyJson().extractingPath("$.lastReviewedAt").isNull();
		assertThat(jdbc.queryForObject("select count(*) from lesson_progress_updates", Integer.class)).isEqualTo(2);
	}

	@Test
	void refusesInvalidRequestsAndUnknownLessons() {
		MvcTestResult invalid = post("/api/v1/lessons", """
				{"title": "", "sections": [{"title": "", "body": ""}]}""");
		assertThat(invalid).hasStatus(HttpStatus.BAD_REQUEST);
		assertThat(invalid).bodyJson().extractingPath("$.errors[*].field").asArray()
			.containsExactlyInAnyOrder("title", "sections[0].title", "sections[0].body");

		long lesson = id(post("/api/v1/lessons", "{\"title\": \"Flexbox\"}"));
		assertThat(post("/api/v1/lessons/" + lesson + "/progress", "{\"progress\": -1}"))
			.hasStatus(HttpStatus.BAD_REQUEST);
		assertThat(post("/api/v1/lessons/999999/opens", "")).hasStatus(HttpStatus.NOT_FOUND);
	}

	@Test
	void deletesALessonWithItsSectionsAndActivity(CapturedOutput output) {
		long lesson = id(post("/api/v1/lessons", """
				{"title": "Secret lesson", "sections": [{"title": "One", "body": "Text"}]}"""));
		post("/api/v1/lessons/" + lesson + "/opens", "");
		post("/api/v1/lessons/" + lesson + "/progress", "{\"progress\": 10}");

		assertThat(mvc.delete().uri("/api/v1/lessons/{id}", lesson)).hasStatus(HttpStatus.NO_CONTENT);

		assertThat(mvc.get().uri("/api/v1/lessons/{id}", lesson)).hasStatus(HttpStatus.NOT_FOUND);
		for (String table : new String[] { "lesson_sections", "lesson_opens", "lesson_progress_updates" }) {
			assertThat(jdbc.queryForObject("select count(*) from " + table, Integer.class)).as(table).isZero();
		}
		assertThat(output).contains("Deleted lesson " + lesson).doesNotContain("Secret lesson");
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
