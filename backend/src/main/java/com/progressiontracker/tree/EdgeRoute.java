package com.progressiontracker.tree;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import com.progressiontracker.common.BadRequestException;

/**
 * How a right-angle edge was adjusted by hand. The route itself is worked out by the
 * frontend from the two nodes' positions; this only records, for the route with
 * {@code segments} segments, how far each draggable segment was moved from its default
 * place. A 3-segment route has one draggable segment (the middle one), a 5-segment route
 * three. Offsets rather than points keep an adjustment when either node moves.
 */
public record EdgeRoute(@NotNull Integer segments, @NotNull List<@NotNull Double> offsets) {

	/** Refuses a route whose offsets don't match its number of segments, or aren't finite. */
	void check() {
		int expected = switch (segments) {
			case 3 -> 1;
			case 5 -> 3;
			default -> throw new BadRequestException(
					"An edge route has 3 or 5 segments, not " + segments);
		};
		if (offsets.size() != expected) {
			throw new BadRequestException(
					"A " + segments + "-segment route has " + expected + " offset(s), not " + offsets.size());
		}
		if (offsets.stream().anyMatch(offset -> !Double.isFinite(offset) || Math.abs(offset) > 100_000)) {
			throw new BadRequestException("Route offsets must be finite numbers");
		}
	}

}
