package com.progressiontracker.materials;

/**
 * Lets another module refuse to have a material deleted while it still refers to it, without
 * the Materials module depending on that module. Implementations throw (such as a
 * {@code ConflictException}) to refuse; every implementation runs before a material is deleted.
 */
public interface MaterialDeletionCheck {

	void checkCanDelete(Long materialId);

}
