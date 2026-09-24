package com.progressiontracker.tree;

import java.time.Instant;

/** A tree's edit session: when "Edit" was clicked. */
public record TreeEditSessionResponse(Instant startedAt) {
}
