package com.progressiontracker.node;

import static com.progressiontracker.TestEntities.withId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.progressiontracker.common.ConflictException;
import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.readiness.TestReadiness;
import com.progressiontracker.tree.Tree;
import com.progressiontracker.tree.TreeLinks;
import com.progressiontracker.tree.TreeNodeRepository;
import com.progressiontracker.tree.TreeRef;
import com.progressiontracker.tree.TreeRepository;
import com.progressiontracker.user.CurrentUserService;
import com.progressiontracker.user.User;

@ExtendWith(MockitoExtension.class)
class NodeServiceTest {

	@Mock
	private NodeRepository nodes;

	@Mock
	private TreeRepository trees;

	@Mock
	private TreeNodeRepository treeNodes;

	@Mock
	private TreeLinks treeLinks;

	@Mock
	private CurrentUserService currentUser;

	private NodeService service;

	private final User user = withId(new User("demo"), 1L);

	@BeforeEach
	void setUp() {
		lenient().when(currentUser.getCurrentUser()).thenReturn(user);
		service = new NodeService(nodes, trees, treeLinks, TestReadiness.service(treeNodes), currentUser);
	}

	@Test
	void createCopiesFieldsAndDefaultsToManualSource() {
		when(nodes.save(any(Node.class))).thenAnswer(invocation -> withId(invocation.getArgument(0), 7L));

		NodeResponse response = service.create(new NodeRequest("Generics", "Type parameters", 40,
				List.of(new NodeRequest.Link("https://example.com", "Docs")), null, List.of("java", "types")));

		assertThat(response.id()).isEqualTo(7L);
		assertThat(response.title()).isEqualTo("Generics");
		assertThat(response.description()).isEqualTo("Type parameters");
		assertThat(response.readiness()).isEqualTo(40);
		assertThat(response.readinessSourceType()).isEqualTo("manual");
		assertThat(response.linkedTree()).isNull();
		assertThat(response.links()).containsExactly(new NodeResponse.Link("https://example.com", "Docs"));
		assertThat(response.tags()).containsExactly("java", "types");
	}

	@Test
	void updateReplacesFieldsAndLinks() {
		Node node = withId(new Node(user, "Old title"), 7L);
		node.getLinks().add(new NodeLink("https://old.example.com", null));
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));

		NodeResponse response = service.update(7L, new NodeRequest("New title", null, 90, null, null, null));

		assertThat(response.title()).isEqualTo("New title");
		assertThat(response.readiness()).isEqualTo(90);
		assertThat(response.links()).isEmpty();
	}

	@Test
	void getThrowsNotFoundWhenNodeIsMissingOrOwnedBySomeoneElse() {
		when(nodes.findByIdAndOwner(99L, user)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.get(99L)).isInstanceOf(NotFoundException.class)
			.hasMessage("Node 99 not found");
	}

	@Test
	void deleteRefusesWhenNodeIsUsedInTreesAndListsThem() {
		Node node = withId(new Node(user, "OOP"), 7L);
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));
		when(trees.findTreesContaining(node))
			.thenReturn(List.of(withId(new Tree(user, "Java Fundamentals"), 3L), withId(new Tree(user, "Spring"), 4L)));

		assertThatThrownBy(() -> service.delete(7L)).isInstanceOfSatisfying(ConflictException.class, ex -> {
			assertThat(ex.getMessage()).contains("used in 2 tree(s)");
			assertThat(ex.getProperties()).containsEntry("trees",
					List.of(new TreeRef(3L, "Java Fundamentals"), new TreeRef(4L, "Spring")));
		});
		verify(nodes, never()).delete(any());
	}

	@Test
	void linkingTakesReadinessFromTheTreeAndKeepsTheHandEnteredValue() {
		Node node = withId(new Node(user, "Collections"), 7L);
		Tree linked = withId(new Tree(user, "Collections in depth"), 3L);
		Tree containing = withId(new Tree(user, "Java"), 4L);
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));
		when(trees.findByIdAndOwner(3L, user)).thenReturn(Optional.of(linked));
		when(trees.findTreesContaining(node)).thenReturn(List.of(containing));
		when(treeNodes.findNodesInTree(linked))
			.thenReturn(List.of(manual(11L, "List", 80), manual(12L, "Map", 60), manual(13L, "Set", 31)));

		NodeResponse response = service.update(7L, new NodeRequest("Collections", null, 25, null, 3L, null));

		verify(treeLinks).checkNoLoop(node, List.of(containing), linked);
		assertThat(response.readinessSourceType()).isEqualTo("linked_tree");
		assertThat(response.linkedTree()).isEqualTo(new TreeRef(3L, "Collections in depth"));
		assertThat(response.readiness()).isEqualTo(57); // (80 + 60 + 31) / 3 = 57.0
		assertThat(response.manualReadiness()).isEqualTo(25);
	}

	@Test
	void unlinkingGoesBackToTheHandEnteredValue() {
		Node node = withId(new Node(user, "Collections"), 7L);
		node.setReadiness(25);
		node.setLinkedTree(withId(new Tree(user, "Collections in depth"), 3L));
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));

		NodeResponse response = service.update(7L, new NodeRequest("Collections", null, 25, null, null, null));

		assertThat(response.readinessSourceType()).isEqualTo("manual");
		assertThat(response.linkedTree()).isNull();
		assertThat(response.readiness()).isEqualTo(25);
	}

	@Test
	void linkingToAnUnknownTreeIsNotFound() {
		when(trees.findByIdAndOwner(99L, user)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.create(new NodeRequest("Collections", null, 0, null, 99L, null)))
			.isInstanceOf(NotFoundException.class)
			.hasMessage("Tree 99 not found");
		verify(nodes, never()).save(any());
	}

	@Test
	void linkingRefusesALoop() {
		Node node = withId(new Node(user, "Collections"), 7L);
		Tree linked = withId(new Tree(user, "Java"), 3L);
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));
		when(trees.findByIdAndOwner(3L, user)).thenReturn(Optional.of(linked));
		when(trees.findTreesContaining(node)).thenReturn(List.of(linked));
		doThrow(new ConflictException("loop")).when(treeLinks).checkNoLoop(node, List.of(linked), linked);

		assertThatThrownBy(() -> service.update(7L, new NodeRequest("Collections", null, 0, null, 3L, null)))
			.isInstanceOf(ConflictException.class);
	}

	@Test
	void updateReadinessSetsTheHandEnteredValue() {
		Node node = withId(new Node(user, "Generics"), 7L);
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));

		NodeResponse response = service.updateReadiness(7L, new NodeReadinessRequest(65));

		assertThat(response.readiness()).isEqualTo(65);
		assertThat(response.manualReadiness()).isEqualTo(65);
	}

	@Test
	void updateReadinessRefusesALinkedNode() {
		Node node = withId(new Node(user, "Collections"), 7L);
		node.setLinkedTree(withId(new Tree(user, "Collections in Depth"), 3L));
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));

		assertThatThrownBy(() -> service.updateReadiness(7L, new NodeReadinessRequest(65)))
			.isInstanceOf(ConflictException.class)
			.hasMessageContaining("takes its readiness from the tree \"Collections in Depth\"");
		assertThat(node.getReadiness()).isZero();
	}

	@Test
	void deleteRemovesUnusedNode() {
		Node node = withId(new Node(user, "Maven"), 7L);
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));
		when(trees.findTreesContaining(node)).thenReturn(List.of());

		service.delete(7L);

		verify(nodes).delete(node);
	}

	private Node manual(Long id, String title, int readiness) {
		Node node = withId(new Node(user, title), id);
		node.setReadiness(readiness);
		return node;
	}

}
