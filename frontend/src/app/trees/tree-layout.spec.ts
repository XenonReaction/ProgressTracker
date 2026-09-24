import { aTreeNode } from '../core/test-data';
import { NODE_HEIGHT, NODE_WIDTH, viewBoxFor } from './tree-layout';

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
});
