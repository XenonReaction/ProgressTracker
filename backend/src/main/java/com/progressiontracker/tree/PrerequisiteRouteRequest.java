package com.progressiontracker.tree;

import jakarta.validation.Valid;

/** Body for setting an edge's hand-adjusted route; a null {@code route} resets it to the default. */
public record PrerequisiteRouteRequest(@Valid EdgeRoute route) {
}
