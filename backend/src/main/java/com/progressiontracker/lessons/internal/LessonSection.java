package com.progressiontracker.lessons.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/** One section of a lesson: a heading and a Markdown body. */
@Embeddable
public class LessonSection {

	@Column(nullable = false, length = 200)
	private String title;

	@Column(nullable = false, columnDefinition = "text")
	private String body;

	protected LessonSection() {
	}

	public LessonSection(String title, String body) {
		this.title = title;
		this.body = body;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

}
