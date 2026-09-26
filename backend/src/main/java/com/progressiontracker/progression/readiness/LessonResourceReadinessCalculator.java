package com.progressiontracker.progression.readiness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.progressiontracker.lessons.LessonReadinessCalculator;
import com.progressiontracker.progression.node.NodeResource;
import com.progressiontracker.progression.node.NodeResourceType;

/**
 * A lesson resource: the latest progress the user entered on it, and when they last opened
 * it, from the Lessons module's public API.
 */
@Component
class LessonResourceReadinessCalculator implements ReadinessCalculator {

	private static final Logger log = LoggerFactory.getLogger(LessonResourceReadinessCalculator.class);

	private final LessonReadinessCalculator lessons;

	LessonResourceReadinessCalculator(LessonReadinessCalculator lessons) {
		this.lessons = lessons;
	}

	@Override
	public NodeResourceType resourceType() {
		return NodeResourceType.LESSON;
	}

	@Override
	public ResourceStatus status(NodeResource resource, ReadinessContext context) {
		return lessons.lesson(resource.getLessonId())
			.map(lesson -> new ResourceStatus(lesson.title(), lesson.progress(), lesson.lastReviewedAt()))
			.orElseGet(() -> {
				// Deleting a lesson that nodes list is refused, so this only guards against bad data
				log.warn("Lesson {} not found; counting it as 0% readiness", resource.getLessonId());
				return new ResourceStatus(null, 0, null);
			});
	}

}
