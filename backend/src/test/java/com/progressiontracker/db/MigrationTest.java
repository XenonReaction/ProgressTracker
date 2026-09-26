package com.progressiontracker.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Runs the Flyway migrations the way the app does: on an empty database, and on one whose
 * tables Hibernate created before Flyway was added (now refused). Each test uses its own
 * schema in one shared container.
 */
@Testcontainers
class MigrationTest {

	@Container
	static final PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:18"));

	/** Every named foreign key and unique constraint after the latest migration. */
	private static final List<String> READABLE_NAMES = List.of(
			"card_reviews_card_id_fk", "card_reviews_user_id_fk", "cards_deck_id_fk",
			"coding_questions_set_id_fk", "decks_user_id_fk", "lesson_opens_lesson_id_fk",
			"lesson_opens_user_id_fk", "lesson_progress_updates_lesson_id_fk",
			"lesson_progress_updates_user_id_fk", "lesson_sections_lesson_id_fk", "lessons_user_id_fk",
			"material_progress_updates_material_id_fk", "material_progress_updates_user_id_fk",
			"materials_user_id_fk", "node_resources_node_id_fk", "node_resources_tree_id_fk",
			"node_tags_node_id_fk", "nodes_user_id_fk", "prerequisites_dependent_tree_node_id_fk",
			"prerequisites_edge_unique", "prerequisites_prerequisite_tree_node_id_fk",
			"question_attempts_question_id_fk", "question_attempts_user_id_fk", "question_sets_user_id_fk",
			"tree_edit_sessions_tree_id_fk", "tree_edit_sessions_tree_id_unique", "tree_nodes_node_id_fk",
			"tree_nodes_tree_id_fk", "tree_nodes_tree_node_unique", "tree_tags_tree_id_fk", "trees_user_id_fk",
			"users_username_unique");

	@Test
	void emptyDatabaseRunsEveryMigration() throws Exception {
		MigrateResult result = flyway("fresh").migrate();

		assertThat(result.migrations).extracting(m -> m.version).containsExactly("1", "2", "3", "4", "5", "6", "7", "8", "9",
				"10", "11");
		assertThat(result.targetSchemaVersion).isEqualTo("11");
		assertThat(foreignKeyAndUniqueNames("fresh")).containsExactlyElementsOf(READABLE_NAMES);
	}

	/**
	 * Phase 5.1 baselined the one database that predated Flyway, and 6.1 turned that off: a
	 * database with tables but no Flyway history is now refused, and left as it was.
	 */
	@Test
	void databaseWithTablesButNoFlywayHistoryIsRefused() throws Exception {
		// What ddl-auto=update used to leave behind: V1's tables and some data, but no history
		try (Connection connection = connect(); Statement statement = connection.createStatement()) {
			statement.execute("create schema legacy");
			statement.execute("set search_path to legacy");
			statement.execute(resource("db/migration/V1__initial_schema.sql"));
			statement.execute("insert into users (created_at, username) values (now(), 'demo')");
		}

		assertThatThrownBy(() -> flyway("legacy").migrate()).isInstanceOf(FlywayException.class)
			.hasMessageContaining("no schema history table");

		assertThat(foreignKeyAndUniqueNames("legacy")).doesNotContain("users_username_unique");
		try (Connection connection = connect(); Statement statement = connection.createStatement();
				ResultSet rows = statement.executeQuery("select username from legacy.users")) {
			assertThat(rows.next()).isTrue();
			assertThat(rows.getString(1)).isEqualTo("demo");
		}
	}

	/**
	 * Phase 7.2 moved links and linked trees into node_resources. A node's linked tree comes
	 * first and counts; its links follow in order and don't.
	 */
	@Test
	void nodeResourcesMigrationKeepsEveryLinkAndLinkedTree() throws Exception {
		flyway("resources", "6").migrate();
		try (Connection connection = connect(); Statement statement = connection.createStatement()) {
			statement.execute("set search_path to resources");
			statement.execute("insert into users (id, created_at, username) values (1, now(), 'demo')");
			statement.execute("insert into trees (id, user_id, title, created_at, updated_at) "
					+ "values (10, 1, 'Collections', now(), now())");
			statement.execute("insert into nodes (id, user_id, title, readiness, readiness_source_type, "
					+ "linked_tree_id, created_at, updated_at) values "
					+ "(20, 1, 'Linked with links', 30, 'linked_tree', 10, now(), now()), "
					+ "(21, 1, 'Links only', 40, 'manual', null, now(), now()), "
					+ "(22, 1, 'Nothing', 50, 'manual', null, now(), now())");
			statement.execute("insert into node_links (node_id, position, url, label) values "
					+ "(20, 0, 'https://a.example', 'A'), (20, 1, 'https://b.example', null), "
					+ "(21, 0, 'https://c.example', 'C')");
		}

		flyway("resources").migrate();

		List<String> rows = new ArrayList<>();
		try (Connection connection = connect(); Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery("""
						select node_id, position, resource_type, tree_id, url, label, counts
						from resources.node_resources order by node_id, position""")) {
			while (result.next()) {
				rows.add(String.join(" ", result.getString(1), result.getString(2), result.getString(3),
						String.valueOf(result.getObject(4)), String.valueOf(result.getString(5)),
						String.valueOf(result.getString(6)), result.getString(7)));
			}
		}
		assertThat(rows).containsExactly("20 0 tree 10 null null t", "20 1 url null https://a.example A f",
				"20 2 url null https://b.example null f", "21 0 url null https://c.example C f");
		try (Connection connection = connect(); Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery("""
						select column_name from information_schema.columns
						where table_schema = 'resources' and table_name = 'nodes'
						and column_name in ('linked_tree_id', 'readiness_source_type')""")) {
			assertThat(result.next()).isFalse();
		}
	}

	/** Flyway with the app's settings from application.properties, pointed at one schema. */
	private static Flyway flyway(String schema) throws IOException {
		return flyway(schema, "latest");
	}

	/** The same, stopping at {@code target} (a version, or "latest"). */
	private static Flyway flyway(String schema, String target) throws IOException {
		Properties app = new Properties();
		try (InputStream in = MigrationTest.class.getClassLoader().getResourceAsStream("application.properties")) {
			app.load(in);
		}
		return Flyway.configure()
			.dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
			.schemas(schema)
			.target(target)
			.baselineOnMigrate(Boolean.parseBoolean(app.getProperty("spring.flyway.baseline-on-migrate", "false")))
			.load();
	}

	private static List<String> foreignKeyAndUniqueNames(String schema) throws SQLException {
		String sql = """
				select c.conname from pg_constraint c join pg_namespace n on n.oid = c.connamespace
				where n.nspname = ? and c.contype in ('f', 'u') order by c.conname""";
		List<String> names = new ArrayList<>();
		try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, schema);
			try (ResultSet rows = statement.executeQuery()) {
				while (rows.next()) {
					names.add(rows.getString(1));
				}
			}
		}
		return names;
	}

	private static Connection connect() throws SQLException {
		return DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
	}

	private static String resource(String path) throws IOException {
		try (InputStream in = MigrationTest.class.getClassLoader().getResourceAsStream(path)) {
			return new String(in.readAllBytes(), StandardCharsets.UTF_8);
		}
	}

}
