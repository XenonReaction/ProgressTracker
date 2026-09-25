package com.progressiontracker.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Resolves the user a request acts as. v1 is a single-user proof of concept with no
 * authentication, so every request acts as one configured user, created on first use.
 * When real authentication arrives, this is the one place that changes.
 */
@Service
public class CurrentUserService {

	private static final Logger log = LoggerFactory.getLogger(CurrentUserService.class);

	private final UserRepository users;

	private final String username;

	/** Creating the user needs its own read-write transaction: the first request may be a read. */
	private final TransactionTemplate newTransaction;

	public CurrentUserService(UserRepository users,
			@Value("${progressiontracker.default-username}") String username,
			PlatformTransactionManager transactionManager) {
		this.users = users;
		this.username = username;
		this.newTransaction = new TransactionTemplate(transactionManager);
		this.newTransaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	}

	@Transactional
	public User getCurrentUser() {
		return users.findByUsername(username).orElseGet(this::createDefaultUser);
	}

	private User createDefaultUser() {
		try {
			newTransaction.executeWithoutResult(status -> users.save(new User(username)));
			log.info("Created the default user '{}'", username);
		}
		catch (DataIntegrityViolationException createdByAnotherRequest) {
			// Two first requests at once: the other one created it, which is fine
		}
		return users.findByUsername(username).orElseThrow();
	}

}
