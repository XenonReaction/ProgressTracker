import { expect, nodeBox, test } from './fixtures';

test.describe('Readiness gradient', () => {
  test('shows how close each node is to ready, and moves up as readiness improves', async ({
    page,
    api,
    unique,
  }) => {
    // One dependent per level, each with two prerequisites; default thresholds are 80/70
    const tree = await api.createTree(unique('Gradient'));
    const cases = [
      { level: 'not started', readiness: [5, 10] },
      { level: 'early', readiness: [40, 60] },
      { level: 'close', readiness: [70, 75] },
      { level: 'ready', readiness: [90, 85] },
    ];
    const dependents: string[] = [];
    for (const [i, c] of cases.entries()) {
      const x = i * 450;
      const dependent = await api.place(
        tree.id,
        (await api.createNode(unique(`Needs ${i}`), 0)).id,
        x + 100,
        200,
      );
      for (const [j, readiness] of c.readiness.entries()) {
        const prerequisite = await api.place(
          tree.id,
          (await api.createNode(unique(`Part ${i}.${j}`), readiness)).id,
          x + j * 200,
          0,
        );
        await api.connect(tree.id, prerequisite, dependent);
      }
      dependents.push(dependent.title);
    }

    await page.goto(`/trees/${tree.id}`);
    for (const [i, c] of cases.entries()) {
      await expect(nodeBox(page, dependents[i])).toHaveAttribute(
        'aria-label',
        new RegExp(`, ${c.level}$`),
      );
      await expect(nodeBox(page, dependents[i])).toContainText(`0% · ${c.level}`);
    }
    // Each level has its own border style, not only its own colour
    const dashes = await page
      .locator('svg g.node')
      .filter({ has: page.locator('title', { hasText: /^Needs / }) })
      .locator('rect')
      .evaluateAll((rects) => rects.map((r) => getComputedStyle(r).strokeDasharray));
    expect(new Set(dashes).size).toBe(4);
    await expect(page.locator('.hint .legend')).toHaveText([
      'not started',
      'early',
      'close',
      'ready',
    ]);

    // Raising the weaker "early" prerequisite from 40 to 70 makes it close: min(65/80, 60/70) = 0.81
    await nodeBox(page, `Part 1.0 ${dependents[1].split(' ').at(-1)}`).click();
    await page.locator('aside.details').getByLabel('Readiness').fill('70');
    await page.locator('aside.details').getByRole('button', { name: 'Save' }).click();
    await expect(nodeBox(page, dependents[1])).toHaveAttribute('aria-label', /, close$/);
  });
});
