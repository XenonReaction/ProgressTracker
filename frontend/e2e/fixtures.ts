import { APIRequestContext, Locator, Page, test as base, expect } from '@playwright/test';

import type { Node, Prerequisite, Tree, TreeNode } from '../src/app/core/api.models';

/**
 * Sets up test data through the REST API, so each test starts from exactly the state it
 * needs without clicking through other pages first.
 */
export class Api {
  constructor(private readonly request: APIRequestContext) {}

  async createNode(title: string, readiness = 0): Promise<Node> {
    return this.post('/api/v1/nodes', { title, description: null, readiness, links: [] });
  }

  async createTree(title: string): Promise<Tree> {
    return this.post('/api/v1/trees', { title, description: null, category: null, tags: [] });
  }

  async place(
    treeId: number,
    nodeId: number,
    positionX: number,
    positionY: number,
  ): Promise<TreeNode> {
    return this.post(`/api/v1/trees/${treeId}/nodes`, { nodeId, positionX, positionY });
  }

  async connect(
    treeId: number,
    prerequisite: TreeNode,
    dependent: TreeNode,
  ): Promise<Prerequisite> {
    return this.post(`/api/v1/trees/${treeId}/prerequisites`, {
      prerequisiteTreeNodeId: prerequisite.id,
      dependentTreeNodeId: dependent.id,
    });
  }

  /** Makes the node take its readiness from the tree (keeping its hand-entered value). */
  async link(node: Node, treeId: number): Promise<Node> {
    const response = await this.request.put(`/api/v1/nodes/${node.id}`, {
      data: {
        title: node.title,
        description: node.description,
        readiness: node.manualReadiness,
        links: node.links,
        linkedTreeId: treeId,
      },
    });
    expect(response.ok(), `link node ${node.id}: ${await response.text()}`).toBeTruthy();
    return response.json();
  }

  async treeNodes(treeId: number): Promise<TreeNode[]> {
    return this.get(`/api/v1/trees/${treeId}/nodes`);
  }

  async edges(treeId: number): Promise<Prerequisite[]> {
    return this.get(`/api/v1/trees/${treeId}/prerequisites`);
  }

  async nodes(): Promise<Node[]> {
    return this.get('/api/v1/nodes');
  }

  async trees(): Promise<Tree[]> {
    return this.get('/api/v1/trees');
  }

  private async get<T>(url: string): Promise<T> {
    const response = await this.request.get(url);
    expect(response.ok(), `GET ${url}`).toBeTruthy();
    return response.json();
  }

  private async post<T>(url: string, data: unknown): Promise<T> {
    const response = await this.request.post(url, { data });
    expect(response.ok(), `POST ${url}: ${await response.text()}`).toBeTruthy();
    return response.json();
  }
}

/** A tree node's box on the canvas, found by its title. */
export function nodeBox(page: Page, title: string): Locator {
  return page.locator('svg g.node', {
    has: page.locator('title', { hasText: new RegExp(`^${title}$`) }),
  });
}

/** The centre of an element on screen, for mouse moves. */
export async function centreOf(locator: Locator): Promise<{ x: number; y: number }> {
  const box = await locator.boundingBox();
  expect(box).not.toBeNull();
  return { x: box!.x + box!.width / 2, y: box!.y + box!.height / 2 };
}

export const test = base.extend<{ api: Api; unique: (label: string) => string }>({
  api: async ({ request }, use) => use(new Api(request)),
  /** Titles that can't clash with the sample data or with other tests running alongside. */
  unique: async ({}, use, testInfo) => {
    const suffix = `${testInfo.workerIndex}-${Date.now().toString(36)}`;
    await use((label) => `${label} ${suffix}`);
  },
  page: async ({ page }, use) => {
    // The app asks for confirmation before deleting or resetting; say yes
    page.on('dialog', (dialog) => dialog.accept());
    await use(page);
  },
});

export { expect };
