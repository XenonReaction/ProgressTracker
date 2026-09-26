package com.progressiontracker.progression.readiness;

import java.util.List;

import org.mockito.Mockito;

import com.progressiontracker.flashcards.FlashcardReadinessCalculator;
import com.progressiontracker.materials.MaterialReadinessCalculator;
import com.progressiontracker.progression.tree.TreeNodeRepository;

/**
 * A real {@link ReadinessService} for unit tests, reading trees from a mock repository and
 * decks and materials from mock module APIs.
 */
public final class TestReadiness {

	private TestReadiness() {
	}

	/** For tests without decks or materials. */
	public static ReadinessService service(TreeNodeRepository treeNodes) {
		return service(treeNodes, Mockito.mock(FlashcardReadinessCalculator.class));
	}

	/** For tests without materials. */
	public static ReadinessService service(TreeNodeRepository treeNodes, FlashcardReadinessCalculator flashcards) {
		return service(treeNodes, flashcards, Mockito.mock(MaterialReadinessCalculator.class));
	}

	public static ReadinessService service(TreeNodeRepository treeNodes, FlashcardReadinessCalculator flashcards,
			MaterialReadinessCalculator materials) {
		return new ReadinessService(List.of(new TreeResourceReadinessCalculator(),
				new DeckResourceReadinessCalculator(flashcards), new MaterialResourceReadinessCalculator(materials)),
				treeNodes);
	}

}
