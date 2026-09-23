package com.progressiontracker.node;

import static com.progressiontracker.TestEntities.withId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.progressiontracker.common.ConflictException;
import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.tree.Tree;
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
	private CurrentUserService currentUser;

	@InjectMocks
	private NodeService service;

	private final User user = withId(new User("demo"), 1L);

	@BeforeEach
	void setUp() {
		lenient().when(currentUser.getCurrentUser()).thenReturn(user);
	}

	@Test
	void createCopiesFieldsAndDefaultsToManualSource() {
		when(nodes.save(any(Node.class))).thenAnswer(invocation -> withId(invocation.getArgument(0), 7L));

		NodeResponse response = service.create(new NodeRequest("Generics", "Type parameters", 40,
				List.of(new NodeRequest.Link("https://example.com", "Docs"))));

		assertThat(response.id()).isEqualTo(7L);
		assertThat(response.title()).isEqualTo("Generics");
		assertThat(response.description()).isEqualTo("Type parameters");
		assertThat(response.readiness()).isEqualTo(40);
		assertThat(response.readinessSourceType()).isEqualTo("manual");
		assertThat(response.links()).containsExactly(new NodeResponse.Link("https://example.com", "Docs"));
	}

	@Test
	void updateReplacesFieldsAndLinks() {
		Node node = withId(new Node(user, "Old title"), 7L);
		node.getLinks().add(new NodeLink("https://old.example.com", null));
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));

		NodeResponse response = service.update(7L, new NodeRequest("New title", null, 90, null));

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
			assertThat(ex.getProperties()).containsEntry("trees", List
				.of(new NodeService.TreeRef(3L, "Java Fundamentals"), new NodeService.TreeRef(4L, "Spring")));
		});
		verify(nodes, never()).delete(any());
	}

	@Test
	void deleteRemovesUnusedNode() {
		Node node = withId(new Node(user, "Maven"), 7L);
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));
		when(trees.findTreesContaining(node)).thenReturn(List.of());

		service.delete(7L);

		verify(nodes).delete(node);
	}

}
