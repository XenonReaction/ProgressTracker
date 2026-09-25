package com.progressiontracker.common;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Gives every request an id and logs one line when it finishes (Phase 6.2): method, path,
 * status and duration. The id is in the logging MDC as {@code requestId}, so every line
 * logged during the request carries it, and it's returned in the {@code X-Request-Id} header
 * so a problem seen in the browser can be matched to its log lines. A caller's own
 * {@code X-Request-Id} is kept if it looks like an id. Health checks aren't logged, since
 * Docker calls them every few seconds.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

	public static final String HEADER = "X-Request-Id";

	public static final String MDC_KEY = "requestId";

	private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

	/** Only simple ids are taken from callers, so nothing odd ends up in the logs. */
	private static final Pattern ACCEPTED_ID = Pattern.compile("[A-Za-z0-9-]{1,64}");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String requestId = requestIdFor(request);
		MDC.put(MDC_KEY, requestId);
		response.setHeader(HEADER, requestId);
		long start = System.nanoTime();
		try {
			chain.doFilter(request, response);
		}
		finally {
			if (!request.getRequestURI().startsWith("/actuator/")) {
				log.info("{} {} {} {}ms", request.getMethod(), request.getRequestURI(), response.getStatus(),
						(System.nanoTime() - start) / 1_000_000);
			}
			MDC.remove(MDC_KEY);
		}
	}

	private static String requestIdFor(HttpServletRequest request) {
		String given = request.getHeader(HEADER);
		if (given != null && ACCEPTED_ID.matcher(given).matches()) {
			return given;
		}
		return UUID.randomUUID().toString().substring(0, 8);
	}

}
