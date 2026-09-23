import { TreeNode } from '../core/api.models';
import { aTreeNode } from '../core/test-data';
import { readinessState } from './readiness';

function stateOf(target: TreeNode, others: TreeNode[]) {
  return readinessState(target, new Map([target, ...others].map((n) => [n.id, n])));
}

describe('readinessState', () => {
  // Default thresholds: prerequisites must average >= 80 and each be >= 70
  const dependent = (prerequisiteIds: number[]) => aTreeNode({ id: 10, prerequisiteIds });

  it('is ready when there are no prerequisites', () => {
    expect(stateOf(aTreeNode({ readiness: 0 }), [])).toBe('ready');
  });

  it('is ready when the average and every prerequisite meet the thresholds', () => {
    const prerequisites = [aTreeNode({ id: 1, readiness: 90 }), aTreeNode({ id: 2, readiness: 70 })];
    expect(stateOf(dependent([1, 2]), prerequisites)).toBe('ready');
  });

  it('is locked when the average is too low, even if each prerequisite passes individually', () => {
    const prerequisites = [aTreeNode({ id: 1, readiness: 75 }), aTreeNode({ id: 2, readiness: 75 })];
    expect(stateOf(dependent([1, 2]), prerequisites)).toBe('locked');
  });

  it('is locked when one prerequisite is below the individual threshold, even if the average passes', () => {
    const prerequisites = [aTreeNode({ id: 1, readiness: 100 }), aTreeNode({ id: 2, readiness: 65 })];
    expect(stateOf(dependent([1, 2]), prerequisites)).toBe('locked');
  });

  it("uses the dependent node's own thresholds", () => {
    const prerequisite = aTreeNode({ id: 1, readiness: 60 });
    const lenient = aTreeNode({ id: 10, prerequisiteIds: [1], aggregateThreshold: 50, individualThreshold: 50 });
    expect(stateOf(lenient, [prerequisite])).toBe('ready');
  });

  it("ignores its own readiness: a 100% node can still be locked", () => {
    const prerequisite = aTreeNode({ id: 1, readiness: 10 });
    expect(stateOf(aTreeNode({ id: 10, readiness: 100, prerequisiteIds: [1] }), [prerequisite])).toBe('locked');
  });
});
