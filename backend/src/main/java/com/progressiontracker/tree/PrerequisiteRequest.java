package com.progressiontracker.tree;

import jakarta.validation.constraints.NotNull;

/** Both ids are tree node ids in the same tree. */
public record PrerequisiteRequest(@NotNull Long prerequisiteTreeNodeId, @NotNull Long dependentTreeNodeId) {
}
