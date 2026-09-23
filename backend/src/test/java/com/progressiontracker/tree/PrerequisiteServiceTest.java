package com.progressiontracker.tree;

import static com.progressiontracker.TestEntities.withId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.progressiontracker.common.BadRequestException;
import com.progressiontracker.common.ConflictException;
import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.node.Node;
import com.progressiontracker.user.User;

@ExtendWith(MockitoExtension.class)
class PrerequisiteServiceTest {

	@Mock
	private PrerequisiteRepository prerequisites;

	@Mock
	private TreeService treeService;

	@Mock
	private TreeNodeService treeNodeService;

	@InjectMocks
	private PrerequisiteService service;

	private final User user = withId(new User("demo"), 1L);

	private final Tree tree = withId(new Tree(user, "Java"), 10L);

	private final TreeNode a = treeNode(31L);

	private final TreeNode b = treeNode(32L);

	private final TreeNode c = treeNode(33L);

	@Test
	void rejectsSelfEdgeBeforeTouchingTheDatabase() {
		assertThatThrownBy(() -> service.create(10L, new PrerequisiteRequest(31L, 31L)))
			.isInstanceOf(BadRequestException.class);
		verifyNoInteractions(treeService, treeNodeService, prerequisites);
	}

	@Test
	void rejectsTreeNodeFromAnotherTree() {
		when(treeService.findOwned(10L)).thenReturn(tree);
		when(treeNodeService.findInTree(tree, 31L)).thenReturn(a);
		when(treeNodeService.findInTree(tree, 99L)).thenThrow(new NotFoundException("Tree node", 99L));

		assertThatThrownBy(() -> service.create(10L, new PrerequisiteRequest(31L, 99L)))
			.isInstanceOf(NotFoundException.class);
		verify(prerequisites, never()).save(any());
	}

	@Test
	void rejectsDuplicateEdge() {
		givenTreeNodes();
		when(prerequisites.existsByPrerequisiteAndDependent(a, b)).thenReturn(true);

		assertThatThrownBy(() -> service.create(10L, new PrerequisiteRequest(31L, 32L)))
			.isInstanceOf(ConflictException.class);
		verify(prerequisites, never()).save(any());
	}

	@Test
	void rejectsEdgeThatWouldCreateCycle() {
		givenTreeNodes();
		when(prerequisites.findByTree(tree)).thenReturn(List.of(new Prerequisite(a, b), new Prerequisite(b, c)));

		assertThatThrownBy(() -> service.create(10L, new PrerequisiteRequest(33L, 31L)))
			.isInstanceOf(ConflictException.class)
			.hasMessageContaining("would create a cycle");
		verify(prerequisites, never()).save(any());
	}

	@Test
	void savesValidEdge() {
		givenTreeNodes();
		when(prerequisites.findByTree(tree)).thenReturn(List.of(new Prerequisite(a, b)));
		when(prerequisites.save(any(Prerequisite.class)))
			.thenAnswer(invocation -> withId(invocation.getArgument(0), 40L));

		PrerequisiteResponse response = service.create(10L, new PrerequisiteRequest(31L, 33L));

		assertThat(response).isEqualTo(new PrerequisiteResponse(40L, 31L, 33L));
	}

	private void givenTreeNodes() {
		when(treeService.findOwned(10L)).thenReturn(tree);
		for (TreeNode treeNode : List.of(a, b, c)) {
			lenient().when(treeNodeService.findInTree(tree, treeNode.getId())).thenReturn(treeNode);
		}
	}

	private TreeNode treeNode(long id) {
		return withId(new TreeNode(tree, withId(new Node(user, "Node " + id), id), 0, 0), id);
	}

}
