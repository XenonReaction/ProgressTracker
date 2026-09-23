/** Shapes of the backend's /api/v1 JSON. Keep in sync with the Java *Response/*Request records. */

export interface NodeLink {
  url: string;
  label: string | null;
}

export interface Node {
  id: number;
  title: string;
  description: string | null;
  readiness: number;
  readinessSourceType: string;
  links: NodeLink[];
  createdAt: string;
  updatedAt: string;
}

export interface NodeRequest {
  title: string;
  description: string | null;
  readiness: number;
  links: NodeLink[];
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
  readiness: number;
  positionX: number;
  positionY: number;
  aggregateThreshold: number;
  individualThreshold: number;
  prerequisiteIds: number[];
  dependentIds: number[];
}

/** RFC 9457 problem response body, plus the extra properties the backend adds. */
export interface Problem {
  status: number;
  title: string;
  detail?: string;
  errors?: { field: string; message: string }[];
  trees?: { id: number; title: string }[];
}
