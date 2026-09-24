import { Page } from '@playwright/test';

import { Api, centreOf, expect, nodeBox, test } from './fixtures';

/** A tree with First → Second, laid out diagonally so the arrow is easy to click. */
async function twoNodeTree(api: Api, unique: (label: string) => string) {
  const tree = await api.createTree(unique('Undo'));
  const first = await api.place(tree.id, (await api.createNode(unique('First'), 90)).id, 0, 0);
  const second = await api.place(tree.id, (await api.createNode(unique('Second'), 0)).id, 200, 200);
  await api.connect(tree.id, first, second);
  return { tree, first, second };
}

async function edit(page: Page, treeId: number): Promise<void> {
  await page.goto(`/trees/${treeId}`);
  await page.getByRole('button', { name: 'Edit', exact: true }).click();
  await expect(page.getByRole('toolbar')).toBeVisible();
}

function toolbarButton(page: Page, name: string) {
  return page.getByRole('toolbar').getByRole('button', { name, exact: true });
}

test.describe('Undo and redo', () => {
  test('undoes and redoes a move with the buttons and the keyboard', async ({
    page,
    api,
    unique,
  }) => {
    const { tree, first } = await twoNodeTree(api, unique);
    const x = async () => (await api.treeNodes(tree.id)).find((n) => n.id === first.id)!.positionX;
    await edit(page, tree.id);

    const start = await centreOf(nodeBox(page, first.title));
    await page.mouse.move(start.x, start.y);
    await page.mouse.down();
    await page.mouse.move(start.x - 120, start.y, { steps: 8 });
    await page.mouse.up();
    await expect.poll(x).toBeLessThan(-50);

    // Each step waits for the page to settle too: a key pressed while an undo is still
    // being saved is ignored
    const undo = toolbarButton(page, 'Undo');
    const redo = toolbarButton(page, 'Redo');
    await expect(undo).toBeEnabled();
    await undo.click();
    await expect.poll(x).toBe(0);
    await expect(undo).toBeDisabled();
    await expect(redo).toBeEnabled();

    await page.keyboard.press('Control+y');
    await expect.poll(x).toBeLessThan(-50);
    await expect(undo).toBeEnabled();
    await expect(redo).toBeDisabled();
    await page.keyboard.press('Control+z');
    await expect.poll(x).toBe(0);
    await expect(redo).toBeEnabled();
    await page.keyboard.press('Control+Shift+z');
    await expect.poll(x).toBeLessThan(-50);
  });

  test('undoing a removal brings the node back with its arrow', async ({ page, api, unique }) => {
    const { tree, second } = await twoNodeTree(api, unique);
    await edit(page, tree.id);

    await toolbarButton(page, 'Delete').click();
    await nodeBox(page, second.title).click();
    await expect(page.locator('svg g.node')).toHaveCount(1);
    await expect(page.locator('svg g.edge')).toHaveCount(0);

    await toolbarButton(page, 'Undo').click();
    await expect(page.locator('svg g.node')).toHaveCount(2);
    await expect(page.locator('svg g.edge')).toHaveCount(1);

    // Redo works on the re-placed node, whose tree node id is new
    await toolbarButton(page, 'Redo').click();
    await expect(page.locator('svg g.node')).toHaveCount(1);
    expect(await api.edges(tree.id)).toHaveLength(0);
  });

  test('undoing a new arrow deletes it, and a new change clears redo', async ({
    page,
    api,
    unique,
  }) => {
    const { tree, first, second } = await twoNodeTree(api, unique);
    const third = await api.place(tree.id, (await api.createNode(unique('Third'), 0)).id, 400, 400);
    await edit(page, tree.id);

    await toolbarButton(page, 'Connect').click();
    await nodeBox(page, second.title).click();
    await nodeBox(page, third.title).click();
    await expect(page.locator('svg g.edge')).toHaveCount(2);

    await toolbarButton(page, 'Undo').click();
    await expect(page.locator('svg g.edge')).toHaveCount(1);
    await expect(toolbarButton(page, 'Redo')).toBeEnabled();

    await nodeBox(page, first.title).click();
    await nodeBox(page, third.title).click();
    await expect(page.locator('svg g.edge')).toHaveCount(2);
    await expect(toolbarButton(page, 'Redo')).toBeDisabled();
  });

  test('undoing a created node deletes it from the library; redo creates it again', async ({
    page,
    api,
    unique,
  }) => {
    const { tree } = await twoNodeTree(api, unique);
    const title = unique('Created');
    const inLibrary = async () => (await api.nodes()).some((n) => n.title === title);
    await edit(page, tree.id);

    await toolbarButton(page, 'Add node').click();
    await page.getByRole('img', { name: /^Skill tree / }).click({ position: { x: 20, y: 20 } });
    await page.locator('app-add-node-panel').getByPlaceholder('Title').fill(title);
    await page
      .locator('app-add-node-panel')
      .getByRole('button', { name: 'Create and place' })
      .click();
    await expect(nodeBox(page, title)).toBeVisible();

    await toolbarButton(page, 'Undo').click();
    await expect(nodeBox(page, title)).toHaveCount(0);
    await expect.poll(inLibrary).toBe(false);

    await toolbarButton(page, 'Redo').click();
    await expect(nodeBox(page, title)).toBeVisible();
    await expect.poll(inLibrary).toBe(true);
  });

  test('undoes auto-layout in one step', async ({ page, api, unique }) => {
    const { tree, first, second } = await twoNodeTree(api, unique);
    const positions = async () =>
      (await api.treeNodes(tree.id)).map((n) => [n.positionX, n.positionY]).sort();
    const before = [
      [first.positionX, first.positionY],
      [second.positionX, second.positionY],
    ].sort();
    await edit(page, tree.id);

    await toolbarButton(page, 'Reset to auto-layout').click();
    await expect.poll(positions).not.toEqual(before);

    await toolbarButton(page, 'Undo').click();
    await expect.poll(positions).toEqual(before);
  });
});
