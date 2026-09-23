package com.progressiontracker;

import org.springframework.test.util.ReflectionTestUtils;

/** Helpers for unit tests that build entities without a database. */
public final class TestEntities {

	private TestEntities() {
	}

	/** Sets the generated id that the database would normally assign. */
	public static <T> T withId(T entity, long id) {
		ReflectionTestUtils.setField(entity, "id", id);
		return entity;
	}

}
