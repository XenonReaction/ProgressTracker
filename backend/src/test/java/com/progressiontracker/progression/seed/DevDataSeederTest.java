package com.progressiontracker.progression.seed;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.progressiontracker.TestcontainersConfiguration;
import com.progressiontracker.progression.tree.TreeNode;

@SpringBootTest
@ActiveProfiles("dev")
@Import(TestcontainersConfiguration.class)
class DevDataSeederTest {

	@Autowired
	private DevDataSeeder seeder;

	@Autowired
	private EntityManager em;

	@Test
	void seedsDemoDataOnStartupAndIsIdempotent() {
		assertSeededCounts();

		seeder.run(new DefaultApplicationArguments());

		assertSeededCounts();
	}

	@Test
	void seededStreamsNodeUsesCustomThresholds() {
		TreeNode streams = em
			.createQuery("select tn from TreeNode tn where tn.node.title = 'Streams API'", TreeNode.class)
			.getSingleResult();

		assertThat(streams.getAggregateThreshold()).isEqualTo(85);
		assertThat(streams.getIndividualThreshold()).isEqualTo(75);
	}

	private void assertSeededCounts() {
		assertThat(count("select count(u) from User u")).isEqualTo(1);
		assertThat(count("select count(n) from Node n")).isEqualTo(10);
		assertThat(count("select count(t) from Tree t")).isEqualTo(3);
		assertThat(count("select count(tn) from TreeNode tn")).isEqualTo(10);
		assertThat(count("select count(p) from Prerequisite p")).isEqualTo(8);
		assertThat(count("select count(n) from Node n join n.resources r where r.tree is not null and r.counts = true"))
			.isEqualTo(1);
	}

	private long count(String jpql) {
		return em.createQuery(jpql, Long.class).getSingleResult();
	}

}
