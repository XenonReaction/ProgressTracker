package com.progressiontracker.progression.node;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Stores {@link ReadinessSourceType} as its lowercase database value. The allowed values
 * are also listed in the {@code nodes_readiness_source_type_check} constraint, so adding a
 * source type needs a migration that widens it.
 */
@Converter(autoApply = true)
class ReadinessSourceTypeConverter implements AttributeConverter<ReadinessSourceType, String> {

	@Override
	public String convertToDatabaseColumn(ReadinessSourceType type) {
		return type == null ? null : type.getDbValue();
	}

	@Override
	public ReadinessSourceType convertToEntityAttribute(String dbValue) {
		return dbValue == null ? null : ReadinessSourceType.fromDbValue(dbValue);
	}

}
