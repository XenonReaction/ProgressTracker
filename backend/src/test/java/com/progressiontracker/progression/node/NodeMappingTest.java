package com.progressiontracker.progression.node;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.progressiontracker.TestcontainersConfiguration;
import com.progressiontracker.user.User;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class NodeMappingTest {

	@Autowired
	private TestEntityManager em;

	private User owner;

	@BeforeEach
	void setUp() {
		owner = em.persist(new User("alice"));
	}

	@Test
	void newNodeDefaultsToZeroManualReadiness() {
		Long id = em.persistAndGetId(new Node(owner, "Generics"), Long.class);
		em.flush();
		em.clear();

		Node reloaded = em.find(Node.class, id);

		assertThat(reloaded.getReadiness()).isZero();
		assertThat(reloaded.getReadinessSourceType()).isEqualTo(ReadinessSourceType.MANUAL);
		assertThat(reloaded.getCreatedAt()).isNotNull();
		assertThat(reloaded.getUpdatedAt()).isNotNull();
	}

	@Test
	void readinessSourceTypeIsStoredAsLowercaseString() {
		Long id = em.persistAndGetId(new Node(owner, "Generics"), Long.class);
		em.flush();

		Object stored = em.getEntityManager()
			.createNativeQuery("select readiness_source_type from nodes where id = :id")
			.setParameter("id", id)
			.getSingleResult();

		assertThat(stored).isEqualTo("manual");
	}

	@Test
	void linksArePersistedInOrder() {
		Node node = new Node(owner, "Generics");
		node.getLinks().add(new NodeLink("https://example.com/first", "First"));
		node.getLinks().add(new NodeLink("https://example.com/second", null));
		Long id = em.persistAndGetId(node, Long.class);
		em.flush();
		em.clear();

		Node reloaded = em.find(Node.class, id);

		assertThat(reloaded.getLinks()).extracting(NodeLink::getUrl)
			.containsExactly("https://example.com/first", "https://example.com/second");
		assertThat(reloaded.getLinks()).extracting(NodeLink::getLabel).containsExactly("First", null);
	}

	@ParameterizedTest
	@ValueSource(ints = { -1, 101 })
	void rejectsReadinessOutsideZeroToHundred(int readiness) {
		Node node = new Node(owner, "Generics");
		node.setReadiness(readiness);

		assertThatThrownBy(() -> em.persistAndFlush(node)).isInstanceOf(ConstraintViolationException.class)
			.extracting("constraintName")
			.isEqualTo("nodes_readiness_range");
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 100 })
	void acceptsReadinessBoundaries(int readiness) {
		Node node = new Node(owner, "Generics");
		node.setReadiness(readiness);

		em.persistAndFlush(node);

		assertThat(node.getId()).isNotNull();
	}

}
