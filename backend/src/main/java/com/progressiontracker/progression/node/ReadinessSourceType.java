package com.progressiontracker.progression.node;

/**
 * Where a node's readiness value comes from. Each type has a
 * {@link com.progressiontracker.progression.readiness.ReadinessCalculator}.
 */
public enum ReadinessSourceType {

	/** Entered by hand. */
	MANUAL("manual"),

	/** The average readiness of the nodes in the node's linked tree. */
	LINKED_TREE("linked_tree");

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
