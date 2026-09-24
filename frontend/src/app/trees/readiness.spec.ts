import { TreeNode } from '../core/api.models';
import { aTreeNode } from '../core/test-data';
import { readinessLabel, readinessProgress, readinessState } from './readiness';

function byId(target: TreeNode, others: TreeNode[]): Map<number, TreeNode> {
  return new Map([target, ...others].map((n) => [n.id, n]));
}

function stateOf(target: TreeNode, others: TreeNode[]) {
  return readinessState(target, byId(target, others));
}

describe('readiness', () => {
  // Default thresholds: prerequisites must average >= 80 and each be >= 70
  const dependent = (prerequisiteIds: number[]) => aTreeNode({ id: 10, prerequisiteIds });
  const prerequisites = (...readiness: number[]) => readiness.map((r, i) => aTreeNode({ id: i + 1, readiness: r }));

  it('is ready when there are no prerequisites', () => {
    expect(stateOf(aTreeNode({ readiness: 0 }), [])).toBe('ready');
  });

  it('is ready when the average and every prerequisite meet the thresholds', () => {
    expect(stateOf(dependent([1, 2]), prerequisites(90, 70))).toBe('ready');
  });

  it('is not ready when the average is too low, even if each prerequisite passes individually', () => {
    expect(stateOf(dependent([1, 2]), prerequisites(75, 75))).not.toBe('ready');
  });

  it('is not ready when one prerequisite is below the individual threshold, even if the average passes', () => {
    expect(stateOf(dependent([1, 2]), prerequisites(100, 65))).not.toBe('ready');
  });

  it('measures progress by the lower of the two ratios', () => {
    // Average 45 / 80 = 0.5625; weakest 40 / 70 = 0.571
    expect(readinessProgress(dependent([1, 2]), byId(dependent([1, 2]), prerequisites(40, 50)))).toBeCloseTo(0.5625);
    // Average 100 / 80 = 1.25, but weakest 35 / 70 = 0.5
    expect(readinessProgress(dependent([1, 2]), byId(dependent([1, 2]), prerequisites(165, 35)))).toBeCloseTo(0.5);
  });

  it("matches the plan's examples with the default thresholds", () => {
    expect(stateOf(dependent([1, 2]), prerequisites(5, 10))).toBe('not-started');
    expect(stateOf(dependent([1, 2]), prerequisites(40, 50))).toBe('early');
    expect(stateOf(dependent([1, 2]), prerequisites(70, 75))).toBe('close');
    expect(stateOf(dependent([1, 2]), prerequisites(90, 85))).toBe('ready');
  });

  it('changes level at 25% and 75% progress', () => {
    const lenient = (readiness: number) =>
      stateOf(aTreeNode({ id: 10, prerequisiteIds: [1], aggregateThreshold: 100, individualThreshold: 0 }), prerequisites(readiness));
    expect(lenient(24)).toBe('not-started');
    expect(lenient(25)).toBe('early');
    expect(lenient(74)).toBe('early');
    expect(lenient(75)).toBe('close');
    expect(lenient(99)).toBe('close');
    expect(lenient(100)).toBe('ready');
  });

  it("uses the dependent node's own thresholds, and treats a threshold of 0 as met", () => {
    const lenient = aTreeNode({ id: 10, prerequisiteIds: [1], aggregateThreshold: 50, individualThreshold: 50 });
    expect(stateOf(lenient, prerequisites(60))).toBe('ready');
    const none = aTreeNode({ id: 10, prerequisiteIds: [1], aggregateThreshold: 0, individualThreshold: 0 });
    expect(stateOf(none, prerequisites(0))).toBe('ready');
  });

  it('ignores its own readiness: a 100% node can still be not started', () => {
    expect(stateOf(aTreeNode({ id: 10, readiness: 100, prerequisiteIds: [1] }), prerequisites(10))).toBe('not-started');
  });

  it('labels each level in words', () => {
    expect(readinessLabel('not-started')).toBe('not started');
    expect(readinessLabel('close')).toBe('close');
  });
});
