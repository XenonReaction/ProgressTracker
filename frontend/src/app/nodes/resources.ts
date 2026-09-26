import { NodeResource, NodeResourceRequest, NodeResourceType, TreeRef } from '../core/api.models';

/** Headings for each resource type, in the order a node's page lists them. */
export const RESOURCE_GROUPS: { type: NodeResourceType; heading: string }[] = [
  { type: 'tree', heading: 'Trees' },
  { type: 'deck', heading: 'Decks' },
  { type: 'url', heading: 'Links' },
];

/** What one resource of each type is called. */
export const RESOURCE_TYPE_NAMES: Record<NodeResourceType, string> = {
  tree: 'Tree',
  deck: 'Deck',
  url: 'Link',
};

/** What to call a resource: its label, or else its target's title. */
export function resourceTitle(resource: NodeResource): string {
  return resource.label || resource.tree?.title || resource.deck?.title || resource.url || '';
}

/** The in-app page for a tree or deck resource; null for a URL. */
export function resourceLink(resource: NodeResource): (string | number)[] | null {
  if (resource.tree) {
    return ['/trees', resource.tree.id];
  }
  return resource.deck ? ['/decks', resource.deck.id] : null;
}

/** The resources a node's readiness comes from. */
export function countingResources(resources: NodeResource[]): NodeResource[] {
  return resources.filter((resource) => resource.counts);
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
export function titleList(items: (TreeRef | string)[]): string {
  const titles = items.map((item) => (typeof item === 'string' ? item : item.title));
  return titles.length < 2
    ? (titles[0] ?? '')
    : `${titles.slice(0, -1).join(', ')} and ${titles.at(-1)}`;
}

/** Where a derived readiness comes from, for screen readers: "tree Collections and deck Flexbox". */
export function readinessSources(resources: NodeResource[]): string {
  return titleList(
    countingResources(resources).map(
      (r) => `${RESOURCE_TYPE_NAMES[r.type].toLowerCase()} ${resourceTitle(r)}`,
    ),
  );
}

/** The request form of a resource, to send it back unchanged. */
export function toRequest(resource: NodeResource): NodeResourceRequest {
  return {
    type: resource.type,
    url: resource.url,
    treeId: resource.tree?.id ?? null,
    deckId: resource.deck?.id ?? null,
    label: resource.label,
    counts: resource.counts,
  };
}
