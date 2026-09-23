package com.progressiontracker.tree;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.progressiontracker.tree.PrerequisiteGraph.Edge;

class PrerequisiteGraphTest {

	@Test
	void emptyGraphHasNoCycles() {
		assertThat(PrerequisiteGraph.of(List.of()).wouldCreateCycle(1L, 2L)).isFalse();
	}

	@Test
	void reverseOfExistingEdgeIsACycle() {
		PrerequisiteGraph graph = PrerequisiteGraph.of(List.of(new Edge(1L, 2L)));

		assertThat(graph.wouldCreateCycle(2L, 1L)).isTrue();
	}

	@Test
	void closingALongChainIsACycle() {
		PrerequisiteGraph graph = PrerequisiteGraph.of(List.of(new Edge(1L, 2L), new Edge(2L, 3L), new Edge(3L, 4L)));

		assertThat(graph.wouldCreateCycle(4L, 1L)).isTrue();
	}

	@Test
	void diamondShortcutIsNotACycle() {
		// 1 -> 2 -> 4 and 1 -> 3 -> 4; adding 1 -> 4 is redundant but acyclic
		PrerequisiteGraph graph = PrerequisiteGraph
			.of(List.of(new Edge(1L, 2L), new Edge(1L, 3L), new Edge(2L, 4L), new Edge(3L, 4L)));

		assertThat(graph.wouldCreateCycle(1L, 4L)).isFalse();
	}

	@Test
	void edgeBetweenUnrelatedBranchesIsNotACycle() {
		PrerequisiteGraph graph = PrerequisiteGraph.of(List.of(new Edge(1L, 2L), new Edge(3L, 4L)));

		assertThat(graph.wouldCreateCycle(2L, 3L)).isFalse();
		assertThat(graph.wouldCreateCycle(4L, 1L)).isFalse();
	}

	@Test
	void terminatesOnGraphsWithSharedDescendants() {
		// 1 fans out to 2 and 3, both of which lead to 4 -> 5
		PrerequisiteGraph graph = PrerequisiteGraph.of(List.of(new Edge(1L, 2L), new Edge(1L, 3L), new Edge(2L, 4L),
				new Edge(3L, 4L), new Edge(4L, 5L)));

		assertThat(graph.wouldCreateCycle(5L, 1L)).isTrue();
		assertThat(graph.wouldCreateCycle(6L, 1L)).isFalse();
	}

}
