package com.progressiontracker.progression.readiness;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.progressiontracker.lessons.LessonReadinessCalculator;
import com.progressiontracker.lessons.LessonSummary;
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
		return statusOf(resource.getLessonId(), lessons.lesson(resource.getLessonId()).orElse(null));
	}

	@Override
	public Map<String, ResourceStatus> statuses(Collection<NodeResource> resources, ReadinessContext context) {
		Map<Long, LessonSummary> found = lessons
			.lessons(resources.stream().map(NodeResource::getLessonId).collect(Collectors.toSet()));
		return ReadinessCalculator.byTargetKey(resources,
				resource -> statusOf(resource.getLessonId(), found.get(resource.getLessonId())));
	}

	private static ResourceStatus statusOf(Long lessonId, LessonSummary lesson) {
		if (lesson == null) {
			// Deleting a lesson that nodes list is refused, so this only guards against bad data
			log.warn("Lesson {} not found; counting it as 0% readiness", lessonId);
			return new ResourceStatus(null, 0, null);
		}
		return new ResourceStatus(lesson.title(), lesson.progress(), lesson.lastReviewedAt());
	}

}
