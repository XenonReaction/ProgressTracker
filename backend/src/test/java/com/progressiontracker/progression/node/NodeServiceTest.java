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

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.progressiontracker.coding.QuestionSetReadinessCalculator;
import com.progressiontracker.coding.QuestionSetSummary;
import com.progressiontracker.coding.SetProgress;
import com.progressiontracker.common.BadRequestException;
import com.progressiontracker.common.ConflictException;
import com.progressiontracker.common.NotFoundException;
import com.progressiontracker.flashcards.DeckProgress;
import com.progressiontracker.flashcards.DeckSummary;
import com.progressiontracker.flashcards.FlashcardReadinessCalculator;
import com.progressiontracker.lessons.LessonReadinessCalculator;
import com.progressiontracker.lessons.LessonSummary;
import com.progressiontracker.materials.MaterialReadinessCalculator;
import com.progressiontracker.materials.MaterialSummary;
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

	@Mock
	private FlashcardReadinessCalculator flashcards;

	@Mock
	private MaterialReadinessCalculator materials;

	@Mock
	private LessonReadinessCalculator lessons;

	@Mock
	private QuestionSetReadinessCalculator questionSets;

	private NodeService service;

	private final User user = withId(new User("demo"), 1L);

	@BeforeEach
	void setUp() {
		lenient().when(currentUser.getCurrentUser()).thenReturn(user);
		service = new NodeService(nodes, trees, treeLinks,
				TestReadiness.service(treeNodes, flashcards, materials, lessons, questionSets), currentUser, flashcards,
				materials, lessons, questionSets);
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
			.containsExactly(
					new NodeResponse.Resource("url", "https://example.com", null, null, null, null, null, "Docs", false, null,
					null, false));
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
				new NodeResponse.Resource("tree", null, new TreeRef(3L, "Collections in depth"), null, null, null, null, null,
						true, 57, null, false));
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

		// (54 + 100) / 2 = 77; the reference-only tree isn't counted or loop-checked, though its
		// own readiness is shown
		assertThat(response.readiness()).isEqualTo(77);
		assertThat(response.resources()).extracting(NodeResponse.Resource::type)
			.containsExactly("tree", "url", "tree", "tree");
		assertThat(response.resources().get(0).label()).isEqualTo("Styling");
		verify(treeLinks).checkNoLoop(node, List.of(), css);
		verify(treeLinks).checkNoLoop(node, List.of(), html);
		verify(treeLinks, never()).checkNoLoop(node, List.of(), reading);
		assertThat(response.resources().get(3).readiness()).isZero();
		assertThat(response.resources().get(3).counts()).isFalse();
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

		assertThatThrownBy(() -> create(new NodeRequest.Resource("url", "https://example.com", null, null, null, null, null, null, true)))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("can't count toward readiness");
		assertThatThrownBy(() -> create(new NodeRequest.Resource("url", null, null, null, null, null, null, null, false)))
			.isInstanceOf(BadRequestException.class);
		assertThatThrownBy(() -> create(new NodeRequest.Resource("tree", "https://example.com", 3L, null, null, null, null, null, true)))
			.isInstanceOf(BadRequestException.class);
		assertThatThrownBy(() -> create(new NodeRequest.Resource("deck", null, 3L, null, null, null, null, null, true)))
			.isInstanceOf(BadRequestException.class);
		assertThatThrownBy(() -> create(new NodeRequest.Resource("quiz", null, null, 3L, null, null, null, null, true)))
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
	void aDeckCountsWithItsShareOfCardsPassedAndATreeAlongsideIsAveragedIn() {
		Node node = withId(new Node(user, "CSS Flexbox"), 7L);
		Tree article = withId(new Tree(user, "Article"), 3L);
		Instant reviewed = Instant.parse("2026-09-01T10:00:00Z");
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));
		when(trees.findByIdAndOwner(3L, user)).thenReturn(Optional.of(article));
		when(trees.findTreesContaining(node)).thenReturn(List.of());
		when(treeNodes.findNodesInTree(article)).thenReturn(List.of(manual(11L, "Reading", 60)));
		when(flashcards.deck(40L)).thenReturn(Optional.of(new DeckSummary(40L, "Flexbox cards",
				new DeckProgress(20, 15, 75, false, reviewed, 0))));

		NodeResponse response = service.update(7L,
				new NodeRequest("CSS Flexbox", null, 0, List.of(deck(40L, true), tree(3L, null, true)), null));

		// (75 + 60) / 2 = 67.5, rounded to 68: the 7.0 example with a tree in place of the article
		assertThat(response.readiness()).isEqualTo(68);
		assertThat(response.lastReviewedAt()).isEqualTo(reviewed);
		assertThat(response.resources().get(0)).isEqualTo(new NodeResponse.Resource("deck", null, null,
				new DeckRef(40L, "Flexbox cards"), null, null, null, null, true, 75, reviewed, false));
		assertThat(response.resources().get(1).readiness()).isEqualTo(60);
		// Only the tree can make a loop
		verify(treeLinks).checkNoLoop(node, List.of(), article);
	}

	@Test
	void aDeckThatIsNotTheUsersIsNotFoundAndADeckTwiceIsRefused() {
		when(flashcards.deck(40L)).thenReturn(Optional.empty());
		when(flashcards.deck(41L))
			.thenReturn(Optional.of(new DeckSummary(41L, "Mine", new DeckProgress(0, 0, 0, false, null, 0))));

		assertThatThrownBy(() -> create(deck(40L, true))).isInstanceOf(NotFoundException.class)
			.hasMessage("Deck 40 not found");
		assertThatThrownBy(() -> service.create(
				new NodeRequest("Twice", null, 0, List.of(deck(41L, true), deck(41L, false)), null)))
			.isInstanceOf(BadRequestException.class)
			.hasMessage("Deck 41 is listed more than once");
	}

	@Test
	void aMaterialCountsWithTheLatestProgressReportedAndMustBeTheUsers() {
		Node node = withId(new Node(user, "CSS Flexbox"), 7L);
		Instant reported = Instant.parse("2026-09-15T10:00:00Z");
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));
		when(trees.findTreesContaining(node)).thenReturn(List.of());
		when(materials.material(50L)).thenReturn(Optional.of(new MaterialSummary(50L, "Flexbox guide", 60, reported)));
		when(materials.material(51L)).thenReturn(Optional.empty());

		NodeResponse response = service.update(7L,
				new NodeRequest("CSS Flexbox", null, 0, List.of(material(50L, true)), null));

		assertThat(response.readiness()).isEqualTo(60);
		assertThat(response.lastReviewedAt()).isEqualTo(reported);
		assertThat(response.resources().get(0).material()).isEqualTo(new MaterialRef(50L, "Flexbox guide"));
		assertThatThrownBy(() -> create(material(51L, true))).isInstanceOf(NotFoundException.class)
			.hasMessage("Material 51 not found");
		assertThatThrownBy(() -> service.create(
				new NodeRequest("Twice", null, 0, List.of(material(50L, true), material(50L, false)), null)))
			.isInstanceOf(BadRequestException.class)
			.hasMessage("Material 50 is listed more than once");
	}

	@Test
	void aLessonCountsWithTheProgressEnteredAndWhenItWasLastOpened() {
		Node node = withId(new Node(user, "Front-end Basics"), 7L);
		Instant opened = Instant.parse("2026-09-20T10:00:00Z");
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));
		when(trees.findTreesContaining(node)).thenReturn(List.of());
		when(lessons.lesson(60L)).thenReturn(Optional.of(new LessonSummary(60L, "Flexbox lesson", 100, opened)));
		when(lessons.lesson(61L)).thenReturn(Optional.empty());

		NodeResponse response = service.update(7L,
				new NodeRequest("Front-end Basics", null, 0, List.of(lesson(60L, true)), null));

		assertThat(response.readiness()).isEqualTo(100);
		assertThat(response.lastReviewedAt()).isEqualTo(opened);
		assertThat(response.resources().get(0).lesson()).isEqualTo(new LessonRef(60L, "Flexbox lesson"));
		assertThatThrownBy(() -> create(lesson(61L, true))).isInstanceOf(NotFoundException.class)
			.hasMessage("Lesson 61 not found");
	}

	@Test
	void aQuestionSetCountsWithTheShareOfItsQuestionsSolved() {
		Node node = withId(new Node(user, "CSS Flexbox"), 7L);
		Instant solved = Instant.parse("2026-09-22T10:00:00Z");
		when(nodes.findByIdAndOwner(7L, user)).thenReturn(Optional.of(node));
		when(trees.findTreesContaining(node)).thenReturn(List.of());
		when(questionSets.questionSet(70L)).thenReturn(Optional.of(new QuestionSetSummary(70L, "Flexbox exercises",
				new SetProgress(4, 3, 75, solved))));
		when(questionSets.questionSet(71L)).thenReturn(Optional.empty());

		NodeResponse response = service.update(7L,
				new NodeRequest("CSS Flexbox", null, 0, List.of(questionSet(70L, true)), null));

		assertThat(response.readiness()).isEqualTo(75);
		assertThat(response.lastReviewedAt()).isEqualTo(solved);
		assertThat(response.resources().get(0).questionSet()).isEqualTo(new QuestionSetRef(70L, "Flexbox exercises"));
		assertThatThrownBy(() -> create(questionSet(71L, true))).isInstanceOf(NotFoundException.class)
			.hasMessage("Question set 71 not found");
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
		return new NodeRequest.Resource("url", url, null, null, null, null, null, label, false);
	}

	private static NodeRequest.Resource tree(Long treeId, String label, boolean counts) {
		return new NodeRequest.Resource("tree", null, treeId, null, null, null, null, label, counts);
	}

	private static NodeRequest.Resource questionSet(Long setId, boolean counts) {
		return new NodeRequest.Resource("question_set", null, null, null, null, null, setId, null, counts);
	}

	private static NodeRequest.Resource lesson(Long lessonId, boolean counts) {
		return new NodeRequest.Resource("lesson", null, null, null, null, lessonId, null, null, counts);
	}

	private static NodeRequest.Resource material(Long materialId, boolean counts) {
		return new NodeRequest.Resource("material", null, null, null, materialId, null, null, null, counts);
	}

	private static NodeRequest.Resource deck(Long deckId, boolean counts) {
		return new NodeRequest.Resource("deck", null, null, deckId, null, null, null, null, counts);
	}

	private Node manual(Long id, String title, int readiness) {
		Node node = withId(new Node(user, title), id);
		node.setReadiness(readiness);
		return node;
	}

}
