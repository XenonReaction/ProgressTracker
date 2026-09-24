import { Page } from '@playwright/test';

import { Api, centreOf, expect, nodeBox, test } from './fixtures';

/** A tree with a prerequisite edge: Basics (hand-entered 50) → Advanced. */
async function basicsToAdvanced(api: Api, unique: (label: string) => string) {
  const tree = await api.createTree(unique('Modes'));
  const basics = await api.place(tree.id, (await api.createNode(unique('Basics'), 50)).id, 0, 0);
  const advanced = await api.place(
    tree.id,
    (await api.createNode(unique('Advanced'), 0)).id,
    0,
    150,
  );
  await api.connect(tree.id, basics, advanced);
  return { tree, basics, advanced };
}

function editButton(page: Page) {
  return page.getByRole('button', { name: 'Edit', exact: true });
}

async function dragBy(page: Page, title: string, dx: number, dy: number): Promise<void> {
  const start = await centreOf(nodeBox(page, title));
  await page.mouse.move(start.x, start.y);
  await page.mouse.down();
  await page.mouse.move(start.x + dx, start.y + dy, { steps: 8 });
  await page.mouse.up();
}

test.describe('Tree view and edit modes', () => {
  test('opens in view mode, where only hand-entered readiness can change', async ({
    page,
    api,
    unique,
  }) => {
    const { tree, basics, advanced } = await basicsToAdvanced(api, unique);
    await page.goto(`/trees/${tree.id}`);

    await expect(editButton(page)).toBeVisible();
    await expect(page.getByRole('toolbar')).toHaveCount(0);
    await expect(nodeBox(page, advanced.title)).toHaveAttribute('aria-label', /, early$/);

    // Dragging does nothing in view mode
    await dragBy(page, basics.title, 100, 0);
    expect((await api.treeNodes(tree.id)).find((n) => n.id === basics.id)!.positionX).toBe(0);

    // Clicking shows the details, where readiness can be updated
    await nodeBox(page, basics.title).click();
    const details = page.locator('aside.details');
    await expect(details.getByRole('link', { name: 'Open node page' })).toBeVisible();
    await details.getByLabel('Readiness').fill('90');
    await details.getByRole('button', { name: 'Save' }).click();

    // Basics at 90 meets Advanced's 80/70 thresholds
    await expect(nodeBox(page, advanced.title)).toHaveAttribute('aria-label', /, ready$/);
  });

  test('"Discard changes" puts the tree back as it was when Edit was clicked', async ({
    page,
    api,
    unique,
  }) => {
    const { tree, basics, advanced } = await basicsToAdvanced(api, unique);
    const newTitle = unique('Made while editing');
    await page.goto(`/trees/${tree.id}`);
    await editButton(page).click();

    // Move a node, delete the arrow, and create a brand new node
    await dragBy(page, basics.title, 150, 0);
    await expect
      .poll(async () => (await api.treeNodes(tree.id)).find((n) => n.id === basics.id)!.positionX)
      .toBeGreaterThan(50);
    // The page has handled the saved move once it can be undone
    await expect(page.getByRole('button', { name: 'Undo' })).toBeEnabled();
    await page.getByRole('toolbar').getByRole('button', { name: 'Delete', exact: true }).click();
    // The new hint can reflow the page, so let it settle before aiming at the arrow
    await expect(page.getByText('click an arrow to remove that prerequisite')).toBeVisible();
    await page.locator('svg g.edge .edge-hit').click();
    await expect(page.locator('svg g.edge')).toHaveCount(0);
    await page.getByRole('toolbar').getByRole('button', { name: 'Add node', exact: true }).click();
    await page.getByRole('img', { name: /^Skill tree / }).click({ position: { x: 20, y: 20 } });
    await page.locator('app-add-node-panel').getByPlaceholder('Title').fill(newTitle);
    await page
      .locator('app-add-node-panel')
      .getByRole('button', { name: 'Create and place' })
      .click();
    await expect(nodeBox(page, newTitle)).toBeVisible();

    await page.getByRole('button', { name: 'Discard changes' }).click();

    await expect(editButton(page)).toBeVisible();
    await expect(page.locator('svg g.node')).toHaveCount(2);
    await expect(page.locator('svg g.edge')).toHaveCount(1);
    const restored = await api.treeNodes(tree.id);
    expect(restored.find((n) => n.nodeId === basics.nodeId)!.positionX).toBe(0);
    expect(restored.find((n) => n.nodeId === advanced.nodeId)!.prerequisiteIds).toHaveLength(1);
    // The node created while editing is gone from the library too
    expect((await api.nodes()).map((n) => n.title)).not.toContain(newTitle);
  });

  test('"Done" keeps the changes', async ({ page, api, unique }) => {
    const { tree, basics } = await basicsToAdvanced(api, unique);
    await page.goto(`/trees/${tree.id}`);
    await editButton(page).click();

    await dragBy(page, basics.title, 150, 0);
    await expect
      .poll(async () => (await api.treeNodes(tree.id)).find((n) => n.id === basics.id)!.positionX)
      .toBeGreaterThan(50);
    await page.getByRole('button', { name: 'Done' }).click();

    await expect(editButton(page)).toBeVisible();
    await page.reload();
    await expect(page.getByText("didn't finish")).toHaveCount(0);
    expect(
      (await api.treeNodes(tree.id)).find((n) => n.id === basics.id)!.positionX,
    ).toBeGreaterThan(50);
  });

  test('leaving while editing asks first; staying keeps editing and leaving discards', async ({
    page,
    api,
    unique,
    dialogs,
  }) => {
    const { tree, basics } = await basicsToAdvanced(api, unique);
    await page.goto(`/trees/${tree.id}`);
    await editButton(page).click();
    await dragBy(page, basics.title, 150, 0);
    await expect
      .poll(async () => (await api.treeNodes(tree.id)).find((n) => n.id === basics.id)!.positionX)
      .toBeGreaterThan(50);

    // "Cancel": stay and keep editing
    dialogs.accept = false;
    await page.getByRole('link', { name: 'Node library' }).click();
    await expect(page.getByRole('toolbar')).toBeVisible();
    expect(dialogs.messages.at(-1)).toContain('Leave without saving?');

    // "OK": leave, and the move is undone
    dialogs.accept = true;
    await page.getByRole('link', { name: 'Node library' }).click();
    await expect(page.getByRole('heading', { level: 1, name: 'Node library' })).toBeVisible();
    expect((await api.treeNodes(tree.id)).find((n) => n.id === basics.id)!.positionX).toBe(0);
    expect((await api.tree(tree.id)).editSessionStartedAt).toBeNull();
  });

  test('an edit session that did not finish is resolved on the next visit', async ({
    page,
    api,
    unique,
  }) => {
    // As if the browser closed mid-edit: a session with a change, and nobody on the page
    const { tree, basics } = await basicsToAdvanced(api, unique);
    await api.startEditSession(tree.id);
    await api.moveNode(tree.id, basics, 300, 0);

    await page.goto(`/trees/${tree.id}`);
    const banner = page.getByRole('region', { name: 'Unfinished edit session' });
    await expect(banner).toContainText("didn't finish");
    await expect(editButton(page)).toHaveCount(0);

    await banner.getByRole('button', { name: 'Discard changes' }).click();

    await expect(banner).toHaveCount(0);
    await expect(editButton(page)).toBeVisible();
    expect((await api.treeNodes(tree.id)).find((n) => n.id === basics.id)!.positionX).toBe(0);
  });

  test('tree details edited during a session come back on discard', async ({
    page,
    api,
    unique,
  }) => {
    const { tree } = await basicsToAdvanced(api, unique);
    await page.goto(`/trees/${tree.id}`);
    await editButton(page).click();

    await page.getByRole('link', { name: 'Edit details' }).click();
    await expect(page.getByLabel('Title')).toHaveValue(tree.title);
    await page.getByLabel('Title').fill(unique('Renamed'));
    await page.getByRole('button', { name: 'Save' }).click();

    // Back in the same edit session
    await expect(page.getByRole('toolbar')).toBeVisible();
    await page.getByRole('button', { name: 'Discard changes' }).click();
    await expect(page.getByRole('heading', { level: 1, name: tree.title })).toBeVisible();
  });
});
