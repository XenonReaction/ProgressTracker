import { NodeResource, NodeResourceRequest, NodeResourceType, TreeRef } from '../core/api.models';

/** Headings for each resource type, in the order a node's page lists them. */
export const RESOURCE_GROUPS: { type: NodeResourceType; heading: string }[] = [
  { type: 'tree', heading: 'Trees' },
  { type: 'url', heading: 'Links' },
];

/** What to call a resource: its label, or else its target's title. */
export function resourceTitle(resource: NodeResource): string {
  return resource.label || resource.tree?.title || resource.url || '';
}

/** Whether any resource counts, so the hand-entered readiness isn't used. */
export function hasCountingResource(resources: NodeResource[]): boolean {
  return resources.some((resource) => resource.counts);
}

/** The trees a node's readiness comes from. */
export function countingTrees(resources: NodeResource[]): TreeRef[] {
  return resources.filter((r) => r.counts && r.tree).map((r) => r.tree!);
}

/** "A", "A and B", "A, B and C". */
export function titleList(trees: TreeRef[]): string {
  const titles = trees.map((tree) => tree.title);
  return titles.length < 2
    ? (titles[0] ?? '')
    : `${titles.slice(0, -1).join(', ')} and ${titles.at(-1)}`;
}

/** The request form of a resource, to send it back unchanged. */
export function toRequest(resource: NodeResource): NodeResourceRequest {
  return {
    type: resource.type,
    url: resource.url,
    treeId: resource.tree?.id ?? null,
    label: resource.label,
    counts: resource.counts,
  };
}
