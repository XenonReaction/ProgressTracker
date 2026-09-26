package com.progressiontracker.progression.readiness;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.progressiontracker.progression.node.NodeResourceType;
import com.progressiontracker.progression.tree.TreeNodeRepository;

/** Hands out {@link ReadinessContext}s backed by one calculator per resource type that can count. */
@Service
public class ReadinessService {

	private final Map<NodeResourceType, ReadinessCalculator> calculators = new EnumMap<>(NodeResourceType.class);

	private final TreeNodeRepository treeNodes;

	public ReadinessService(List<ReadinessCalculator> calculators, TreeNodeRepository treeNodes) {
		for (ReadinessCalculator calculator : calculators) {
			if (this.calculators.put(calculator.resourceType(), calculator) != null) {
				throw new IllegalStateException("Two readiness calculators for " + calculator.resourceType());
			}
		}
		for (NodeResourceType type : NodeResourceType.values()) {
			if (type.canCount() && !this.calculators.containsKey(type)) {
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
