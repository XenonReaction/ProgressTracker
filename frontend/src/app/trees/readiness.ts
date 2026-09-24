import { TreeNode } from '../core/api.models';

/**
 * How close a node is to being ready to start (5.5): four levels, from "not started" to
 * "ready". Each has its own border style and label as well as a colour.
 */
export type ReadinessState = 'not-started' | 'early' | 'close' | 'ready';

/** The levels from least to most ready, with the label shown in node boxes and the legend. */
export const READINESS_LEVELS: readonly { state: ReadinessState; label: string; from: number }[] = [
  { state: 'not-started', label: 'not started', from: 0 },
  { state: 'early', label: 'early', from: 0.25 },
  { state: 'close', label: 'close', from: 0.75 },
  { state: 'ready', label: 'ready', from: 1 },
];

/**
 * Progress towards being ready, from 0 upwards; 1 or more means ready. It's the lower of two
 * ratios: the prerequisites' average readiness over the aggregate threshold, and the weakest
 * prerequisite over the individual threshold. So a node is ready exactly when its
 * prerequisites average at least its aggregate threshold AND each reaches its individual
 * threshold. A node with no prerequisites is always ready; its own readiness doesn't count.
 */
export function readinessProgress(node: TreeNode, treeNodesById: Map<number, TreeNode>): number {
  const readiness = node.prerequisiteIds
    .map((id) => treeNodesById.get(id))
    .filter((prerequisite): prerequisite is TreeNode => prerequisite !== undefined)
    .map((prerequisite) => prerequisite.readiness);
  if (readiness.length === 0) {
    return 1;
  }
  const average = readiness.reduce((sum, value) => sum + value, 0) / readiness.length;
  return Math.min(ratio(average, node.aggregateThreshold), ratio(Math.min(...readiness), node.individualThreshold));
}

export function readinessState(node: TreeNode, treeNodesById: Map<number, TreeNode>): ReadinessState {
  const progress = readinessProgress(node, treeNodesById);
  return [...READINESS_LEVELS].reverse().find((level) => progress >= level.from)!.state;
}

export function readinessLabel(state: ReadinessState): string {
  return READINESS_LEVELS.find((level) => level.state === state)!.label;
}

/** A threshold of 0 is always met. */
function ratio(value: number, threshold: number): number {
  return threshold <= 0 ? Infinity : value / threshold;
}
