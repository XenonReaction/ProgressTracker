package com.progressiontracker.common;

/** The request breaks a rule that field-level validation can't express. Returns 400. */
public class BadRequestException extends RuntimeException {

	public BadRequestException(String message) {
		super(message);
	}

}
