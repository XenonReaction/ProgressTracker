package com.progressiontracker.materials.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.progressiontracker.TestcontainersConfiguration;
import com.progressiontracker.user.User;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class MaterialMappingTest {

	@Autowired
	private TestEntityManager em;

	@Autowired
	private MaterialProgressUpdateRepository updates;

	private User owner;

	private Material guide;

	@BeforeEach
	void setUp() {
		owner = em.persist(new User("alice"));
		guide = new Material(owner, "Flexbox guide", "https://example.com");
		guide.setNotes("Container first");
		em.persist(guide);
	}

	@Test
	void theLatestUpdateIsFoundFirst() {
		Instant earlier = Instant.now().minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MICROS);
		em.persist(new MaterialProgressUpdate(guide, owner, earlier, 40, "Container"));
		em.persist(new MaterialProgressUpdate(guide, owner, earlier.plusSeconds(60), 60, null));
		em.flush();
		em.clear();

		Material reloaded = em.find(Material.class, guide.getId());
		assertThat(reloaded.getNotes()).isEqualTo("Container first");
		assertThat(updates.findFirstByMaterialOrderByRecordedAtDescIdDesc(reloaded)).get()
			.extracting(MaterialProgressUpdate::getProgress)
			.isEqualTo(60);
		assertThat(updates.findByMaterialOrderByRecordedAtDescIdDesc(reloaded))
			.extracting(MaterialProgressUpdate::getNote)
			.containsExactly(null, "Container");
	}

	@Test
	void progressOutsideZeroToHundredIsRefused() {
		assertThatThrownBy(() -> em.persistAndFlush(new MaterialProgressUpdate(guide, owner, Instant.now(), 101, null)))
			.isInstanceOf(ConstraintViolationException.class)
			.extracting("constraintName")
			.isEqualTo("material_progress_updates_progress_range");
	}

	@Test
	void deletingAMaterialDeletesItsUpdates() {
		em.persist(new MaterialProgressUpdate(guide, owner, Instant.now(), 10, null));
		em.flush();
		em.clear();

		em.remove(em.find(Material.class, guide.getId()));
		em.flush();

		assertThat(em.getEntityManager()
			.createQuery("select count(u) from MaterialProgressUpdate u", Long.class)
			.getSingleResult()).isZero();
	}

}
