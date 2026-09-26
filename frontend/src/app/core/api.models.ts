/** Shapes of the backend's /api/v1 JSON. Keep in sync with the Java *Response/*Request records. */

/** A tree named in another response, e.g. a tree a node lists as a resource. */
export interface TreeRef {
  id: number;
  title: string;
}

/** A flashcard deck named in a node's resources. */
export interface DeckRef {
  id: number;
  title: string;
}

/** An external material named in a node's resources. */
export interface MaterialRef {
  id: number;
  title: string;
}

/** A lesson named in a node's resources. */
export interface LessonRef {
  id: number;
  title: string;
}

/** A set of coding questions named in a node's resources. */
export interface QuestionSetRef {
  id: number;
  title: string;
}

/** What a node resource points to. All but a URL can count toward readiness. */
export type NodeResourceType = 'url' | 'tree' | 'deck' | 'material' | 'lesson' | 'question_set';

/**
 * Something a node points to, in the node's order: a tree (`tree`), a flashcard deck
 * (`deck`), an external material with reported progress (`material`), a lesson written in
 * the app (`lesson`), a set of coding questions (`question_set`) or a plain external page
 * (`url`). `label` is as entered, or null to show
 * the target's title. `counts` is whether it counts toward the node's readiness; a URL never does.
 * `readiness` and `lastReviewedAt` are the resource's own (null for a URL), whether it
 * counts or not, and `reviewDue` whether a flashcard beneath it is due for review.
 */
export interface NodeResource {
  type: NodeResourceType;
  url: string | null;
  tree: TreeRef | null;
  deck: DeckRef | null;
  material: MaterialRef | null;
  lesson: LessonRef | null;
  questionSet: QuestionSetRef | null;
  label: string | null;
  counts: boolean;
  readiness: number | null;
  lastReviewedAt: string | null;
  reviewDue: boolean;
}

export interface NodeResourceRequest {
  type: NodeResourceType;
  url?: string | null;
  treeId?: number | null;
  deckId?: number | null;
  materialId?: number | null;
  lessonId?: number | null;
  questionSetId?: number | null;
  label?: string | null;
  counts?: boolean;
}

/**
 * `readiness` is the value to show: the average of the resources that count, or
 * `manualReadiness` (entered by hand) when none do. The hand-entered value is always kept.
 */
export interface Node {
  id: number;
  title: string;
  description: string | null;
  readiness: number;
  manualReadiness: number;
  /** The latest review beneath the resources that count; null if none. */
  lastReviewedAt: string | null;
  /** Whether a flashcard beneath the resources that count is due for review. */
  reviewDue: boolean;
  resources: NodeResource[];
  tags: string[];
  createdAt: string;
  updatedAt: string;
}

export interface NodeRequest {
  title: string;
  description: string | null;
  /** The hand-entered value, kept even while a resource counts. */
  readiness: number;
  resources?: NodeResourceRequest[];
  tags?: string[];
}

export interface Tree {
  id: number;
  title: string;
  description: string | null;
  category: string | null;
  tags: string[];
  /** The average readiness of its nodes (0 when empty). */
  readiness: number;
  /** The latest review beneath any of its nodes; null if none. */
  lastReviewedAt: string | null;
  /** Whether any of its nodes has a review due. */
  reviewDue: boolean;
  createdAt: string;
  updatedAt: string;
  /** Set while the tree is in edit mode (it has a restore point). */
  editSessionStartedAt: string | null;
}

export interface TreeEditSession {
  startedAt: string;
}

export interface TreeRequest {
  title: string;
  description: string | null;
  category: string | null;
  tags: string[];
}

/**
 * A library node placed in a tree. `id` is the tree node id; `nodeId` is the library node.
 * `prerequisiteIds` / `dependentIds` are tree node ids in the same tree.
 */
export interface TreeNode {
  id: number;
  treeId: number;
  nodeId: number;
  title: string;
  description: string | null;
  resources: NodeResource[];
  /** Effective readiness: derived when any of `resources` counts. */
  readiness: number;
  lastReviewedAt: string | null;
  reviewDue: boolean;
  positionX: number;
  positionY: number;
  aggregateThreshold: number;
  individualThreshold: number;
  prerequisiteIds: number[];
  dependentIds: number[];
}

export interface TreeNodeCreateRequest {
  nodeId: number;
  positionX: number;
  positionY: number;
  aggregateThreshold?: number;
  individualThreshold?: number;
}

export interface TreeNodeUpdateRequest {
  positionX: number;
  positionY: number;
  aggregateThreshold: number;
  individualThreshold: number;
}

