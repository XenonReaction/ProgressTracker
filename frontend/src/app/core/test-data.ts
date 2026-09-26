import {
  Card,
  CodingQuestion,
  Deck,
  Lesson,
  Material,
  Node,
  NodeResource,
  QuestionSet,
  Tree,
  TreeNode,
} from './api.models';

/** Builders for specs; override only the fields a test cares about. */
export function aNode(overrides: Partial<Node> = {}): Node {
  return {
    id: 1,
    title: 'Generics',
    description: null,
    readiness: 50,
    manualReadiness: 50,
    lastReviewedAt: null,
    resources: [],
    tags: [],
    createdAt: '2026-01-01T00:00:00Z',
    updatedAt: '2026-01-01T00:00:00Z',
    ...overrides,
  };
}

export function aTree(overrides: Partial<Tree> = {}): Tree {
  return {
    id: 1,
    title: 'Java Fundamentals',
    description: null,
    category: null,
    tags: [],
    readiness: 0,
    lastReviewedAt: null,
    createdAt: '2026-01-01T00:00:00Z',
    updatedAt: '2026-01-01T00:00:00Z',
    editSessionStartedAt: null,
    ...overrides,
  };
}

export function aTreeNode(overrides: Partial<TreeNode> = {}): TreeNode {
  return {
    id: 1,
    treeId: 1,
    nodeId: 1,
    title: 'Generics',
    description: null,
    resources: [],
    readiness: 50,
    lastReviewedAt: null,
    positionX: 0,
    positionY: 0,
    aggregateThreshold: 80,
    individualThreshold: 70,
    prerequisiteIds: [],
    dependentIds: [],
    ...overrides,
  };
}

export function aDeck(overrides: Partial<Deck> = {}): Deck {
  return {
    id: 1,
    title: 'CSS Flexbox',
    description: null,
    cardCount: 0,
    passedCount: 0,
    readiness: 0,
    complete: false,
    lastReviewedAt: null,
    createdAt: '2026-01-01T00:00:00Z',
    updatedAt: '2026-01-01T00:00:00Z',
    ...overrides,
  };
}

export function aCard(overrides: Partial<Card> = {}): Card {
  return {
    id: 1,
    deckId: 1,
    deckTitle: 'CSS Flexbox',
    front: 'Which property sets the main axis?',
    back: 'flex-direction',
    correctInARow: 0,
    passed: false,
    lastReviewedAt: null,
    createdAt: '2026-01-01T00:00:00Z',
    updatedAt: '2026-01-01T00:00:00Z',
    ...overrides,
  };
}

export function aTreeResource(
  id: number,
  title: string,
  counts = true,
  label: string | null = null,
  readiness: number | null = 50,
): NodeResource {
  return {
    type: 'tree',
    url: null,
    tree: { id, title },
    deck: null,
    material: null,
    lesson: null,
    questionSet: null,
    label,
    counts,
    readiness,
    lastReviewedAt: null,
  };
}

export function aDeckResource(
  id: number,
  title: string,
  counts = true,
  readiness: number | null = 50,
  lastReviewedAt: string | null = null,
): NodeResource {
  return {
    type: 'deck',
    url: null,
    tree: null,
    deck: { id, title },
    material: null,
    lesson: null,
    questionSet: null,
    label: null,
    counts,
    readiness,
    lastReviewedAt,
  };
}

export function aUrlResource(url: string, label: string | null = null): NodeResource {
  return {
    type: 'url',
    url,
    tree: null,
    deck: null,
    material: null,
    lesson: null,
    questionSet: null,
    label,
    counts: false,
    readiness: null,
    lastReviewedAt: null,
  };
}

export function aMaterialResource(
  id: number,
  title: string,
  counts = true,
  readiness: number | null = 50,
  lastReviewedAt: string | null = null,
): NodeResource {
  return {
    type: 'material',
    url: null,
    tree: null,
    deck: null,
    material: { id, title },
    lesson: null,
    questionSet: null,
    label: null,
    counts,
    readiness,
    lastReviewedAt,
  };
}

export function aMaterial(overrides: Partial<Material> = {}): Material {
  return {
    id: 1,
    title: 'A Complete Guide to Flexbox',
    url: 'https://css-tricks.com/snippets/css/a-guide-to-flexbox/',
    notes: null,
    progress: 0,
    lastReviewedAt: null,
    updateCount: 0,
    createdAt: '2026-01-01T00:00:00Z',
    updatedAt: '2026-01-01T00:00:00Z',
    ...overrides,
  };
}

export function aLessonResource(
  id: number,
  title: string,
  counts = true,
  readiness: number | null = 50,
  lastReviewedAt: string | null = null,
): NodeResource {
  return {
    type: 'lesson',
    url: null,
    tree: null,
    deck: null,
    material: null,
    lesson: { id, title },
    questionSet: null,
    label: null,
    counts,
    readiness,
    lastReviewedAt,
  };
}

export function aLesson(overrides: Partial<Lesson> = {}): Lesson {
  return {
    id: 1,
    title: 'Flexbox in Ten Minutes',
    summary: null,
    sections: [],
    progress: 0,
    lastReviewedAt: null,
    createdAt: '2026-01-01T00:00:00Z',
    updatedAt: '2026-01-01T00:00:00Z',
    ...overrides,
  };
}

export function aQuestionSetResource(
  id: number,
  title: string,
  counts = true,
  readiness: number | null = 50,
  lastReviewedAt: string | null = null,
): NodeResource {
  return {
    type: 'question_set',
    url: null,
    tree: null,
    deck: null,
    material: null,
    lesson: null,
    questionSet: { id, title },
    label: null,
    counts,
    readiness,
    lastReviewedAt,
  };
}

export function aQuestionSet(overrides: Partial<QuestionSet> = {}): QuestionSet {
  return {
    id: 1,
    title: 'CSS Flexbox exercises',
    description: null,
    questionCount: 0,
    solvedCount: 0,
    readiness: 0,
    lastReviewedAt: null,
    createdAt: '2026-01-01T00:00:00Z',
    updatedAt: '2026-01-01T00:00:00Z',
    ...overrides,
  };
}

export function aCodingQuestion(overrides: Partial<CodingQuestion> = {}): CodingQuestion {
  return {
    id: 1,
    setId: 1,
    setTitle: 'CSS Flexbox exercises',
    title: 'Centre a box',
    language: 'css',
    problem: 'Centre `.box` inside `.frame`.',
    examples: null,
    solution: null,
    solved: false,
    solutionRevealed: false,
    revealedBeforeSolved: false,
    lastAttemptAt: null,
    createdAt: '2026-01-01T00:00:00Z',
    updatedAt: '2026-01-01T00:00:00Z',
    ...overrides,
  };
}
