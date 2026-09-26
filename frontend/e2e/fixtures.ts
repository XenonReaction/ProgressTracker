import { APIRequestContext, Locator, Page, test as base, expect } from '@playwright/test';

import type {
  Card,
  Deck,
  Lesson,
  Material,
  Node,
  NodeResourceRequest,
  Prerequisite,
  Tree,
  TreeNode,
} from '../src/app/core/api.models';

/**
 * Sets up test data through the REST API, so each test starts from exactly the state it
 * needs without clicking through other pages first.
 */
export class Api {
  constructor(private readonly request: APIRequestContext) {}

  async createNode(title: string, readiness = 0): Promise<Node> {
    return this.post('/api/v1/nodes', { title, description: null, readiness, resources: [] });
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
    return this.setResources(node, [{ type: 'tree', treeId, counts: true }]);
  }

  /** Replaces the node's resources. */
  async setResources(node: Node, resources: NodeResourceRequest[]): Promise<Node> {
    const response = await this.request.put(`/api/v1/nodes/${node.id}`, {
      data: {
        title: node.title,
        description: node.description,
        readiness: node.manualReadiness,
        resources,
        tags: node.tags,
      },
    });
    expect(
      response.ok(),
      `set resources of node ${node.id}: ${await response.text()}`,
    ).toBeTruthy();
    return response.json();
  }

  /** Puts the tree in edit mode, as clicking "Edit" does. */
  async startEditSession(treeId: number): Promise<void> {
    await this.post(`/api/v1/trees/${treeId}/edit-session`, null);
  }

  async moveNode(
    treeId: number,
    treeNode: TreeNode,
    positionX: number,
    positionY: number,
  ): Promise<void> {
    const response = await this.request.put(`/api/v1/trees/${treeId}/nodes/${treeNode.id}`, {
      data: {
        positionX,
        positionY,
        aggregateThreshold: treeNode.aggregateThreshold,
        individualThreshold: treeNode.individualThreshold,
      },
    });
    expect(response.ok(), `move tree node ${treeNode.id}`).toBeTruthy();
  }

  async createDeck(title: string): Promise<Deck> {
    return this.post('/api/v1/decks', { title, description: null });
  }

  async createCard(deckId: number, front: string, back: string): Promise<Card> {
    return this.post(`/api/v1/decks/${deckId}/cards`, { front, back });
  }

  /** Records answers to the card, one per verdict, in order. */
  async answer(card: Card, ...correct: boolean[]): Promise<void> {
    for (const verdict of correct) {
      await this.post(`/api/v1/decks/${card.deckId}/cards/${card.id}/reviews`, {
        correct: verdict,
      });
    }
  }

  async createMaterial(title: string, url = 'https://example.com/material'): Promise<Material> {
    return this.post('/api/v1/materials', { title, url, notes: null });
  }

  /** Reports progress on a material, as the material's page does. */
  async reportProgress(
    materialId: number,
    progress: number,
    note: string | null = null,
  ): Promise<Material> {
    const response = await this.request.post(`/api/v1/materials/${materialId}/progress`, {
      data: { progress, note },
    });
    expect(response.ok(), `report progress on material ${materialId}`).toBeTruthy();
    return response.json();
  }

  async createLesson(title: string, sections: { title: string; body: string }[]): Promise<Lesson> {
    return this.post('/api/v1/lessons', { title, summary: null, sections });
  }

  async enterLessonProgress(lessonId: number, progress: number): Promise<Lesson> {
    return this.post(`/api/v1/lessons/${lessonId}/progress`, { progress });
  }

  async deck(deckId: number): Promise<Deck> {
    return this.get(`/api/v1/decks/${deckId}`);
  }

  async tree(treeId: number): Promise<Tree> {
    return this.get(`/api/v1/trees/${treeId}`);
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

/** How the next `confirm()` dialogs are answered; tests can switch to "Cancel". */
export interface Dialogs {
  accept: boolean;
  /** The messages of every dialog shown so far. */
  messages: string[];
}

export const test = base.extend<{ api: Api; unique: (label: string) => string; dialogs: Dialogs }>({
  api: async ({ request }, use) => use(new Api(request)),
  /** Titles that can't clash with the sample data or with other tests running alongside. */
  unique: async ({}, use, testInfo) => {
    const suffix = `${testInfo.workerIndex}-${Date.now().toString(36)}`;
    await use((label) => `${label} ${suffix}`);
  },
  dialogs: async ({}, use) => use({ accept: true, messages: [] }),
  page: async ({ page, dialogs }, use) => {
    // The app asks for confirmation before deleting, resetting or leaving edit mode; say yes
    // unless a test asks otherwise
    page.on('dialog', (dialog) => {
      dialogs.messages.push(dialog.message());
      return dialogs.accept ? dialog.accept() : dialog.dismiss();
    });
    await use(page);
  },
});

export { expect };
