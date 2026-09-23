package com.progressiontracker.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Resolves the user a request acts as. v1 is a single-user proof of concept with no
 * authentication, so every request acts as one configured user, created on first use.
 * When real authentication arrives, this is the one place that changes.
 */
@Service
public class CurrentUserService {

	private final UserRepository users;

	private final String username;

	public CurrentUserService(UserRepository users,
			@Value("${progressiontracker.default-username}") String username) {
		this.users = users;
		this.username = username;
	}

	@Transactional
	public User getCurrentUser() {
		return users.findByUsername(username).orElseGet(() -> users.save(new User(username)));
	}

}
