package com.progressiontracker.tree;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Both ids are tree node ids in the same tree. {@code route} is optional: undo uses it to put
 * back an edge with the shape it had.
 */
public record PrerequisiteRequest(@NotNull Long prerequisiteTreeNodeId, @NotNull Long dependentTreeNodeId,
		@Valid EdgeRoute route) {
}
