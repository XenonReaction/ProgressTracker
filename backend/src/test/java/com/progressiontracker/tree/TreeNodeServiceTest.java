package com.progressiontracker.tree;

import static com.progressiontracker.TestEntities.withId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.progressiontracker.common.BadRequestException;
import com.progressiontracker.common.ConflictException;
import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.node.Node;
import com.progressiontracker.node.NodeService;
import com.progressiontracker.user.User;

@ExtendWith(MockitoExtension.class)
class TreeNodeServiceTest {

	@Mock
	private TreeNodeRepository treeNodes;

	@Mock
	private PrerequisiteRepository prerequisites;

	@Mock
	private TreeService treeService;

	@Mock
	private NodeService nodeService;

	@InjectMocks
	private TreeNodeService service;

	private final User user = withId(new User("demo"), 1L);

	private final Tree tree = withId(new Tree(user, "Java"), 10L);

	private final Node node = withId(new Node(user, "Generics"), 20L);

	@BeforeEach
	void setUp() {
		when(treeService.findOwned(10L)).thenReturn(tree);
	}

	@Test
	void addUsesDefaultThresholdsWhenOmitted() {
		when(nodeService.findOwned(20L)).thenReturn(node);
		when(treeNodes.save(any(TreeNode.class))).thenAnswer(invocation -> withId(invocation.getArgument(0), 30L));

		TreeNodeResponse response = service.add(10L, new TreeNodeCreateRequest(20L, 5.0, -5.0, null, null));

		assertThat(response.id()).isEqualTo(30L);
		assertThat(response.treeId()).isEqualTo(10L);
		assertThat(response.nodeId()).isEqualTo(20L);
		assertThat(response.title()).isEqualTo("Generics");
		assertThat(response.positionX()).isEqualTo(5.0);
		assertThat(response.positionY()).isEqualTo(-5.0);
		assertThat(response.aggregateThreshold()).isEqualTo(80);
		assertThat(response.individualThreshold()).isEqualTo(70);
		assertThat(response.prerequisiteIds()).isEmpty();
		assertThat(response.dependentIds()).isEmpty();
	}

	@Test
	void addUsesGivenThresholds() {
		when(nodeService.findOwned(20L)).thenReturn(node);
		when(treeNodes.save(any(TreeNode.class))).thenAnswer(invocation -> withId(invocation.getArgument(0), 30L));

		TreeNodeResponse response = service.add(10L, new TreeNodeCreateRequest(20L, 0.0, 0.0, 90, 60));

		assertThat(response.aggregateThreshold()).isEqualTo(90);
		assertThat(response.individualThreshold()).isEqualTo(60);
	}

	@Test
	void addRejectsNodeAlreadyInTree() {
		when(nodeService.findOwned(20L)).thenReturn(node);
		when(treeNodes.existsByTreeAndNode(tree, node)).thenReturn(true);

		assertThatThrownBy(() -> service.add(10L, new TreeNodeCreateRequest(20L, 0.0, 0.0, null, null)))
			.isInstanceOf(ConflictException.class)
			.hasMessage("Node 20 is already in tree 10");
		verify(treeNodes, never()).save(any());
	}

	@Test
	void addRejectsNodeNotInUsersLibrary() {
		when(nodeService.findOwned(99L)).thenThrow(new NotFoundException("Node", 99L));

		assertThatThrownBy(() -> service.add(10L, new TreeNodeCreateRequest(99L, 0.0, 0.0, null, null)))
			.isInstanceOf(NotFoundException.class);
		verify(treeNodes, never()).save(any());
	}

	@Test
	void updatePositionsMovesListedNodesAndReturnsTheWholeTree() {
		TreeNode first = withId(new TreeNode(tree, node, 0, 0), 31L);
		TreeNode second = withId(new TreeNode(tree, withId(new Node(user, "Streams"), 21L), 5, 5), 32L);
		when(treeNodes.findByTreeOrderByIdAsc(tree)).thenReturn(List.of(first, second));

		List<TreeNodeResponse> response = service.updatePositions(10L,
				new TreeLayoutRequest(List.of(new TreeLayoutRequest.Position(31L, 100.0, 200.0))));

		assertThat(first.getPositionX()).isEqualTo(100.0);
		assertThat(first.getPositionY()).isEqualTo(200.0);
		assertThat(second.getPositionX()).isEqualTo(5.0);
		assertThat(response).extracting(TreeNodeResponse::id).containsExactly(31L, 32L);
	}

	@Test
	void updatePositionsChangesNothingWhenAnyIdIsNotInTheTree() {
		TreeNode first = withId(new TreeNode(tree, node, 0, 0), 31L);
		when(treeNodes.findByTreeOrderByIdAsc(tree)).thenReturn(List.of(first));

		assertThatThrownBy(() -> service.updatePositions(10L,
				new TreeLayoutRequest(List.of(new TreeLayoutRequest.Position(31L, 100.0, 100.0),
						new TreeLayoutRequest.Position(99L, 1.0, 1.0)))))
			.isInstanceOf(NotFoundException.class);
		assertThat(first.getPositionX()).isZero();
	}

	@Test
	void updatePositionsRejectsRepeatedIds() {
		assertThatThrownBy(() -> service.updatePositions(10L,
				new TreeLayoutRequest(List.of(new TreeLayoutRequest.Position(31L, 1.0, 1.0),
						new TreeLayoutRequest.Position(31L, 2.0, 2.0)))))
			.isInstanceOf(BadRequestException.class);
	}

	@Test
	void listShowsWhatEachNodeNeedsAndUnlocks() {
		TreeNode first = withId(new TreeNode(tree, withId(new Node(user, "Syntax"), 21L), 0, 0), 31L);
		TreeNode middle = withId(new TreeNode(tree, node, 0, 100), 32L);
		TreeNode last = withId(new TreeNode(tree, withId(new Node(user, "Streams"), 23L), 0, 200), 33L);
		when(treeNodes.findByTreeOrderByIdAsc(tree)).thenReturn(List.of(first, middle, last));
		when(prerequisites.findByTree(tree))
			.thenReturn(List.of(new Prerequisite(first, middle), new Prerequisite(middle, last)));

		List<TreeNodeResponse> response = service.list(10L);

		assertThat(response).extracting(TreeNodeResponse::id).containsExactly(31L, 32L, 33L);
		TreeNodeResponse middleResponse = response.get(1);
		assertThat(middleResponse.prerequisiteIds()).containsExactly(31L);
		assertThat(middleResponse.dependentIds()).containsExactly(33L);
		assertThat(response.get(0).prerequisiteIds()).isEmpty();
		assertThat(response.get(2).dependentIds()).isEmpty();
	}

}
