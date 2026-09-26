package com.progressiontracker.coding.internal;

/** What the user did on a question. */
public enum AttemptAction {

	/** Asked to see the solution. */
	REVEALED("revealed"),

	/** Marked the question solved. */
	SOLVED("solved");

	private final String dbValue;

	AttemptAction(String dbValue) {
		this.dbValue = dbValue;
	}

	public String getDbValue() {
		return dbValue;
	}

	public static AttemptAction fromDbValue(String dbValue) {
		for (AttemptAction action : values()) {
			if (action.dbValue.equals(dbValue)) {
				return action;
			}
		}
		throw new IllegalArgumentException("Unknown attempt action: " + dbValue);
	}

}
