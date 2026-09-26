package com.progressiontracker.progression.readiness;

import java.util.List;

import org.mockito.Mockito;

import com.progressiontracker.flashcards.FlashcardReadinessCalculator;
import com.progressiontracker.progression.tree.TreeNodeRepository;

/**
 * A real {@link ReadinessService} for unit tests, reading trees from a mock repository and
 * decks from a mock Flashcards API.
 */
public final class TestReadiness {

	private TestReadiness() {
	}

	/** For tests without decks. */
	public static ReadinessService service(TreeNodeRepository treeNodes) {
		return service(treeNodes, Mockito.mock(FlashcardReadinessCalculator.class));
	}

	public static ReadinessService service(TreeNodeRepository treeNodes, FlashcardReadinessCalculator flashcards) {
		return new ReadinessService(
				List.of(new TreeResourceReadinessCalculator(), new DeckResourceReadinessCalculator(flashcards)),
				treeNodes);
	}

}
