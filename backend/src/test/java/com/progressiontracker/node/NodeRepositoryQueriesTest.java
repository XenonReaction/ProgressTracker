package com.progressiontracker.node;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.progressiontracker.TestcontainersConfiguration;
import com.progressiontracker.user.User;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class NodeRepositoryQueriesTest {

	@Autowired
	private TestEntityManager em;

	@Autowired
	private NodeRepository nodes;

	@Test
	void nodesAreScopedToTheirOwnerAndSortedByTitle() {
		User alice = em.persist(new User("alice"));
		User bob = em.persist(new User("bob"));
		Node streams = em.persist(new Node(alice, "Streams"));
		Node generics = em.persist(new Node(alice, "Generics"));
		Node bobsNode = em.persist(new Node(bob, "Bob's node"));

		assertThat(nodes.findByOwnerOrderByTitleAsc(alice)).containsExactly(generics, streams);
		assertThat(nodes.findByIdAndOwner(streams.getId(), alice)).contains(streams);
		assertThat(nodes.findByIdAndOwner(bobsNode.getId(), alice)).isEmpty();
		assertThat(nodes.existsByOwner(bob)).isTrue();
		assertThat(nodes.existsByOwner(em.persist(new User("carol")))).isFalse();
	}

}
