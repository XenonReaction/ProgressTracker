package com.progressiontracker.progression.node;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.progressiontracker.common.ConflictException;
import com.progressiontracker.materials.MaterialDeletionCheck;

/** Refuses to delete a material that nodes list as a resource (409, listing them), as for trees and decks. */
@Component
class NodeMaterialDeletionCheck implements MaterialDeletionCheck {

	private final NodeRepository nodes;

	NodeMaterialDeletionCheck(NodeRepository nodes) {
		this.nodes = nodes;
	}

	@Override
	public void checkCanDelete(Long materialId) {
		List<Node> listing = nodes.findListing(NodeResourceType.MATERIAL, materialId);
		if (!listing.isEmpty()) {
			throw new ConflictException("Material " + materialId + " is a resource of " + listing.size()
					+ " node(s); remove it from them first",
					Map.of("nodes", listing.stream().map(NodeRef::of).toList()));
		}
	}

}
