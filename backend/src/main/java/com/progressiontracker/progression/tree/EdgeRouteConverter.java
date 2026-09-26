package com.progressiontracker.progression.tree;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import tools.jackson.databind.json.JsonMapper;

/** Stores an {@link EdgeRoute} as JSON text; null stays null (the default route). */
@Converter
class EdgeRouteConverter implements AttributeConverter<EdgeRoute, String> {

	private static final JsonMapper JSON = JsonMapper.builder().build();

	@Override
	public String convertToDatabaseColumn(EdgeRoute route) {
		return route == null ? null : JSON.writeValueAsString(route);
	}

	@Override
	public EdgeRoute convertToEntityAttribute(String json) {
		return json == null ? null : JSON.readValue(json, EdgeRoute.class);
	}

}
