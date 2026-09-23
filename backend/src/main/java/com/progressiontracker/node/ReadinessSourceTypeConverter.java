package com.progressiontracker.node;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Stores {@link ReadinessSourceType} as a plain lowercase string. Unlike
 * {@code @Enumerated(STRING)}, this doesn't make Hibernate generate a check constraint
 * listing the enum values, which {@code ddl-auto=update} would never widen when new
 * source types are added.
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
