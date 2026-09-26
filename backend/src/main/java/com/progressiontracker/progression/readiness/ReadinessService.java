package com.progressiontracker.progression.readiness;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.progressiontracker.progression.node.ReadinessSourceType;
import com.progressiontracker.progression.tree.TreeNodeRepository;

/** Hands out {@link ReadinessContext}s backed by one calculator per readiness source type. */
@Service
public class ReadinessService {

	private final Map<ReadinessSourceType, ReadinessCalculator> calculators = new EnumMap<>(
			ReadinessSourceType.class);

	private final TreeNodeRepository treeNodes;

	public ReadinessService(List<ReadinessCalculator> calculators, TreeNodeRepository treeNodes) {
		for (ReadinessCalculator calculator : calculators) {
			if (this.calculators.put(calculator.sourceType(), calculator) != null) {
				throw new IllegalStateException("Two readiness calculators for " + calculator.sourceType());
			}
		}
		for (ReadinessSourceType type : ReadinessSourceType.values()) {
			if (!this.calculators.containsKey(type)) {
				throw new IllegalStateException("No readiness calculator for " + type);
			}
		}
		this.treeNodes = treeNodes;
	}

	/** A fresh context: use one per request, so values are computed once and never stale. */
	public ReadinessContext context() {
		return new ReadinessContext(calculators, treeNodes::findNodesInTree);
	}

}
