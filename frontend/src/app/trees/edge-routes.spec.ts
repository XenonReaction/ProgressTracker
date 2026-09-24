import { aTreeNode } from '../core/test-data';
import { NODE_HEIGHT, NODE_WIDTH } from './tree-layout';
import { SIDE_GAP, STUB, pointsAttr, routeEdge, routeEdges, routeOutgrown } from './edge-routes';

describe('edge routes', () => {
  const top = aTreeNode({ id: 1, positionX: 0, positionY: 0 });
  const below = aTreeNode({ id: 2, positionX: 200, positionY: 200 });
  const above = aTreeNode({ id: 3, positionX: 200, positionY: -100 });
  const bottomPort = NODE_HEIGHT / 2; // 28, under the top node
  const topPortBelow = 200 - NODE_HEIGHT / 2; // 172, above the lower node

  describe('a dependent below its prerequisite', () => {
    it('goes down, across and down, with the middle segment halfway', () => {
      const route = routeEdge(7, top, below, null);

      const middle = (bottomPort + topPortBelow) / 2;
      expect(route.segments).toBe(3);
      expect(route.points).toEqual([
        { x: 0, y: bottomPort },
        { x: 0, y: middle },
        { x: 200, y: middle },
        { x: 200, y: topPortBelow },
      ]);
      expect(route.handles).toEqual([{ segment: 1, axis: 'y', offsetIndex: 0 }]);
    });

    it('moves the middle segment by its stored offset', () => {
      const route = routeEdge(7, top, below, { segments: 3, offsets: [40] });

      expect(route.points[1].y).toBe(140);
      expect(route.offsets).toEqual([40]);
    });

    it('keeps the offset when a node moves sideways (E6)', () => {
      const route = routeEdge(7, top, { ...below, positionX: 400 }, { segments: 3, offsets: [40] });

      expect(route.points[1].y).toBe(140);
      expect(route.points[2]).toEqual({ x: 400, y: 140 });
    });

    it('clamps the middle segment between the ports (E6b)', () => {
      const route = routeEdge(7, top, below, { segments: 3, offsets: [500] });

      expect(route.points[1].y).toBe(topPortBelow - STUB);
      expect(route.offsets[0]).toBe(topPortBelow - STUB - (bottomPort + topPortBelow) / 2);
    });
  });

  describe('a dependent level with or above its prerequisite', () => {
    it('goes down, out to the right of both nodes, up, across and down into the top', () => {
      const route = routeEdge(7, top, above, null);

      const side = 200 + NODE_WIDTH / 2 + SIDE_GAP;
      const abovePort = -100 - NODE_HEIGHT / 2;
      expect(route.segments).toBe(5);
      expect(route.points).toEqual([
        { x: 0, y: bottomPort },
        { x: 0, y: bottomPort + STUB },
        { x: side, y: bottomPort + STUB },
        { x: side, y: abovePort - STUB },
        { x: 200, y: abovePort - STUB },
        { x: 200, y: abovePort },
      ]);
      expect(route.handles.map((h) => h.axis)).toEqual(['y', 'x', 'y']);
    });

    it('ignores a route stored for a different number of segments (E6b)', () => {
      const route = routeEdge(7, top, above, { segments: 3, offsets: [40] });
      expect(route.offsets).toEqual([0, 0, 0]);
    });

    it('applies its three offsets, keeping the ends outside the boxes', () => {
      const route = routeEdge(7, top, above, { segments: 5, offsets: [10, 50, -500] });

      expect(route.points[1].y).toBe(bottomPort + STUB + 10);
      expect(route.points[2].x).toBe(200 + NODE_WIDTH / 2 + SIDE_GAP + 50);
      expect(route.points[3].y).toBe(-100 - NODE_HEIGHT / 2 - STUB - 500);
      // Pushing the first segment up into the node is clamped
      expect(routeEdge(7, top, above, { segments: 5, offsets: [-100, 0, 0] }).points[1].y).toBe(bottomPort + STUB);
    });
  });

  it('routes every edge, preferring in-progress overrides, and skips edges with missing ends', () => {
    const byId = new Map([[1, top], [2, below]]);
    const edges = [
      { id: 7, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 2, route: { segments: 3 as const, offsets: [10] } },
      { id: 8, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 99, route: null },
    ];

    const routes = routeEdges(edges, byId, new Map([[7, { segments: 3 as const, offsets: [30] }]]));

    expect(routes.map((r) => r.id)).toEqual([7]);
    expect(routes[0].offsets).toEqual([30]);
  });

  it('knows when a move has outgrown a stored route', () => {
    const edge = { id: 7, prerequisiteTreeNodeId: 1, dependentTreeNodeId: 2, route: { segments: 3 as const, offsets: [10] } };

    expect(routeOutgrown(edge, new Map([[1, top], [2, below]]))).toBe(false);
    expect(routeOutgrown(edge, new Map([[1, top], [2, { ...below, positionY: -100 }]]))).toBe(true);
    expect(routeOutgrown({ ...edge, route: null }, new Map([[1, top], [2, above]]))).toBe(false);
  });

  it('writes points for an SVG polyline', () => {
    expect(pointsAttr([{ x: 0, y: 1 }, { x: 2.5, y: 3 }])).toBe('0,1 2.5,3');
  });
});
