package com.progressiontracker.coding;

/**
 * Lets another module refuse to have a question set deleted while it still refers to it,
 * without the Coding practice module depending on that module. Implementations throw (such
 * as a {@code ConflictException}) to refuse; every implementation runs before a set is deleted.
 */
public interface QuestionSetDeletionCheck {

	void checkCanDelete(Long setId);

}
