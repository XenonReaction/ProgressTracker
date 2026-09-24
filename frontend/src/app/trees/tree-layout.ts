import { TreeNode } from '../core/api.models';

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
