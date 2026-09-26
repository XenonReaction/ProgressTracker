package com.progressiontracker.progression.readiness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.progressiontracker.coding.QuestionSetReadinessCalculator;
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
		return questionSets.questionSet(resource.getQuestionSetId())
			.map(set -> new ResourceStatus(set.title(), set.progress().readiness(), set.progress().lastReviewedAt()))
			.orElseGet(() -> {
				// Deleting a question set that nodes list is refused, so this only guards against bad data
				log.warn("Question set {} not found; counting it as 0% readiness", resource.getQuestionSetId());
				return new ResourceStatus(null, 0, null);
			});
	}

}
