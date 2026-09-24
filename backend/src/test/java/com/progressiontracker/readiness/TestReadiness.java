package com.progressiontracker.readiness;

import java.util.List;

import com.progressiontracker.tree.TreeNodeRepository;

/** A real {@link ReadinessService} for unit tests, reading linked trees from a mock repository. */
public final class TestReadiness {

	private TestReadiness() {
	}

	public static ReadinessService service(TreeNodeRepository treeNodes) {
		return new ReadinessService(List.of(new ManualReadinessCalculator(), new LinkedTreeReadinessCalculator()),
				treeNodes);
	}

}
