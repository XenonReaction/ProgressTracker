import { aTreeNode } from '../core/test-data';
import { NODE_HEIGHT, NODE_WIDTH, edgeLines, viewBoxFor } from './tree-layout';

describe('tree layout', () => {
  it('fits every node box plus padding in the view box', () => {
    const box = viewBoxFor([aTreeNode({ positionX: -100, positionY: 0 }), aTreeNode({ positionX: 100, positionY: 300 })]);

    expect(box.x).toBeLessThanOrEqual(-100 - NODE_WIDTH / 2);
    expect(box.y).toBeLessThanOrEqual(0 - NODE_HEIGHT / 2);
    expect(box.x + box.width).toBeGreaterThanOrEqual(100 + NODE_WIDTH / 2);
    expect(box.y + box.height).toBeGreaterThanOrEqual(300 + NODE_HEIGHT / 2);
  });

  it('gives an empty tree a canvas to click on', () => {
    const box = viewBoxFor([]);
    expect(box.width).toBeGreaterThan(0);
    expect(box.height).toBeGreaterThan(0);
  });

  it('draws each edge between box edges, from prerequisite to dependent', () => {
    const top = aTreeNode({ id: 1, positionX: 0, positionY: 0 });
    const bottom = aTreeNode({ id: 2, positionX: 0, positionY: 200 });

    const [edge] = edgeLines([{ id: 7, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 2 }], new Map([[1, top], [2, bottom]]));

    expect(edge.id).toBe(7);
    expect(edge.from.x).toBe(0);
    expect(edge.from.y).toBeCloseTo(NODE_HEIGHT / 2);
    expect(edge.to.x).toBe(0);
    expect(edge.to.y).toBeCloseTo(200 - NODE_HEIGHT / 2);
  });

  it('clips diagonal edges to the box outline', () => {
    const a = aTreeNode({ id: 1, positionX: 0, positionY: 0 });
    const b = aTreeNode({ id: 2, positionX: 400, positionY: 100 });

    const [edge] = edgeLines([{ id: 7, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 2 }], new Map([[1, a], [2, b]]));

    // Leaves through the right-hand side of the first box
    expect(edge.from.x).toBeCloseTo(NODE_WIDTH / 2);
    expect(Math.abs(edge.from.y)).toBeLessThanOrEqual(NODE_HEIGHT / 2);
  });

  it('skips edges whose ends are not in the tree', () => {
    const a = aTreeNode({ id: 1 });
    expect(edgeLines([{ id: 7, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 99 }], new Map([[1, a]]))).toEqual([]);
  });
});
