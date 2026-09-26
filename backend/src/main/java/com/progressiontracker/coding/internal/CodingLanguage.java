package com.progressiontracker.coding.internal;

/**
 * The language a question is written for. HTML and CSS come first; adding JavaScript means an
 * enum value and a migration widening {@code coding_questions_language_check}.
 */
public enum CodingLanguage {

	HTML("html"),

	CSS("css");

	private final String dbValue;

	CodingLanguage(String dbValue) {
		this.dbValue = dbValue;
	}

	public String getDbValue() {
		return dbValue;
	}

	/** The language for a stored or requested value, or null if it isn't one. */
	public static CodingLanguage fromDbValue(String dbValue) {
		for (CodingLanguage language : values()) {
			if (language.dbValue.equals(dbValue)) {
				return language;
			}
		}
		return null;
	}

}
