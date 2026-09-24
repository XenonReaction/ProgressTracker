import { Locator, Page } from '@playwright/test';

import { Api, centreOf, expect, nodeBox, test } from './fixtures';

/** Upper (0, 0) → Lower (200, 200): a 3-segment edge with a horizontal middle. */
async function upperToLower(api: Api, unique: (label: string) => string) {
  const tree = await api.createTree(unique('Routes'));
  const upper = await api.place(tree.id, (await api.createNode(unique('Upper'), 50)).id, 0, 0);
  const lower = await api.place(tree.id, (await api.createNode(unique('Lower'), 0)).id, 200, 200);
  await api.connect(tree.id, upper, lower);
  return { tree, upper, lower };
}

async function edit(page: Page, treeId: number): Promise<void> {
  await page.goto(`/trees/${treeId}`);
  await page.getByRole('button', { name: 'Edit', exact: true }).click();
  await expect(page.getByRole('toolbar')).toBeVisible();
}

async function drag(page: Page, target: Locator, dx: number, dy: number): Promise<void> {
  const start = await centreOf(target);
  await page.mouse.move(start.x, start.y);
  await page.mouse.down();
  await page.mouse.move(start.x + dx, start.y + dy, { steps: 8 });
  await page.mouse.up();
}

/** Drags a segment and waits until the page has the saved route, so later steps see it. */
async function dragSegment(page: Page, dy: number): Promise<void> {
  const saved = page.waitForResponse(
    (r) => r.url().endsWith('/route') && r.request().method() === 'PUT',
  );
  await drag(page, page.locator('svg g.edge line.segment-handle'), 0, dy);
  await saved;
  await expect(page.getByRole('button', { name: 'Undo' })).toBeEnabled();
}

/** The edge's polyline points, as numbers. */
async function edgePoints(page: Page): Promise<number[][]> {
  const points = await page.locator('svg g.edge polyline.edge-line').first().getAttribute('points');
  return points!.split(' ').map((p) => p.split(',').map(Number));
}

test.describe('Right-angle edges', () => {
  test('the sample tree draws every arrow with only right angles', async ({ page }) => {
    await page.goto('/trees');
    await page.getByRole('link', { name: 'Java Fundamentals' }).click();

    const lines = page.locator('svg g.edge polyline.edge-line');
    await expect(lines).toHaveCount(5);
    for (const points of await lines.evaluateAll((els) =>
      els.map((el) => el.getAttribute('points')!),
    )) {
      const xy = points.split(' ').map((p) => p.split(',').map(Number));
      for (let i = 1; i < xy.length; i++) {
        const [[x1, y1], [x2, y2]] = [xy[i - 1], xy[i]];
        expect(x1 === x2 || y1 === y2).toBe(true);
      }
    }
  });

  test('a dragged segment keeps its place when a node moves, and resets when the shape changes', async ({
    page,
    api,
    unique,
  }) => {
    const { tree, lower } = await upperToLower(api, unique);
    const route = async () => (await api.edges(tree.id))[0].route;
    await edit(page, tree.id);

    // Drag the middle segment 30 units lower
    await dragSegment(page, 30);
    expect((await route())?.offsets[0]).toBeGreaterThan(20);
    const middleY = (await edgePoints(page))[1][1];

    // Moving the lower node sideways keeps the adjustment
    await drag(page, nodeBox(page, lower.title), 120, 0);
    await expect
      .poll(async () => (await api.treeNodes(tree.id)).find((n) => n.id === lower.id)!.positionX)
      .toBeGreaterThan(250);
    await expect(page.getByRole('button', { name: 'Undo' })).toBeEnabled();
    expect((await edgePoints(page))[1][1]).toBe(middleY);
    expect((await route())?.segments).toBe(3);

    // Moving it above the upper node needs 5 segments, so the route resets
    const reset = page.waitForResponse(
      (r) => r.url().endsWith('/route') && r.request().method() === 'PUT',
    );
    await drag(page, nodeBox(page, lower.title), 0, -300);
    await reset;
    expect(await route()).toBeNull();
    await expect.poll(async () => (await edgePoints(page)).length).toBe(6);

    // Undo puts back the move and the adjusted route together
    await page.getByRole('button', { name: 'Undo' }).click();
    await expect.poll(async () => (await route())?.segments).toBe(3);
    await expect.poll(async () => (await edgePoints(page))[1][1]).toBe(middleY);
  });

  test('auto-layout resets every route, and undo restores them', async ({ page, api, unique }) => {
    const { tree } = await upperToLower(api, unique);
    const route = async () => (await api.edges(tree.id))[0].route;
    await edit(page, tree.id);
    await dragSegment(page, 30);
    expect(await route()).not.toBeNull();

    await page.getByRole('button', { name: 'Reset to auto-layout' }).click();
    await expect.poll(route).toBeNull();

    await page.getByRole('button', { name: 'Undo' }).click();
    await expect.poll(route).not.toBeNull();
  });

  test('"Discard changes" puts routes back', async ({ page, api, unique }) => {
    const { tree } = await upperToLower(api, unique);
    const route = async () => (await api.edges(tree.id))[0].route;
    await edit(page, tree.id);

    await dragSegment(page, 30);
    expect(await route()).not.toBeNull();
    await page.getByRole('button', { name: 'Discard changes' }).click();

    await expect.poll(route).toBeNull();
    await expect(page.getByRole('button', { name: 'Edit', exact: true })).toBeVisible();
  });
});
