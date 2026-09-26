package com.progressiontracker.progression.readiness;

import static com.progressiontracker.TestEntities.withId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.progressiontracker.progression.node.Node;
import com.progressiontracker.progression.tree.Tree;
import com.progressiontracker.progression.tree.TreeNodeRepository;
import com.progressiontracker.user.User;

@ExtendWith(MockitoExtension.class)
class ReadinessContextTest {

	@Mock
	private TreeNodeRepository treeNodes;

	private ReadinessContext context;

	private final User user = withId(new User("demo"), 1L);

	private long nextId = 100;

	@BeforeEach
	void setUp() {
		context = TestReadiness.service(treeNodes).context();
	}

	@Test
	void manualNodeUsesItsHandEnteredValue() {
		assertThat(context.of(manual("Syntax", 95))).isEqualTo(95);
	}

	@Test
	void linkedNodeAveragesItsTreeAndRoundsToAWholePercent() {
		Tree collections = tree("Collections", manual("List", 80), manual("Map", 60), manual("Set", 30));

		// (80 + 60 + 30) / 3 = 56.67
		assertThat(context.of(linked("Collections", collections, 10))).isEqualTo(57);
	}

	@Test
	void readinessFlowsUpThroughNestedLinks() {
		Tree concurrency = tree("Concurrency", manual("Threads", 20), manual("Locks", 40));
		Tree collections = tree("Collections", manual("List", 80), manual("Map", 60),
				linked("Concurrent collections", concurrency, 99));

		// The nested node counts as 30 (its tree's average), not its hidden 99
		assertThat(context.of(linked("Collections", collections, 0))).isEqualTo(57);
	}

	@Test
	void nodeLinkedToAnEmptyTreeIsZero() {
		assertThat(context.of(linked("Nothing yet", tree("Empty"), 50))).isZero();
	}

	@Test
	void eachTreeIsAveragedOnlyOncePerContext() {
		Tree shared = tree("Shared", manual("A", 40));

		context.of(linked("First", shared, 0));
		context.of(linked("Second", shared, 0));

		verify(treeNodes, times(1)).findNodesInTree(shared);
	}

	@Test
	void aLoopInTheDataCountsAsZeroInsteadOfRecursingForever() {
		Tree a = withId(new Tree(user, "A"), nextId++);
		Tree b = tree("B", linked("Back to A", a, 0));
		when(treeNodes.findNodesInTree(a)).thenReturn(List.of(linked("To B", b, 0)));

		assertThat(context.ofTree(a)).isZero();
	}

	@Test
	void serviceRefusesToStartWithoutACalculatorForEverySourceType() {
		assertThatThrownBy(() -> new ReadinessService(List.of(new ManualReadinessCalculator()), treeNodes))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("No readiness calculator for LINKED_TREE");
	}

	private Node manual(String title, int readiness) {
		Node node = withId(new Node(user, title), nextId++);
		node.setReadiness(readiness);
		return node;
	}

	private Node linked(String title, Tree tree, int handEntered) {
		Node node = manual(title, handEntered);
		node.setLinkedTree(tree);
		return node;
	}

	private Tree tree(String title, Node... nodes) {
		Tree tree = withId(new Tree(user, title), nextId++);
		when(treeNodes.findNodesInTree(tree)).thenReturn(List.of(nodes));
		return tree;
	}

}
