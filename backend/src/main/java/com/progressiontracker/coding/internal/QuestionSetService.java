package com.progressiontracker.coding.internal;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.coding.QuestionSetDeletionCheck;
import com.progressiontracker.coding.QuestionSetReadinessCalculator;
import com.progressiontracker.coding.SetProgress;
import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.user.CurrentUserService;

/**
 * CRUD for the current user's question sets. Deleting a set deletes its questions and the
 * attempts on them, and is refused while a node lists it.
 */
@Service
@Transactional
public class QuestionSetService {

	private static final Logger log = LoggerFactory.getLogger(QuestionSetService.class);

	private final QuestionSetRepository sets;

	private final QuestionSetReadinessCalculator readiness;

	private final CurrentUserService currentUser;

	private final List<QuestionSetDeletionCheck> deletionChecks;

	public QuestionSetService(QuestionSetRepository sets, QuestionSetReadinessCalculator readiness,
			CurrentUserService currentUser, List<QuestionSetDeletionCheck> deletionChecks) {
		this.sets = sets;
		this.readiness = readiness;
		this.currentUser = currentUser;
		this.deletionChecks = deletionChecks;
	}

	@Transactional(readOnly = true)
	public List<QuestionSetResponse> list() {
		List<QuestionSet> owned = sets.findByOwnerOrderByTitleAscIdAsc(currentUser.getCurrentUser());
		Map<Long, SetProgress> progress = readiness.sets(owned);
		return owned.stream().map(set -> QuestionSetResponse.from(set, progress.get(set.getId()))).toList();
	}

	@Transactional(readOnly = true)
	public QuestionSetResponse get(Long id) {
		return toResponse(findOwned(id));
	}

	public QuestionSetResponse create(QuestionSetRequest request) {
		QuestionSet set = new QuestionSet(currentUser.getCurrentUser(), request.title());
		set.setDescription(request.description());
		return toResponse(sets.save(set));
	}

	public QuestionSetResponse update(Long id, QuestionSetRequest request) {
		QuestionSet set = findOwned(id);
		set.setTitle(request.title());
		set.setDescription(request.description());
		sets.flush(); // so the response carries the new updatedAt
		return toResponse(set);
	}

	/** Refused (by a {@link QuestionSetDeletionCheck}) while another module still refers to the set. */
	public void delete(Long id) {
		QuestionSet set = findOwned(id);
		deletionChecks.forEach(check -> check.checkCanDelete(id));
		sets.delete(set);
		log.info("Deleted question set {}", id);
	}

	/** Looks up one of the current user's question sets, or throws 404. */
	@Transactional(readOnly = true)
	public QuestionSet findOwned(Long id) {
		return sets.findByIdAndOwner(id, currentUser.getCurrentUser())
			.orElseThrow(() -> new NotFoundException("Question set", id));
	}

	private QuestionSetResponse toResponse(QuestionSet set) {
		return QuestionSetResponse.from(set, readiness.set(set));
	}

}
