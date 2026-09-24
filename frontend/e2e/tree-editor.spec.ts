import { Page } from '@playwright/test';

import { Api, centreOf, expect, nodeBox, test } from './fixtures';

/** A tree with the given library nodes placed at the given positions. */
async function treeWith(
  api: Api,
  title: string,
  nodes: { title: string; readiness?: number; x: number; y: number }[],
) {
  const tree = await api.createTree(title);
  const placed = [];
  for (const n of nodes) {
    const node = await api.createNode(n.title, n.readiness ?? 0);
    placed.push(await api.place(tree.id, node.id, n.x, n.y));
  }
  return { tree, placed };
}

/** Opens the tree and switches it to edit mode, where the tools are. */
async function openTree(page: Page, treeId: number): Promise<void> {
  await page.goto(`/trees/${treeId}`);
  await page.getByRole('button', { name: 'Edit', exact: true }).click();
  await expect(page.getByRole('toolbar', { name: 'Tree editing tools' })).toBeVisible();
}

function tool(page: Page, name: string) {
  return page.getByRole('toolbar').getByRole('button', { name, exact: true });
}

test.describe('Tree editor', () => {
  test('drags a node to a new position and saves it', async ({ page, api, unique }) => {
    const title = unique('Drag me');
    const { tree, placed } = await treeWith(api, unique('Drag tree'), [
      { title, x: 0, y: 0 },
      { title: unique('Anchor'), x: 400, y: 0 },
    ]);
    await openTree(page, tree.id);

    const start = await centreOf(nodeBox(page, title));
    await page.mouse.move(start.x, start.y);
    await page.mouse.down();
    await page.mouse.move(start.x + 40, start.y + 150, { steps: 10 });
    await page.mouse.up();

    // The move is saved straight away, and it's a move, not a selection
    await expect
      .poll(
        async () => (await api.treeNodes(tree.id)).find((n) => n.id === placed[0].id)!.positionY,
      )
      .toBeGreaterThan(100);
    await expect(page.locator('aside.details')).toHaveCount(0);

    await page.reload();
    const moved = await centreOf(nodeBox(page, title));
    const anchor = await centreOf(nodeBox(page, placed[1].title));
    expect(moved.y - anchor.y).toBeGreaterThan(100);
  });

  test('changes a node’s thresholds so it becomes ready', async ({ page, api, unique }) => {
    const { tree, placed } = await treeWith(api, unique('Threshold tree'), [
      { title: unique('Basics'), readiness: 50, x: 0, y: 0 },
      { title: unique('Advanced'), x: 0, y: 150 },
    ]);
    const [basics, advanced] = placed;
    await api.connect(tree.id, basics, advanced);
    await openTree(page, tree.id);

    const box = nodeBox(page, advanced.title);
    await expect(box).toHaveAttribute('aria-label', /, early$/);

    await box.click();
    const details = page.locator('aside.details');
    await expect(details.getByRole('heading', { name: advanced.title })).toBeVisible();
    await expect(details.getByRole('listitem')).toHaveText(`${basics.title} (50%)`);

    await details.getByLabel('average at least').fill('50');
    await details.getByLabel('and each is at least').fill('50');
    await details.getByRole('button', { name: 'Save thresholds' }).click();

    await expect(box).toHaveAttribute('aria-label', /, ready$/);
    const saved = (await api.treeNodes(tree.id)).find((n) => n.id === advanced.id)!;
    expect([saved.aggregateThreshold, saved.individualThreshold]).toEqual([50, 50]);
  });

  test('connects two nodes and refuses an edge that would make a cycle', async ({
    page,
    api,
    unique,
  }) => {
    const { tree, placed } = await treeWith(api, unique('Connect tree'), [
      { title: unique('First'), x: 0, y: 0 },
      { title: unique('Second'), x: 0, y: 150 },
    ]);
    const [first, second] = placed;
    await openTree(page, tree.id);

    await tool(page, 'Connect').click();
    await nodeBox(page, first.title).click();
    await expect(page.getByText('Now click the node that')).toBeVisible();
    await nodeBox(page, second.title).click();

    await expect(page.locator('svg g.edge')).toHaveCount(1);
    expect(await api.edges(tree.id)).toEqual([
      expect.objectContaining({ prerequisiteTreeNodeId: first.id, dependentTreeNodeId: second.id }),
    ]);

    // Second → First would close a loop
    await nodeBox(page, second.title).click();
    await nodeBox(page, first.title).click();

    await expect(page.getByRole('alert')).toContainText('cycle');
    await expect(page.locator('svg g.edge')).toHaveCount(1);
    expect(await api.edges(tree.id)).toHaveLength(1);
  });

  test('deletes an arrow, then removes a node from the tree', async ({ page, api, unique }) => {
    const { tree, placed } = await treeWith(api, unique('Delete tree'), [
      { title: unique('Keep'), x: 0, y: 0 },
      // Diagonal, because Playwright won't click a perfectly vertical (zero-width) line
      { title: unique('Remove'), x: 200, y: 200 },
    ]);
    const [keep, remove] = placed;
    await api.connect(tree.id, keep, remove);
    await openTree(page, tree.id);

    await tool(page, 'Delete').click();
    await page.locator('svg g.edge .edge-hit').click();
    await expect(page.locator('svg g.edge')).toHaveCount(0);
    expect(await api.edges(tree.id)).toHaveLength(0);

    await nodeBox(page, remove.title).click();
    await expect(nodeBox(page, remove.title)).toHaveCount(0);
    await expect(page.locator('svg g.node')).toHaveCount(1);

    // Removing it from the tree keeps it in the library
    expect((await api.nodes()).map((n) => n.title)).toContain(remove.title);
  });

  test('adds an existing library node and a brand new one', async ({ page, api, unique }) => {
    const { tree } = await treeWith(api, unique('Add tree'), [
      { title: unique('Already here'), x: 0, y: 0 },
    ]);
    const existing = await api.createNode(unique('From library'), 25);
    const newTitle = unique('Brand new');
    await openTree(page, tree.id);

    const canvas = page.getByRole('img', { name: /^Skill tree / });
    await tool(page, 'Add node').click();

    // The canvas has empty padding around the nodes, so its corner is free
    await canvas.click({ position: { x: 20, y: 20 } });
    const panel = page.locator('app-add-node-panel');
    await panel.getByLabel('From your library').selectOption({ label: `${existing.title} (25%)` });
    await panel.getByRole('button', { name: 'Place', exact: true }).click();
    await expect(nodeBox(page, existing.title)).toBeVisible();

    await canvas.click({ position: { x: 20, y: 20 } });
    await panel.getByPlaceholder('Title').fill(newTitle);
    await panel.getByRole('button', { name: 'Create and place' }).click();
    await expect(nodeBox(page, newTitle)).toBeVisible();

    await expect(page.locator('svg g.node')).toHaveCount(3);
    expect((await api.nodes()).map((n) => n.title)).toContain(newTitle);
  });

  test('resets the layout so prerequisites sit above what they unlock', async ({
    page,
    api,
    unique,
  }) => {
    // Placed upside down: the dependent is above its prerequisite
    const { tree, placed } = await treeWith(api, unique('Layout tree'), [
      { title: unique('Foundation'), x: 0, y: 300 },
      { title: unique('Built on it'), x: 0, y: 0 },
    ]);
    const [foundation, builtOnIt] = placed;
    await api.connect(tree.id, foundation, builtOnIt);
    await openTree(page, tree.id);

    await page.getByRole('button', { name: 'Reset to auto-layout' }).click();

    await expect
      .poll(async () => {
        const nodes = await api.treeNodes(tree.id);
        const y = (id: number) => nodes.find((n) => n.id === id)!.positionY;
        return y(builtOnIt.id) - y(foundation.id);
      })
      .toBeGreaterThan(0);
    await expect
      .poll(
        async () =>
          (await centreOf(nodeBox(page, builtOnIt.title))).y -
          (await centreOf(nodeBox(page, foundation.title))).y,
      )
      .toBeGreaterThan(0);
  });
});
