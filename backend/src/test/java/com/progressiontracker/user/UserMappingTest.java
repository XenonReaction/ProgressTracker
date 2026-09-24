package com.progressiontracker.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.progressiontracker.TestcontainersConfiguration;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class UserMappingTest {

	@Autowired
	private TestEntityManager em;

	@Test
	void persistsAndReloadsUser() {
		Long id = em.persistAndGetId(new User("alice"), Long.class);
		em.flush();
		em.clear();

		User reloaded = em.find(User.class, id);

		assertThat(reloaded.getUsername()).isEqualTo("alice");
		assertThat(reloaded.getCreatedAt()).isNotNull();
	}

	@Test
	void rejectsDuplicateUsername() {
		em.persistAndFlush(new User("alice"));

		assertThatThrownBy(() -> em.persistAndFlush(new User("alice")))
			.isInstanceOf(ConstraintViolationException.class)
			.extracting("constraintName")
			.isEqualTo("users_username_unique");
	}

}
