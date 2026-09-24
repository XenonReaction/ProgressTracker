/** Shapes of the backend's /api/v1 JSON. Keep in sync with the Java *Response/*Request records. */

export interface NodeLink {
  url: string;
  label: string | null;
}

/** A tree named in another response, e.g. the tree a node takes its readiness from. */
export interface TreeRef {
  id: number;
  title: string;
}

export type ReadinessSourceType = 'manual' | 'linked_tree';

/**
 * `readiness` is the value to show: `manualReadiness` (entered by hand), or the average of
 * `linkedTree`'s nodes when the node is linked. The hand-entered value is kept while linked.
 */
export interface Node {
  id: number;
  title: string;
  description: string | null;
  readiness: number;
  manualReadiness: number;
  readinessSourceType: ReadinessSourceType;
  linkedTree: TreeRef | null;
  links: NodeLink[];
  createdAt: string;
  updatedAt: string;
}

export interface NodeRequest {
  title: string;
  description: string | null;
  /** The hand-entered value, kept even while the node is linked. */
  readiness: number;
  links: NodeLink[];
  /** Take readiness from this tree instead; null or omitted for the hand-entered value. */
  linkedTreeId?: number | null;
}

export interface Tree {
  id: number;
  title: string;
  description: string | null;
  category: string | null;
  tags: string[];
  createdAt: string;
  updatedAt: string;
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
  /** Effective readiness: derived from `linkedTree` when the node is linked. */
  readiness: number;
  linkedTree: TreeRef | null;
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

/** A prerequisite edge; both ids are tree node ids. */
export interface Prerequisite {
  id: number;
  prerequisiteTreeNodeId: number;
  dependentTreeNodeId: number;
}

/** RFC 9457 problem response body, plus the extra properties the backend adds. */
export interface Problem {
  status: number;
  title: string;
  detail?: string;
  errors?: { field: string; message: string }[];
  /** Trees still using a node that couldn't be deleted. */
  trees?: TreeRef[];
  /** Nodes linked to a tree that couldn't be deleted. */
  nodes?: { id: number; title: string }[];
}
