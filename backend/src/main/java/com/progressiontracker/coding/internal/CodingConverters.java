package com.progressiontracker.coding.internal;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Stores this module's enums as their lowercase database values. The allowed values are
 * also listed in check constraints ({@code coding_questions_language_check},
 * {@code question_attempts_action_check}), so a new value needs a migration that widens them.
 */
final class CodingConverters {

	private CodingConverters() {
	}

	@Converter(autoApply = true)
	static class LanguageConverter implements AttributeConverter<CodingLanguage, String> {

		@Override
		public String convertToDatabaseColumn(CodingLanguage language) {
			return language == null ? null : language.getDbValue();
		}

		@Override
		public CodingLanguage convertToEntityAttribute(String dbValue) {
			CodingLanguage language = CodingLanguage.fromDbValue(dbValue);
			if (dbValue != null && language == null) {
				throw new IllegalArgumentException("Unknown coding language: " + dbValue);
			}
			return language;
		}

	}

	@Converter(autoApply = true)
	static class ActionConverter implements AttributeConverter<AttemptAction, String> {

		@Override
		public String convertToDatabaseColumn(AttemptAction action) {
			return action == null ? null : action.getDbValue();
		}

		@Override
		public AttemptAction convertToEntityAttribute(String dbValue) {
			return dbValue == null ? null : AttemptAction.fromDbValue(dbValue);
		}

	}

}
