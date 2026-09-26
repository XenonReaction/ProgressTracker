package com.progressiontracker.progression.tree;

import static com.progressiontracker.TestEntities.withId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.progressiontracker.common.ConflictException;
import com.progressiontracker.progression.node.Node;
import com.progressiontracker.user.User;

@ExtendWith(MockitoExtension.class)
class TreeLinksTest {

	@Mock
	private TreeNodeRepository treeNodes;

	private TreeLinks treeLinks;

	private final User user = withId(new User("demo"), 1L);

	private final Tree java = withId(new Tree(user, "Java"), 1L);

	private final Tree collections = withId(new Tree(user, "Collections"), 2L);

	private final Tree concurrency = withId(new Tree(user, "Concurrency"), 3L);

	private final Node node = withId(new Node(user, "Collections node"), 10L);

	@BeforeEach
	void setUp() {
		treeLinks = new TreeLinks(treeNodes);
	}

	/** Tree id → the trees its nodes link to. */
	private void links(Map<Long, List<Long>> links) {
		lenient().when(treeNodes.findLinkedTreeIds(anyLong()))
			.thenAnswer(invocation -> links.getOrDefault(invocation.<Long>getArgument(0), List.of()));
	}

	@Test
	void allowsLinkingToATreeThatDoesNotLeadBack() {
		links(Map.of(2L, List.of(3L)));

		assertThatNoException().isThrownBy(() -> treeLinks.checkNoLoop(node, List.of(java), collections));
	}

	@Test
	void refusesLinkingANodeToATreeItIsIn() {
		links(Map.of());

		assertThatThrownBy(() -> treeLinks.checkNoLoop(node, List.of(java), java))
			.isInstanceOf(ConflictException.class)
			.hasMessageContaining("because it's in that tree");
	}

	@Test
	void refusesALinkThatLeadsBackThroughNestedTrees() {
		// Collections links Concurrency, which links back to Java
		links(Map.of(2L, List.of(3L), 3L, List.of(1L)));

		assertThatThrownBy(() -> treeLinks.checkNoLoop(node, List.of(java), collections))
			.isInstanceOf(ConflictException.class)
			.hasMessageContaining("lead back to \"Java\"");
	}

	@Test
	void checksEveryTreeTheNodeIsIn() {
		links(Map.of(2L, List.of(3L)));

		assertThatThrownBy(() -> treeLinks.checkNoLoop(node, List.of(java, concurrency), collections))
			.isInstanceOf(ConflictException.class)
			.hasMessageContaining("lead back to \"Concurrency\"");
	}

	@Test
	void reachableFromStopsAtTreesAlreadySeen() {
		links(Map.of(1L, List.of(2L), 2L, List.of(1L, 3L)));

		assertThat(treeLinks.reachableFrom(1L)).containsExactly(1L, 2L, 3L);
	}

}
