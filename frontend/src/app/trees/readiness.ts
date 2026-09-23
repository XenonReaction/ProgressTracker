import { TreeNode } from '../core/api.models';

/** v1 has two visual states only (see plan: "Gradient step count"). */
export type ReadinessState = 'ready' | 'locked';

/**
 * A node is ready to start when its prerequisites, taken together, average at least its
 * aggregate threshold AND each prerequisite on its own reaches its individual threshold.
 * Nodes with no prerequisites are always ready.
 */
export function readinessState(node: TreeNode, treeNodesById: Map<number, TreeNode>): ReadinessState {
  const prerequisites = node.prerequisiteIds
    .map((id) => treeNodesById.get(id))
    .filter((prerequisite): prerequisite is TreeNode => prerequisite !== undefined);
  if (prerequisites.length === 0) {
    return 'ready';
  }
  const average =
    prerequisites.reduce((sum, prerequisite) => sum + prerequisite.readiness, 0) / prerequisites.length;
  const eachMeetsMinimum = prerequisites.every((p) => p.readiness >= node.individualThreshold);
  return average >= node.aggregateThreshold && eachMeetsMinimum ? 'ready' : 'locked';
}
