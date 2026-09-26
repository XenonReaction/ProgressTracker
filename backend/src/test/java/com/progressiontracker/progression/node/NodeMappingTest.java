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
import com.progressiontracker.progression.tree.Tree;
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
	void newNodeDefaultsToZeroReadinessAndNoResources() {
		Long id = em.persistAndGetId(new Node(owner, "Generics"), Long.class);
		em.flush();
		em.clear();

		Node reloaded = em.find(Node.class, id);

		assertThat(reloaded.getReadiness()).isZero();
		assertThat(reloaded.getResources()).isEmpty();
		assertThat(reloaded.getCreatedAt()).isNotNull();
		assertThat(reloaded.getUpdatedAt()).isNotNull();
	}

	@Test
	void resourcesArePersistedInOrderWithTheirTypeStoredAsLowercaseText() {
		Tree tree = em.persist(new Tree(owner, "Collections"));
		Node node = new Node(owner, "Generics");
		node.getResources().add(NodeResource.url("https://example.com/first", "First"));
		node.getResources().add(NodeResource.tree(tree, null, true));
		node.getResources().add(NodeResource.url("https://example.com/second", null));
		Long id = em.persistAndGetId(node, Long.class);
		em.flush();
		em.clear();

		Node reloaded = em.find(Node.class, id);

		assertThat(reloaded.getResources()).extracting(NodeResource::getType)
			.containsExactly(NodeResourceType.URL, NodeResourceType.TREE, NodeResourceType.URL);
		assertThat(reloaded.getResources()).extracting(NodeResource::getUrl)
			.containsExactly("https://example.com/first", null, "https://example.com/second");
		assertThat(reloaded.getResources()).extracting(NodeResource::getLabel).containsExactly("First", null, null);
		assertThat(reloaded.getResources().get(1).getTree().getId()).isEqualTo(tree.getId());
		assertThat(reloaded.countingTrees()).extracting(Tree::getId).containsExactly(tree.getId());
		assertThat(em.getEntityManager()
			.createNativeQuery("select resource_type from node_resources where node_id = :id order by position")
			.setParameter("id", id)
			.getResultList()).containsExactly("url", "tree", "url");
	}

	@Test
	void aUrlThatCountsIsRefusedByTheDatabase() {
		Long id = em.persistAndGetId(new Node(owner, "Generics"), Long.class);
		em.flush();

		assertThatThrownBy(() -> em.getEntityManager()
			.createNativeQuery("insert into node_resources (node_id, position, resource_type, url, counts) "
					+ "values (:id, 0, 'url', 'https://example.com', true)")
			.setParameter("id", id)
			.executeUpdate()).hasMessageContaining("node_resources_url_never_counts");
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
