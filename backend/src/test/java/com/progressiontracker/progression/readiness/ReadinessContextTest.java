package com.progressiontracker.progression.readiness;

import static com.progressiontracker.TestEntities.withId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.progressiontracker.flashcards.DeckProgress;
import com.progressiontracker.flashcards.DeckSummary;
import com.progressiontracker.flashcards.FlashcardReadinessCalculator;
import com.progressiontracker.progression.node.Node;
import com.progressiontracker.progression.node.NodeResource;
import com.progressiontracker.progression.tree.Tree;
import com.progressiontracker.progression.tree.TreeNodeRepository;
import com.progressiontracker.user.User;

@ExtendWith(MockitoExtension.class)
class ReadinessContextTest {

	@Mock
	private TreeNodeRepository treeNodes;

	@Mock
	private FlashcardReadinessCalculator flashcards;

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
	void severalCountingResourcesAreAveragedAndOnesThatDoNotCountAreIgnored() {
		Node node = manual("Front-end Basics", 10);
		node.getResources().add(NodeResource.tree(tree("CSS", manual("Selectors", 54)), null, true));
		node.getResources().add(NodeResource.url("https://example.com", null));
		node.getResources().add(NodeResource.tree(withId(new Tree(user, "Reading"), nextId++), null, false));
		node.getResources().add(NodeResource.tree(tree("HTML", manual("Forms", 100)), null, true));

		// (54 + 100) / 2 = 77
		assertThat(context.of(node)).isEqualTo(77);
	}

	@Test
	void nodeWithOnlyResourcesThatDoNotCountUsesItsHandEnteredValue() {
		Node node = manual("Syntax", 95);
		node.getResources().add(NodeResource.url("https://example.com", null));
		node.getResources().add(NodeResource.tree(withId(new Tree(user, "Reading"), nextId++), null, false));

		assertThat(context.of(node)).isEqualTo(95);
	}

	@Test
	void lastReviewedIsTheLatestReviewBeneathTheResourcesThatCount() {
		Instant earlier = Instant.parse("2026-09-01T10:00:00Z");
		Instant later = Instant.parse("2026-09-15T10:00:00Z");
		ReadinessContext withDecks = TestReadiness.service(treeNodes, flashcards).context();
		when(flashcards.deck(40L))
			.thenReturn(Optional.of(new DeckSummary(40L, "Early", new DeckProgress(1, 1, 100, true, earlier, 0))));
		// Never asked for: it doesn't count
		lenient().when(flashcards.deck(41L))
			.thenReturn(Optional.of(new DeckSummary(41L, "Late", new DeckProgress(2, 0, 0, false, later, 0))));
		Node studied = manual("Studied", 0);
		studied.getResources().add(NodeResource.deck(40L, null, true));
		studied.getResources().add(NodeResource.deck(41L, null, false));
		Tree tree = tree("Tree", studied, manual("Untouched", 20));
		Node above = manual("Above", 0);
		above.getResources().add(NodeResource.tree(tree, null, true));

		// The deck that doesn't count isn't beneath the node's readiness, so its later review is ignored
		assertThat(withDecks.lastReviewed(studied)).isEqualTo(earlier);
		assertThat(withDecks.of(studied)).isEqualTo(100);
		assertThat(withDecks.lastReviewedOfTree(tree)).isEqualTo(earlier);
		assertThat(withDecks.ofTree(tree)).isEqualTo(60);
		assertThat(withDecks.lastReviewed(above)).isEqualTo(earlier);
		assertThat(withDecks.lastReviewed(manual("Hand-entered", 50))).isNull();
		// Each deck is read once per context
		withDecks.of(studied);
		verify(flashcards, times(1)).deck(40L);
	}

	@Test
	void serviceRefusesToStartWithoutACalculatorForEveryTypeThatCanCount() {
		assertThatThrownBy(() -> new ReadinessService(List.of(), treeNodes)).isInstanceOf(IllegalStateException.class)
			.hasMessage("No readiness calculator for TREE");
	}

	private Node manual(String title, int readiness) {
		Node node = withId(new Node(user, title), nextId++);
		node.setReadiness(readiness);
		return node;
	}

	private Node linked(String title, Tree tree, int handEntered) {
		Node node = manual(title, handEntered);
		node.getResources().add(NodeResource.tree(tree, null, true));
		return node;
	}

	private Tree tree(String title, Node... nodes) {
		Tree tree = withId(new Tree(user, title), nextId++);
		when(treeNodes.findNodesInTree(tree)).thenReturn(List.of(nodes));
		return tree;
	}

}