export interface TreeNodePosition {
  treeNodeId: number;
  positionX: number;
  positionY: number;
}

/**
 * A right-angle edge's hand adjustments: for the route with `segments` segments (3 or 5),
 * how far each draggable segment was moved from its default place. See `edge-routes.ts`.
 */
export interface EdgeRoute {
  segments: 3 | 5;
  offsets: number[];
}

/** A prerequisite edge; both ids are tree node ids. `route` is null for the default route. */
export interface Prerequisite {
  id: number;
  prerequisiteTreeNodeId: number;
  dependentTreeNodeId: number;
  route: EdgeRoute | null;
}

/**
 * A flashcard deck. `readiness` is the share of its cards that have passed, and it's
 * `complete` once at least 80% have.
 */
export interface Deck {
  id: number;
  title: string;
  description: string | null;
  cardCount: number;
  passedCount: number;
  readiness: number;
  complete: boolean;
  /** The latest answer to any of its cards; null if none. */
  lastReviewedAt: string | null;
  /** How many passed cards are due for review. Readiness doesn't change while they're due. */
  dueCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface DeckRequest {
  title: string;
  description: string | null;
}

/**
 * A flashcard. It has `passed` when its last 3 answers were correct; `correctInARow` counts
 * towards that (0 to 3). A passed card comes back for review at `dueAt` (7 days after it
 * passes, then 14, 30 and 60), and is `due` from then. `deckTitle` is for the review page,
 * which mixes decks.
 */
export interface Card {
  id: number;
  deckId: number;
  deckTitle: string;
  front: string;
  back: string;
  correctInARow: number;
  passed: boolean;
  lastReviewedAt: string | null;
  /** When a passed card is next due for review; null while it hasn't passed. */
  dueAt: string | null;
  due: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CardRequest {
  front: string;
  back: string;
}

/**
 * An external material (article, video, course). `progress` is the latest the user reported
 * (0 if none yet) and `lastReviewedAt` when; `updateCount` is how many reports there are.
 */
export interface Material {
  id: number;
  title: string;
  url: string;
  notes: string | null;
  progress: number;
  lastReviewedAt: string | null;
  updateCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface MaterialRequest {
  title: string;
  url: string;
  notes: string | null;
}

/** One entry in a material's progress history. */
export interface ProgressUpdate {
  id: number;
  progress: number;
  note: string | null;
  recordedAt: string;
}

export interface ProgressUpdateRequest {
  progress: number;
  note: string | null;
}

/** One section of a lesson: a heading and a Markdown body. */
export interface LessonSection {
  title: string;
  body: string;
}

/**
 * A lesson written in the app. `progress` is the latest the user entered (0 if none), and
 * `lastReviewedAt` when they last opened it (reading it through the API doesn't count).
 */
export interface Lesson {
  id: number;
  title: string;
  summary: string | null;
  sections: LessonSection[];
  progress: number;
  lastReviewedAt: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface LessonRequest {
  title: string;
  summary: string | null;
  sections: LessonSection[];
}

/** A set of coding questions. `readiness` is the share of its questions solved. */
export interface QuestionSet {
  id: number;
  title: string;
  description: string | null;
  questionCount: number;
  solvedCount: number;
  readiness: number;
  lastReviewedAt: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface QuestionSetRequest {
  title: string;
  description: string | null;
}

/** HTML and CSS for now; JavaScript comes later. */
export type CodingLanguage = 'html' | 'css';

/**
 * A written coding problem, solved on the user's own machine. `problem` and `examples` are
 * Markdown. `solution` is null until revealed (or when the edit form asks for it).
 */
export interface CodingQuestion {
  id: number;
  setId: number;
  setTitle: string;
  title: string;
  language: CodingLanguage;
  problem: string;
  examples: string | null;
  solution: string | null;
  solved: boolean;
  solutionRevealed: boolean;
  /** Recorded for later; today a solved question counts the same either way. */
  revealedBeforeSolved: boolean;
  lastAttemptAt: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CodingQuestionRequest {
  title: string;
  language: CodingLanguage;
  problem: string;
  examples: string | null;
  solution: string;
}

/** RFC 9457 problem response body, plus the extra properties the backend adds. */
export interface Problem {
  status: number;
  title: string;
  detail?: string;
  errors?: { field: string; message: string }[];
  /** Trees still using a node that couldn't be deleted. */
  trees?: TreeRef[];
  /** Nodes listing a resource (tree, deck, material, lesson, question set) that couldn't be deleted. */
  nodes?: { id: number; title: string }[];
}
