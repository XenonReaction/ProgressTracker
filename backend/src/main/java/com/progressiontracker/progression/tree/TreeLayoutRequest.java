package com.progressiontracker.progression.tree;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * New positions for some or all of a tree's nodes, applied together (e.g. auto-layout).
 * Tree nodes not listed keep their position.
 */
public record TreeLayoutRequest(@NotNull List<@NotNull @Valid Position> positions) {

	public record Position(@NotNull Long treeNodeId, @NotNull Double positionX, @NotNull Double positionY) {
	}

}
