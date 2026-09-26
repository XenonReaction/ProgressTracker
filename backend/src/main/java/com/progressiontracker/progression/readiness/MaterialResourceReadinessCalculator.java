package com.progressiontracker.progression.readiness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.progressiontracker.materials.MaterialReadinessCalculator;
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
		return materials.material(resource.getMaterialId())
			.map(material -> new ResourceStatus(material.title(), material.progress(), material.lastReviewedAt()))
			.orElseGet(() -> {
				// Deleting a material that nodes list is refused, so this only guards against bad data
				log.warn("Material {} not found; counting it as 0% readiness", resource.getMaterialId());
				return new ResourceStatus(null, 0, null);
			});
	}

}
