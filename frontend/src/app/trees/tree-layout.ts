import { Prerequisite, TreeNode } from '../core/api.models';

/** Node boxes are drawn centred on their stored (positionX, positionY). */
export const NODE_WIDTH = 180;
export const NODE_HEIGHT = 56;
/** Room around the nodes, so there's empty canvas to click on when adding nodes. */
const PADDING = 120;
/** Canvas shown for a tree with no nodes yet. */
const EMPTY_CANVAS = { x: -400, y: -150, width: 800, height: 450 };

export interface Point {
  x: number;
  y: number;
}

export interface ViewBox {
  x: number;
  y: number;
  width: number;
  height: number;
}

export interface EdgeLine {
  /** The prerequisite edge's id, used to delete it. */
  id: number;
  from: Point;
  to: Point;
}

/** SVG viewBox that fits every node box plus padding. */
export function viewBoxFor(treeNodes: TreeNode[]): ViewBox {
  if (treeNodes.length === 0) {
    return EMPTY_CANVAS;
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
export function edgeLines(edges: Prerequisite[], byId: Map<number, TreeNode>): EdgeLine[] {
  return edges.flatMap((edge) => {
    const prerequisite = byId.get(edge.prerequisiteTreeNodeId);
    const dependent = byId.get(edge.dependentTreeNodeId);
    if (!prerequisite || !dependent) {
      return [];
    }
    const from = center(prerequisite);
    const to = center(dependent);
    return [{ id: edge.id, from: boxEdge(from, to), to: boxEdge(to, from) }];
  });
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
