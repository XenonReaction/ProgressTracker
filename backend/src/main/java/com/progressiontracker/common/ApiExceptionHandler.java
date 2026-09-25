package com.progressiontracker.common;

import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Turns exceptions into RFC 9457 problem responses ({@code application/problem+json}).
 * Spring's own errors (malformed JSON, unsupported method, ...) are handled by the base
 * class; this adds the domain exceptions and field-level validation details.
 * <p>
 * Logging (Phase 6.2): expected refusals (404, 400, 409) are normal, so they're logged at
 * DEBUG only. Anything unexpected is logged at ERROR with its stack trace, and the client
 * gets a plain 500 with the request id to quote, never the internal details.
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@ExceptionHandler(NotFoundException.class)
	ProblemDetail handleNotFound(NotFoundException ex) {
		log.debug("Not found: {}", ex.getMessage());
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(BadRequestException.class)
	ProblemDetail handleBadRequest(BadRequestException ex) {
		log.debug("Bad request: {}", ex.getMessage());
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(ConflictException.class)
	ProblemDetail handleConflict(ConflictException ex) {
		log.debug("Conflict: {}", ex.getMessage());
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
		ex.getProperties().forEach(problem::setProperty);
		return problem;
	}

	/** Backstop for database constraints that a service-level check didn't catch first. */
	@ExceptionHandler(DataIntegrityViolationException.class)
	ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		log.warn("Database constraint violation", ex);
		return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "The change conflicts with existing data");
	}

	/** Anything not handled above: a bug or an outage, so it's logged in full. */
	@ExceptionHandler(Exception.class)
	ProblemDetail handleUnexpected(Exception ex) {
		log.error("Unexpected error", ex);
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
				"Something went wrong on the server");
		String requestId = MDC.get(RequestLoggingFilter.MDC_KEY);
		if (requestId != null) {
			problem.setProperty("requestId", requestId);
		}
		return problem;
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.debug("Validation failed: {} field error(s)", ex.getBindingResult().getFieldErrorCount());
		ProblemDetail problem = ex.getBody();
		problem.setDetail("Request validation failed");
		problem.setProperty("errors",
				ex.getBindingResult()
					.getFieldErrors()
					.stream()
					.map(error -> Map.of("field", error.getField(), "message",
							Objects.requireNonNullElse(error.getDefaultMessage(), "is invalid")))
					.toList());
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

}
