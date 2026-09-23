package com.progressiontracker.node;

/**
 * Where a node's readiness value comes from. Milestone 1 only supports manual entry;
 * derived sources (e.g. a linked tree's aggregate) are Milestone 2+.
 */
public enum ReadinessSourceType {

	MANUAL("manual");

	private final String dbValue;

	ReadinessSourceType(String dbValue) {
		this.dbValue = dbValue;
	}

	public String getDbValue() {
		return dbValue;
	}

	public static ReadinessSourceType fromDbValue(String dbValue) {
		for (ReadinessSourceType type : values()) {
			if (type.dbValue.equals(dbValue)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown readiness source type: " + dbValue);
	}

}
