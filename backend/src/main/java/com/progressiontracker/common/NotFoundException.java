package com.progressiontracker.common;

/**
 * A requested resource doesn't exist, or doesn't belong to the current user. Both cases
 * return 404 so the API doesn't reveal other users' data.
 */
public class NotFoundException extends RuntimeException {

	public NotFoundException(String resource, Long id) {
		super(resource + " " + id + " not found");
	}

}
