package com.progressiontracker.progression.node;

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

import com.progressiontracker.common.BadRequestException;
import com.progressiontracker.common.ConflictException;
import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.progression.readiness.TestReadiness;
import com.progressiontracker.progression.tree.Tree;
import com.progressiontracker.progression.tree.TreeLinks;
import com.progressiontracker.progression.tree.TreeNodeRepository;
import com.progressiontracker.progression.tree.TreeRef;
import com.progressiontracker.progression.tree.TreeRepository;
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
	void createCopiesFieldsAndUsesTheHandEnteredValueWhenNothingCounts() {
		when(nodes.save(any(Node.class))).thenAnswer(invocation -> withId(invocation.getArgument(0), 7L));

		NodeResponse response = service.create(new NodeRequest("Generics", "Type parameters", 40,
				List.of(url("https://example.com", "Docs")), List.of("java", "types")));

		assertThat(response.id()).isEqualTo(7L);
		assertThat(response.title()).isEqualTo("Generics");
		assertThat(response.description()).isEqualTo("Type parameters");
		assertThat(response.readiness()).isEqualTo(40);
		assertThat(response.resources())
			.containsExactly(new NodeResponse.Resource("url", "https://example.com", null, "Docs", false));
		assertThat(response.tags()).containsExactly("java", "types");
	}

	@Test
	void updateReplacesFieldsAndResources() {
		Node node = withId(new Node(user, "Old title"), 7L);
		node.getResources().add(NodeResource.url("https://old.example.com", null));
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));

		NodeResponse response = service.update(7L, new NodeRequest("New title", null, 90, null, null));

		assertThat(response.title()).isEqualTo("New title");
		assertThat(response.readiness()).isEqualTo(90);
		assertThat(response.resources()).isEmpty();
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
	void aCountingTreeGivesTheNodeItsReadinessAndTheHandEnteredValueIsKept() {
		Node node = withId(new Node(user, "Collections"), 7L);
		Tree linked = withId(new Tree(user, "Collections in depth"), 3L);
		Tree containing = withId(new Tree(user, "Java"), 4L);
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));
		when(trees.findByIdAndOwner(3L, user)).thenReturn(Optional.of(linked));
		when(trees.findTreesContaining(node)).thenReturn(List.of(containing));
		when(treeNodes.findNodesInTree(linked))
			.thenReturn(List.of(manual(11L, "List", 80), manual(12L, "Map", 60), manual(13L, "Set", 31)));

		NodeResponse response = service.update(7L,
				new NodeRequest("Collections", null, 25, List.of(tree(3L, null, true)), null));

		verify(treeLinks).checkNoLoop(node, List.of(containing), linked);
		assertThat(response.resources()).containsExactly(
				new NodeResponse.Resource("tree", null, new TreeRef(3L, "Collections in depth"), null, true));
		assertThat(response.readiness()).isEqualTo(57); // (80 + 60 + 31) / 3 = 57.0
		assertThat(response.manualReadiness()).isEqualTo(25);
	}

	@Test
	void severalCountingTreesAreAveragedAndATreeThatDoesNotCountIsIgnored() {
		Node node = withId(new Node(user, "Front-end Basics"), 7L);
		Tree css = withId(new Tree(user, "CSS"), 3L);
		Tree html = withId(new Tree(user, "HTML"), 4L);
		Tree reading = withId(new Tree(user, "Further reading"), 5L);
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));
		when(trees.findByIdAndOwner(3L, user)).thenReturn(Optional.of(css));
		when(trees.findByIdAndOwner(4L, user)).thenReturn(Optional.of(html));
		when(trees.findByIdAndOwner(5L, user)).thenReturn(Optional.of(reading));
		when(trees.findTreesContaining(node)).thenReturn(List.of());
		when(treeNodes.findNodesInTree(css)).thenReturn(List.of(manual(11L, "Selectors", 54)));
		when(treeNodes.findNodesInTree(html)).thenReturn(List.of(manual(12L, "Forms", 100)));

		NodeResponse response = service.update(7L, new NodeRequest("Front-end Basics", null, 10,
				List.of(tree(3L, "Styling", true), url("https://example.com", null), tree(4L, null, true),
						tree(5L, null, false)),
				null));

		// (54 + 100) / 2 = 77; the reference-only tree isn't read or loop-checked
		assertThat(response.readiness()).isEqualTo(77);
		assertThat(response.resources()).extracting(NodeResponse.Resource::type)
			.containsExactly("tree", "url", "tree", "tree");
		assertThat(response.resources().get(0).label()).isEqualTo("Styling");
		verify(treeLinks).checkNoLoop(node, List.of(), css);
		verify(treeLinks).checkNoLoop(node, List.of(), html);
		verify(treeLinks, never()).checkNoLoop(node, List.of(), reading);
		verify(treeNodes, never()).findNodesInTree(reading);
	}

	@Test
	void removingTheLastCountingResourceGoesBackToTheHandEnteredValue() {
		Node node = withId(new Node(user, "Collections"), 7L);
		node.setReadiness(25);
		node.getResources().add(NodeResource.tree(withId(new Tree(user, "Collections in depth"), 3L), null, true));
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));

		NodeResponse response = service.update(7L, new NodeRequest("Collections", null, 25, null, null));

		assertThat(response.resources()).isEmpty();
		assertThat(response.readiness()).isEqualTo(25);
	}

	@Test
	void aTreeThatIsNotTheUsersIsNotFound() {
		when(trees.findByIdAndOwner(99L, user)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.create(
				new NodeRequest("Collections", null, 0, List.of(tree(99L, null, true)), null)))
			.isInstanceOf(NotFoundException.class)
			.hasMessage("Tree 99 not found");
		verify(nodes, never()).save(any());
	}

	@Test
	void refusesResourcesThatDoNotFitTheirType() {
		Tree css = withId(new Tree(user, "CSS"), 3L);
		lenient().when(trees.findByIdAndOwner(3L, user)).thenReturn(Optional.of(css));

		assertThatThrownBy(() -> create(new NodeRequest.Resource("url", "https://example.com", null, null, true)))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("can't count toward readiness");
		assertThatThrownBy(() -> create(new NodeRequest.Resource("url", null, null, null, false)))
			.isInstanceOf(BadRequestException.class);
		assertThatThrownBy(() -> create(new NodeRequest.Resource("tree", "https://example.com", 3L, null, true)))
			.isInstanceOf(BadRequestException.class);
		assertThatThrownBy(() -> create(new NodeRequest.Resource("deck", null, 3L, null, true)))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("Unknown resource type");
		assertThatThrownBy(() -> service.create(new NodeRequest("Twice", null, 0,
				List.of(tree(3L, null, true), tree(3L, "Again", false)), null)))
			.isInstanceOf(BadRequestException.class)
			.hasMessage("Tree 3 is listed more than once");
		verify(nodes, never()).save(any());
	}

	@Test
	void countingATreeRefusesALoop() {
		Node node = withId(new Node(user, "Collections"), 7L);
		Tree linked = withId(new Tree(user, "Java"), 3L);
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));
		when(trees.findByIdAndOwner(3L, user)).thenReturn(Optional.of(linked));
		when(trees.findTreesContaining(node)).thenReturn(List.of(linked));
		doThrow(new ConflictException("loop")).when(treeLinks).checkNoLoop(node, List.of(linked), linked);

		assertThatThrownBy(() -> service.update(7L,
				new NodeRequest("Collections", null, 0, List.of(tree(3L, null, true)), null)))
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
	void updateReadinessRefusesANodeWithACountingResource() {
		Node node = withId(new Node(user, "Collections"), 7L);
		node.getResources().add(NodeResource.tree(withId(new Tree(user, "Collections in Depth"), 3L), null, true));
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));

		assertThatThrownBy(() -> service.updateReadiness(7L, new NodeReadinessRequest(65)))
			.isInstanceOf(ConflictException.class)
			.hasMessageContaining("takes its readiness from its resources");
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

	private NodeResponse create(NodeRequest.Resource resource) {
		return service.create(new NodeRequest("Node", null, 0, List.of(resource), null));
	}

	private static NodeRequest.Resource url(String url, String label) {
		return new NodeRequest.Resource("url", url, null, label, false);
	}

	private static NodeRequest.Resource tree(Long treeId, String label, boolean counts) {
		return new NodeRequest.Resource("tree", null, treeId, label, counts);
	}

	private Node manual(Long id, String title, int readiness) {
		Node node = withId(new Node(user, title), id);
		node.setReadiness(readiness);
		return node;
	}

}
