import { TreeNode } from '../core/api.models';

/** Node boxes are drawn centred on their stored (positionX, positionY). */
export const NODE_WIDTH = 180;
export const NODE_HEIGHT = 56;
const PADDING = 40;

export interface Point {
  x: number;
  y: number;
}

export interface EdgeLine {
  key: string;
  from: Point;
  to: Point;
}

/** SVG viewBox that fits every node box plus padding. */
export function viewBoxFor(treeNodes: TreeNode[]): { x: number; y: number; width: number; height: number } {
  if (treeNodes.length === 0) {
    return { x: 0, y: 0, width: 0, height: 0 };
  }
  const xs = treeNodes.map((n) => n.positionX);
  const ys = treeNodes.map((n) => n.positionY);
  const x = Math.min(...xs) - NODE_WIDTH / 2 - PADDING;
  const y = Math.min(...ys) - NODE_HEIGHT / 2 - PADDING;
  return {
    x,
    y,
    width: Math.max(...xs) + NODE_WIDTH / 2 + PADDING - x,
    height: Math.max(...ys) + NODE_HEIGHT / 2 + PADDING - y,
  };
}

/**
 * One line per prerequisite edge, from the prerequisite's box edge to the dependent's box
 * edge, so arrowheads aren't hidden under the boxes.
 */
export function edgeLines(treeNodes: TreeNode[], byId: Map<number, TreeNode>): EdgeLine[] {
  return treeNodes.flatMap((dependent) =>
    dependent.prerequisiteIds
      .map((id) => byId.get(id))
      .filter((prerequisite): prerequisite is TreeNode => prerequisite !== undefined)
      .map((prerequisite) => {
        const from = center(prerequisite);
        const to = center(dependent);
        return { key: `${prerequisite.id}-${dependent.id}`, from: boxEdge(from, to), to: boxEdge(to, from) };
      }),
  );
}

function center(node: TreeNode): Point {
  return { x: node.positionX, y: node.positionY };
}

/** Where the segment from a box's centre towards `toward` leaves the box. */
function boxEdge(boxCenter: Point, toward: Point): Point {
  const dx = toward.x - boxCenter.x;
  const dy = toward.y - boxCenter.y;
  if (dx === 0 && dy === 0) {
    return boxCenter;
  }
  const scale = Math.min(
    dx === 0 ? Infinity : NODE_WIDTH / 2 / Math.abs(dx),
    dy === 0 ? Infinity : NODE_HEIGHT / 2 / Math.abs(dy),
  );
  return { x: boxCenter.x + dx * scale, y: boxCenter.y + dy * scale };
}
