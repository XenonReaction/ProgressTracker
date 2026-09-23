import { TreeNodePosition, TreeNode } from '../core/api.models';

export const LAYER_GAP = 150;
export const COLUMN_GAP = 220;

/**
 * Layered top-down layout. Each node goes one row below its deepest prerequisite (nodes
 * without prerequisites form the top row). Within a row, nodes are ordered by the average
 * x of their prerequisites so edges cross less, then by title for a stable result. Rows
 * are centred on x = 0.
 */
export function autoLayout(treeNodes: TreeNode[]): TreeNodePosition[] {
  const byId = new Map(treeNodes.map((n) => [n.id, n]));
  const depth = new Map<number, number>();

  const depthOf = (node: TreeNode, visiting: Set<number>): number => {
    const known = depth.get(node.id);
    if (known !== undefined) {
      return known;
    }
    // The backend forbids cycles; the guard just keeps a bad input from looping forever
    visiting.add(node.id);
    const prerequisites = node.prerequisiteIds
      .map((id) => byId.get(id))
      .filter((p): p is TreeNode => p !== undefined && !visiting.has(p.id));
    const result = prerequisites.length ? 1 + Math.max(...prerequisites.map((p) => depthOf(p, visiting))) : 0;
    visiting.delete(node.id);
    depth.set(node.id, result);
    return result;
  };
  treeNodes.forEach((n) => depthOf(n, new Set()));

  const rows: TreeNode[][] = [];
  treeNodes.forEach((n) => (rows[depth.get(n.id)!] ??= []).push(n));

  const x = new Map<number, number>();
  const positions: TreeNodePosition[] = [];
  rows.forEach((row, rowIndex) => {
    const averagePrerequisiteX = (n: TreeNode) => {
      const xs = n.prerequisiteIds.map((id) => x.get(id)).filter((v): v is number => v !== undefined);
      return xs.length ? xs.reduce((a, b) => a + b, 0) / xs.length : 0;
    };
    const ordered = [...row].sort(
      (a, b) => averagePrerequisiteX(a) - averagePrerequisiteX(b) || a.title.localeCompare(b.title),
    );
    ordered.forEach((n, i) => {
      const positionX = (i - (ordered.length - 1) / 2) * COLUMN_GAP;
      x.set(n.id, positionX);
      positions.push({ treeNodeId: n.id, positionX, positionY: rowIndex * LAYER_GAP });
    });
  });
  return positions;
}
