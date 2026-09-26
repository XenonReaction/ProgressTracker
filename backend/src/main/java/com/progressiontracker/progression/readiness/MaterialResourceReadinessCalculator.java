package com.progressiontracker.progression.readiness;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.progressiontracker.materials.MaterialReadinessCalculator;
import com.progressiontracker.materials.MaterialSummary;
import com.progressiontracker.progression.node.NodeResource;
import com.progressiontracker.progression.node.NodeResourceType;

/**
 * An external material resource: the latest progress the user reported on it, and when, from
 * the Materials module's public API.
 */
@Component
class MaterialResourceReadinessCalculator implements ReadinessCalculator {

	private static final Logger log = LoggerFactory.getLogger(MaterialResourceReadinessCalculator.class);

	private final MaterialReadinessCalculator materials;

	MaterialResourceReadinessCalculator(MaterialReadinessCalculator materials) {
		this.materials = materials;
	}

	@Override
	public NodeResourceType resourceType() {
		return NodeResourceType.MATERIAL;
	}

	@Override
	public ResourceStatus status(NodeResource resource, ReadinessContext context) {
		return statusOf(resource.getMaterialId(), materials.material(resource.getMaterialId()).orElse(null));
	}

	@Override
	public Map<String, ResourceStatus> statuses(Collection<NodeResource> resources, ReadinessContext context) {
		Map<Long, MaterialSummary> found = materials
			.materials(resources.stream().map(NodeResource::getMaterialId).collect(Collectors.toSet()));
		return ReadinessCalculator.byTargetKey(resources,
				resource -> statusOf(resource.getMaterialId(), found.get(resource.getMaterialId())));
	}

	private static ResourceStatus statusOf(Long materialId, MaterialSummary material) {
		if (material == null) {
			// Deleting a material that nodes list is refused, so this only guards against bad data
			log.warn("Material {} not found; counting it as 0% readiness", materialId);
			return new ResourceStatus(null, 0, null);
		}
		return new ResourceStatus(material.title(), material.progress(), material.lastReviewedAt());
	}

}
