import { expect, nodeBox, test } from './fixtures';

test.describe('Linked trees', () => {
  test('links a node to a tree in the form and shows the derived readiness', async ({
    page,
    api,
    unique,
  }) => {
    const detail = await api.createTree(unique('Detail'));
    for (const [title, readiness] of [
      ['Part A', 80],
      ['Part B', 60],
      ['Part C', 31],
    ] as const) {
      const node = await api.createNode(unique(title), readiness);
      await api.place(detail.id, node.id, 0, 0);
    }
    const overview = await api.createTree(unique('Overview'));
    const summary = await api.createNode(unique('Summary'), 25);
    await api.place(overview.id, summary.id, 0, 0);

    await page.goto(`/nodes/${summary.id}/edit`);
    await expect(page.getByLabel('Title')).toHaveValue(summary.title);
    await page.getByLabel('From a linked tree').check();
    await page.getByRole('combobox').selectOption({ label: detail.title });
    await page.getByRole('button', { name: 'Save' }).click();

    // (80 + 60 + 31) / 3 = 57
    const row = page.getByRole('row', { name: summary.title });
    await expect(row).toContainText(`57% from ${detail.title}`);

    await page.goto(`/trees/${overview.id}`);
    const box = nodeBox(page, summary.title);
    await expect(box).toContainText('linked');
    await expect(box).toHaveAttribute(
      'aria-label',
      new RegExp(`57% ready, from linked tree ${detail.title}`),
    );

    await box.click();
    await page.getByRole('link', { name: 'Open linked tree' }).click();
    await expect(page.getByRole('heading', { level: 1, name: detail.title })).toBeVisible();
    await expect(page.locator('svg g.node')).toHaveCount(3);

    // Unlinking brings back the hand-entered value
    await page.goto(`/nodes/${summary.id}/edit`);
    await expect(page.getByText('Your hand-entered value (25%) is kept')).toBeVisible();
    await page.getByLabel('Enter it myself').check();
    await expect(page.getByRole('spinbutton')).toHaveValue('25');
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.getByRole('row', { name: summary.title })).toContainText('25%');
  });

  test('readiness flows up through nested links', async ({ page, api, unique }) => {
    const inner = await api.createTree(unique('Inner'));
    await api.place(inner.id, (await api.createNode(unique('Deep'), 30)).id, 0, 0);
    const middle = await api.createTree(unique('Middle'));
    const nested = await api.link(await api.createNode(unique('Nested'), 99), inner.id);
    await api.place(middle.id, nested.id, 0, 0);
    await api.place(middle.id, (await api.createNode(unique('Solid'), 90)).id, 200, 0);
    const top = await api.link(await api.createNode(unique('Top'), 0), middle.id);

    await page.goto('/nodes');
    // (30 + 90) / 2 = 60: the nested node counts as 30, not its hidden 99
    await expect(page.getByRole('row', { name: top.title })).toContainText('60%');
  });

  test('refuses a link that would make readiness depend on itself', async ({
    page,
    api,
    unique,
  }) => {
    const tree = await api.createTree(unique('Self'));
    const node = await api.createNode(unique('Inside'), 10);
    await api.place(tree.id, node.id, 0, 0);

    await page.goto(`/nodes/${node.id}/edit`);
    await expect(page.getByLabel('Title')).toHaveValue(node.title);
    await page.getByLabel('From a linked tree').check();
    await page.getByRole('combobox').selectOption({ label: tree.title });
    await page.getByRole('button', { name: 'Save' }).click();

    await expect(page.getByRole('alert')).toContainText("because it's in that tree");
    await expect(page).toHaveURL(new RegExp(`/nodes/${node.id}/edit$`));
  });

  test('refuses to delete a tree that a node takes its readiness from', async ({
    page,
    api,
    unique,
  }) => {
    const tree = await api.createTree(unique('Linked to'));
    const node = await api.link(await api.createNode(unique('Linker'), 0), tree.id);

    await page.goto('/trees');
    await page
      .getByRole('row', { name: tree.title })
      .getByRole('button', { name: 'Delete' })
      .click();

    await expect(page.getByRole('alert')).toContainText(`"${node.title}"`);
    await expect(page.getByRole('row', { name: tree.title })).toHaveCount(1);
  });
});
