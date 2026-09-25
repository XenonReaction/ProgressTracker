package com.progressiontracker.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.MDC;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.ProblemDetail;

@ExtendWith(OutputCaptureExtension.class)
class ApiExceptionHandlerTest {

	private final ApiExceptionHandler handler = new ApiExceptionHandler();

	@AfterEach
	void clearRequestId() {
		MDC.clear();
	}

	@Test
	void logsAnUnexpectedErrorInFullButTellsTheClientOnlyTheRequestId(CapturedOutput output) {
		MDC.put(RequestLoggingFilter.MDC_KEY, "abc123");

		ProblemDetail problem = handler.handleUnexpected(new IllegalStateException("database exploded"));

		assertThat(problem.getStatus()).isEqualTo(500);
		assertThat(problem.getDetail()).isEqualTo("Something went wrong on the server");
		assertThat(problem.getProperties()).containsEntry("requestId", "abc123");
		assertThat(output).contains("ERROR").contains("Unexpected error").contains("database exploded");
	}

	@Test
	void logsExpectedRefusalsOnlyAtDebug(CapturedOutput output) {
		handler.handleNotFound(new NotFoundException("Tree", 99L));
		handler.handleConflict(new ConflictException("would create a cycle"));

		assertThat(output).doesNotContain("Tree 99 not found").doesNotContain("would create a cycle");
	}

}
