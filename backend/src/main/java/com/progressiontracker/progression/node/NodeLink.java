package com.progressiontracker.progression.node;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/** An external reference (docs, course, article) attached to a node. */
@Embeddable
public class NodeLink {

	@Column(nullable = false, length = 2048)
	private String url;

	@Column(length = 200)
	private String label;

	protected NodeLink() {
	}

	public NodeLink(String url, String label) {
		this.url = url;
		this.label = label;
	}

	public String getUrl() {
		return url;
	}

	public String getLabel() {
		return label;
	}

}
