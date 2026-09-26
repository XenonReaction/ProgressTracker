package com.progressiontracker.progression.node;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Stores {@link NodeResourceType} as its lowercase database value. The allowed values are
 * also listed in the {@code node_resources_resource_type_check} constraint, so adding a type
 * needs a migration that widens it.
 */
@Converter(autoApply = true)
class NodeResourceTypeConverter implements AttributeConverter<NodeResourceType, String> {

	@Override
	public String convertToDatabaseColumn(NodeResourceType type) {
		return type == null ? null : type.getDbValue();
	}

	@Override
	public NodeResourceType convertToEntityAttribute(String dbValue) {
		return dbValue == null ? null : NodeResourceType.fromDbValue(dbValue);
	}

}
