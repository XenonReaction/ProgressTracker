package com.progressiontracker.tree;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.user.CurrentUserService;

/** CRUD for the current user's trees (metadata only; see TreeNodeService for contents). */
@Service
@Transactional
public class TreeService {

	private final TreeRepository trees;

	private final CurrentUserService currentUser;

	public TreeService(TreeRepository trees, CurrentUserService currentUser) {
		this.trees = trees;
		this.currentUser = currentUser;
	}

	@Transactional(readOnly = true)
	public List<TreeResponse> list() {
		return trees.findByOwnerOrderByTitleAsc(currentUser.getCurrentUser()).stream().map(TreeResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public TreeResponse get(Long id) {
		return TreeResponse.from(findOwned(id));
	}

	public TreeResponse create(TreeRequest request) {
		Tree tree = new Tree(currentUser.getCurrentUser(), request.title());
		apply(request, tree);
		return TreeResponse.from(trees.save(tree));
	}

	public TreeResponse update(Long id, TreeRequest request) {
		Tree tree = findOwned(id);
		apply(request, tree);
		trees.flush(); // so the response carries the new updatedAt
		return TreeResponse.from(tree);
	}

	/** Deletes the tree with its tree nodes and edges (database cascade). Library nodes stay. */
	public void delete(Long id) {
		trees.delete(findOwned(id));
	}

	/** Looks up one of the current user's trees, or throws 404. */
	@Transactional(readOnly = true)
	public Tree findOwned(Long id) {
		return trees.findByIdAndOwner(id, currentUser.getCurrentUser())
			.orElseThrow(() -> new NotFoundException("Tree", id));
	}

	private static void apply(TreeRequest request, Tree tree) {
		tree.setTitle(request.title());
		tree.setDescription(request.description());
		tree.setCategory(request.category());
		tree.getTags().clear();
		tree.getTags().addAll(request.tagsOrEmpty());
	}

}
