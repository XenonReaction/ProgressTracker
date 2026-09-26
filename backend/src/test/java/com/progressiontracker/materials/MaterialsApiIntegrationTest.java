package com.progressiontracker.materials;

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
 * The Materials module's REST API through the full stack against real Postgres, committing
 * like production and emptying the tables after each test (see ApiIntegrationTest).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@ExtendWith(OutputCaptureExtension.class)
class MaterialsApiIntegrationTest {

	@Autowired
	private MockMvcTester mvc;

	@Autowired
	private JdbcTemplate jdbc;

	@AfterEach
	void emptyTables() {
		jdbc.execute("truncate table material_progress_updates, materials, users cascade");
	}

	@Test
	void keepsEveryProgressUpdateAndTheLatestIsTheMaterialsProgress() {
		long guide = id(post("/api/v1/materials", """
				{"title": "Flexbox guide", "url": "https://css-tricks.com/flexbox/", "notes": "Container first"}"""));

		MvcTestResult fresh = mvc.get().uri("/api/v1/materials/{id}", guide).exchange();
		assertThat(fresh).bodyJson().extractingPath("$.progress").isEqualTo(0);
		assertThat(fresh).bodyJson().extractingPath("$.lastReviewedAt").isNull();
		assertThat(fresh).bodyJson().extractingPath("$.notes").isEqualTo("Container first");

		assertThat(progress(guide, "{\"progress\": 40, \"note\": \"Container properties\"}")).hasStatusOk();
		MvcTestResult second = progress(guide, "{\"progress\": 60}");
		assertThat(second).hasStatusOk();
		assertThat(second).bodyJson().extractingPath("$.progress").isEqualTo(60);
		assertThat(second).bodyJson().extractingPath("$.updateCount").isEqualTo(2);
		assertThat(second).bodyJson().extractingPath("$.lastReviewedAt").isNotNull();

		// Newest first, both kept; progress can go down again, and the latest still wins
		MvcTestResult history = mvc.get().uri("/api/v1/materials/{id}/progress", guide).exchange();
		assertThat(history).bodyJson().extractingPath("$[*].progress").asArray().containsExactly(60, 40);
		assertThat(history).bodyJson().extractingPath("$[1].note").isEqualTo("Container properties");
		assertThat(history).bodyJson().extractingPath("$[0].note").isNull();
		progress(guide, "{\"progress\": 30, \"note\": \"Started again\"}");
		assertThat(mvc.get().uri("/api/v1/materials")).bodyJson().extractingPath("$[0].progress").isEqualTo(30);
	}

	@Test
	void editingTheDetailsIsNotAReview() {
		long guide = id(post("/api/v1/materials", "{\"title\": \"Guide\", \"url\": \"https://example.com\"}"));
		String reviewed = JsonPath.read(body(progress(guide, "{\"progress\": 50}")), "$.lastReviewedAt");

		MvcTestResult edited = put("/api/v1/materials/" + guide, """
				{"title": "Renamed guide", "url": "https://example.com/v2"}""");

		assertThat(edited).hasStatusOk();
		assertThat(edited).bodyJson().extractingPath("$.title").isEqualTo("Renamed guide");
		assertThat(edited).bodyJson().extractingPath("$.url").isEqualTo("https://example.com/v2");
		assertThat(edited).bodyJson().extractingPath("$.progress").isEqualTo(50);
		assertThat(edited).bodyJson().extractingPath("$.lastReviewedAt").isEqualTo(reviewed);
		assertThat(edited).bodyJson().extractingPath("$.updateCount").isEqualTo(1);
	}

	@Test
	void refusesInvalidRequestsAndUnknownMaterials() {
		MvcTestResult invalid = post("/api/v1/materials", "{\"title\": \"\", \"url\": \"ftp://example.com\"}");
		assertThat(invalid).hasStatus(HttpStatus.BAD_REQUEST);
		assertThat(invalid).bodyJson().extractingPath("$.errors[*].field").asArray()
			.containsExactlyInAnyOrder("title", "url");

		long guide = id(post("/api/v1/materials", "{\"title\": \"Guide\", \"url\": \"https://example.com\"}"));
		assertThat(progress(guide, "{\"progress\": 101}")).hasStatus(HttpStatus.BAD_REQUEST);
		assertThat(progress(guide, "{}")).hasStatus(HttpStatus.BAD_REQUEST);
		assertThat(progress(999999, "{\"progress\": 10}")).hasStatus(HttpStatus.NOT_FOUND);
		assertThat(mvc.get().uri("/api/v1/materials/999999/progress")).hasStatus(HttpStatus.NOT_FOUND);
	}

	@Test
	void deletesAMaterialWithItsHistoryAndLogsItById(CapturedOutput output) {
		long guide = id(post("/api/v1/materials", "{\"title\": \"Secret guide\", \"url\": \"https://example.com\"}"));
		progress(guide, "{\"progress\": 10}");

		assertThat(mvc.delete().uri("/api/v1/materials/{id}", guide)).hasStatus(HttpStatus.NO_CONTENT);

		assertThat(mvc.get().uri("/api/v1/materials/{id}", guide)).hasStatus(HttpStatus.NOT_FOUND);
		assertThat(jdbc.queryForObject("select count(*) from material_progress_updates", Integer.class)).isZero();
		assertThat(output).contains("Deleted material " + guide).doesNotContain("Secret guide");
	}

	private MvcTestResult progress(long material, String json) {
		return post("/api/v1/materials/" + material + "/progress", json);
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
