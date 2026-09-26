package com.progressiontracker.progression.readiness;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.progressiontracker.coding.QuestionSetReadinessCalculator;
import com.progressiontracker.coding.QuestionSetSummary;
import com.progressiontracker.progression.node.NodeResource;
import com.progressiontracker.progression.node.NodeResourceType;

/**
 * A coding question set resource: the share of its questions solved, and the latest attempt
 * on any of them, from the Coding practice module's public API.
 */
@Component
class QuestionSetResourceReadinessCalculator implements ReadinessCalculator {

	private static final Logger log = LoggerFactory.getLogger(QuestionSetResourceReadinessCalculator.class);

	private final QuestionSetReadinessCalculator questionSets;

	QuestionSetResourceReadinessCalculator(QuestionSetReadinessCalculator questionSets) {
		this.questionSets = questionSets;
	}

	@Override
	public NodeResourceType resourceType() {
		return NodeResourceType.QUESTION_SET;
	}

	@Override
	public ResourceStatus status(NodeResource resource, ReadinessContext context) {
		return statusOf(resource.getQuestionSetId(), questionSets.questionSet(resource.getQuestionSetId()).orElse(null));
	}

	@Override
	public Map<String, ResourceStatus> statuses(Collection<NodeResource> resources, ReadinessContext context) {
		Map<Long, QuestionSetSummary> found = questionSets
			.questionSets(resources.stream().map(NodeResource::getQuestionSetId).collect(Collectors.toSet()));
		return ReadinessCalculator.byTargetKey(resources,
				resource -> statusOf(resource.getQuestionSetId(), found.get(resource.getQuestionSetId())));
	}

	private static ResourceStatus statusOf(Long setId, QuestionSetSummary set) {
		if (set == null) {
			// Deleting a question set that nodes list is refused, so this only guards against bad data
			log.warn("Question set {} not found; counting it as 0% readiness", setId);
			return new ResourceStatus(null, 0, null);
		}
		return new ResourceStatus(set.title(), set.progress().readiness(), set.progress().lastReviewedAt());
	}

}
