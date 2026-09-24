import { Node, Tree, TreeNode } from './api.models';

/** Builders for specs; override only the fields a test cares about. */
export function aNode(overrides: Partial<Node> = {}): Node {
  return {
    id: 1,
    title: 'Generics',
    description: null,
    readiness: 50,
    manualReadiness: 50,
    readinessSourceType: 'manual',
    linkedTree: null,
    links: [],
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
    links: [],
    readiness: 50,
    linkedTree: null,
    positionX: 0,
    positionY: 0,
    aggregateThreshold: 80,
    individualThreshold: 70,
    prerequisiteIds: [],
    dependentIds: [],
    ...overrides,
  };
}
