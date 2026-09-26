package com.progressiontracker.coding.internal;

import java.time.Instant;

import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * A written coding problem: a problem statement and worked examples (Markdown), and a
 * solution kept hidden until the user asks to see it.
 */
@Entity
@Table(name = "coding_questions", check = @CheckConstraint(name = "coding_questions_language_check",
		constraint = "language in ('html', 'css')"))
public class CodingQuestion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "set_id", nullable = false, foreignKey = @ForeignKey(name = "coding_questions_set_id_fk"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private QuestionSet set;

	@Column(nullable = false, length = 200)
	private String title;

	@Column(nullable = false, length = 20)
	private CodingLanguage language;

	@Column(nullable = false, columnDefinition = "text")
	private String problem;

	@Column(columnDefinition = "text")
	private String examples;

	@Column(nullable = false, columnDefinition = "text")
	private String solution;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private Instant updatedAt;

	protected CodingQuestion() {
	}

	public CodingQuestion(QuestionSet set, String title, CodingLanguage language, String problem, String solution) {
		this.set = set;
		this.title = title;
		this.language = language;
		this.problem = problem;
		this.solution = solution;
	}

	public Long getId() {
		return id;
	}

	public QuestionSet getSet() {
		return set;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public CodingLanguage getLanguage() {
		return language;
	}

	public void setLanguage(CodingLanguage language) {
		this.language = language;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getExamples() {
		return examples;
	}

	public void setExamples(String examples) {
		this.examples = examples;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

}
