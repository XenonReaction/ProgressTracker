package com.progressiontracker.common;

import java.util.Map;

/**
 * The request is well-formed but conflicts with the current state (duplicate, in use,
 * would create a cycle). Returns 409. {@code properties} are added to the problem
 * response so clients can show details, e.g. which trees still use a node.
 */
public class ConflictException extends RuntimeException {

	private final Map<String, Object> properties;

	public ConflictException(String message) {
		this(message, Map.of());
	}

	public ConflictException(String message, Map<String, Object> properties) {
		super(message);
		this.properties = Map.copyOf(properties);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

}
