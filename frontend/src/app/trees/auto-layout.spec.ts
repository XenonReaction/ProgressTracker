import { TreeNodePosition } from '../core/api.models';
import { aTreeNode } from '../core/test-data';
import { COLUMN_GAP, LAYER_GAP, autoLayout } from './auto-layout';

function positionOf(positions: TreeNodePosition[], treeNodeId: number) {
  const position = positions.find((p) => p.treeNodeId === treeNodeId)!;
  return { x: position.positionX, y: position.positionY };
}

describe('autoLayout', () => {
  it('stacks a chain top to bottom, centred on x = 0', () => {
    const positions = autoLayout([
      aTreeNode({ id: 3, prerequisiteIds: [2] }),
      aTreeNode({ id: 1 }),
      aTreeNode({ id: 2, prerequisiteIds: [1] }),
    ]);

    expect(positionOf(positions, 1)).toEqual({ x: 0, y: 0 });
    expect(positionOf(positions, 2)).toEqual({ x: 0, y: LAYER_GAP });
    expect(positionOf(positions, 3)).toEqual({ x: 0, y: 2 * LAYER_GAP });
  });

  it("places a node one row below its deepest prerequisite", () => {
    // 1 -> 2 -> 3, and 1 -> 3 directly: 3 still goes below 2
    const positions = autoLayout([
      aTreeNode({ id: 1 }),
      aTreeNode({ id: 2, prerequisiteIds: [1] }),
      aTreeNode({ id: 3, prerequisiteIds: [1, 2] }),
    ]);

    expect(positionOf(positions, 3).y).toBe(2 * LAYER_GAP);
  });

  it('spreads a row evenly and orders it by title when nothing else decides', () => {
    const positions = autoLayout([aTreeNode({ id: 1, title: 'Beta' }), aTreeNode({ id: 2, title: 'Alpha' })]);

    expect(positionOf(positions, 2)).toEqual({ x: -COLUMN_GAP / 2, y: 0 });
    expect(positionOf(positions, 1)).toEqual({ x: COLUMN_GAP / 2, y: 0 });
  });

  it('keeps children under their own parents to avoid crossing edges', () => {
    // Roots A (left) and B (right); B's child is titled so it would sort first alphabetically
    const positions = autoLayout([
      aTreeNode({ id: 1, title: 'A' }),
      aTreeNode({ id: 2, title: 'B' }),
      aTreeNode({ id: 3, title: 'Z child of A', prerequisiteIds: [1] }),
      aTreeNode({ id: 4, title: 'Another child of B', prerequisiteIds: [2] }),
    ]);

    expect(positionOf(positions, 3).x).toBeLessThan(positionOf(positions, 4).x);
  });

  it('returns a position for every node, and nothing for an empty tree', () => {
    expect(autoLayout([])).toEqual([]);
    expect(autoLayout([aTreeNode({ id: 1 }), aTreeNode({ id: 2 })]).map((p) => p.treeNodeId).sort()).toEqual([1, 2]);
  });

  it('does not loop forever on a cycle', () => {
    const positions = autoLayout([aTreeNode({ id: 1, prerequisiteIds: [2] }), aTreeNode({ id: 2, prerequisiteIds: [1] })]);
    expect(positions.length).toBe(2);
  });
});
