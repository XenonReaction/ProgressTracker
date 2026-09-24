package com.progressiontracker.db;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Runs the Flyway migrations the way the app does, both on an empty database and on one
 * whose tables Hibernate created before Flyway was added. Each test uses its own schema in
 * one shared container.
 */
@Testcontainers
class MigrationTest {

	@Container
	static final PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:18"));

	/** Every named foreign key and unique constraint after V2. */
	private static final List<String> READABLE_NAMES = List.of("node_links_node_id_fk", "nodes_user_id_fk",
			"prerequisites_dependent_tree_node_id_fk", "prerequisites_edge_unique",
			"prerequisites_prerequisite_tree_node_id_fk", "tree_nodes_node_id_fk", "tree_nodes_tree_id_fk",
			"tree_nodes_tree_node_unique", "tree_tags_tree_id_fk", "trees_user_id_fk", "users_username_unique");

	@Test
	void emptyDatabaseRunsEveryMigration() throws Exception {
		MigrateResult result = flyway("fresh").migrate();

		assertThat(result.migrations).extracting(m -> m.version).containsExactly("1", "2");
		assertThat(result.targetSchemaVersion).isEqualTo("2");
		assertThat(foreignKeyAndUniqueNames("fresh")).containsExactlyElementsOf(READABLE_NAMES);
	}

	@Test
	void databaseCreatedByHibernateIsBaselinedAtV1AndKeepsItsData() throws Exception {
		// What ddl-auto=update left behind: V1's tables and some data, but no Flyway history
		try (Connection connection = connect(); Statement statement = connection.createStatement()) {
			statement.execute("create schema legacy");
			statement.execute("set search_path to legacy");
			statement.execute(resource("db/migration/V1__initial_schema.sql"));
			statement.execute("insert into users (created_at, username) values (now(), 'demo')");
		}

		MigrateResult result = flyway("legacy").migrate();

		assertThat(result.migrations).extracting(m -> m.version).containsExactly("2");
		assertThat(foreignKeyAndUniqueNames("legacy")).containsExactlyElementsOf(READABLE_NAMES);
		try (Connection connection = connect(); Statement statement = connection.createStatement();
				ResultSet rows = statement.executeQuery("select username from legacy.users")) {
			assertThat(rows.next()).isTrue();
			assertThat(rows.getString(1)).isEqualTo("demo");
		}
	}

	/** Flyway with the app's settings from application.properties, pointed at one schema. */
	private static Flyway flyway(String schema) throws IOException {
		Properties app = new Properties();
		try (InputStream in = MigrationTest.class.getClassLoader().getResourceAsStream("application.properties")) {
			app.load(in);
		}
		return Flyway.configure()
			.dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
			.schemas(schema)
			.baselineOnMigrate(Boolean.parseBoolean(app.getProperty("spring.flyway.baseline-on-migrate")))
			.baselineVersion(app.getProperty("spring.flyway.baseline-version"))
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
