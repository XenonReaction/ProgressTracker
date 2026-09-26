package com.progressiontracker.progression.node;

/**
 * What a node resource points to. A type that {@link #canCount() can count} toward readiness
 * has a {@link com.progressiontracker.progression.readiness.ReadinessCalculator}.
 */
public enum NodeResourceType {

	/** An external page, for reading only: it never counts. */
	URL("url", false),

	/** Another tree in the app: its readiness is the average of its nodes. */
	TREE("tree", true),

	/** A flashcard deck, by id: its readiness is the share of its cards passed. */
	DECK("deck", true);

	private final String dbValue;

	private final boolean canCount;

	NodeResourceType(String dbValue, boolean canCount) {
		this.dbValue = dbValue;
		this.canCount = canCount;
	}

	public String getDbValue() {
		return dbValue;
	}

	public boolean canCount() {
		return canCount;
	}

	public static NodeResourceType fromDbValue(String dbValue) {
		for (NodeResourceType type : values()) {
			if (type.dbValue.equals(dbValue)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown node resource type: " + dbValue);
	}

}
