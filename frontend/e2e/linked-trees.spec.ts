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
    await page.getByRole('button', { name: '+ Add tree' }).click();
    await page.getByLabel('Resource 1 tree').selectOption({ label: detail.title });
    await expect(page.getByLabel('Counts toward readiness')).toBeChecked();
    await page.getByRole('button', { name: 'Save' }).click();

    // (80 + 60 + 31) / 3 = 57
    await expect(
      page.getByText(`57%, the average of the nodes in the linked tree ${detail.title}`),
    ).toBeVisible();
    await page.goto('/nodes');
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

    // A tree that no longer counts is kept for reference, and the hand-entered value is back
    await page.goto(`/nodes/${summary.id}/edit`);
    await expect(page.getByText('Your hand-entered value (25%) is kept')).toBeVisible();
    await page.getByLabel('Counts toward readiness').uncheck();
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.getByText('25%', { exact: true })).toBeVisible();
    await expect(page.getByRole('listitem').filter({ hasText: detail.title })).toContainText(
      'for reference',
    );
  });

  test('a node averages every tree that counts, and lists its resources by type', async ({
    page,
    api,
    unique,
  }) => {
    const css = await api.createTree(unique('CSS'));
    await api.place(css.id, (await api.createNode(unique('Selectors'), 54)).id, 0, 0);
    const html = await api.createTree(unique('HTML'));
    await api.place(html.id, (await api.createNode(unique('Forms'), 100)).id, 0, 0);
    const reading = await api.createTree(unique('Reading'));
    const node = await api.createNode(unique('Front-end Basics'), 10);

    await page.goto(`/nodes/${node.id}/edit`);
    await expect(page.getByLabel('Title')).toHaveValue(node.title);
    await page.getByRole('button', { name: '+ Add link' }).click();
    await page.getByLabel('Resource 1 URL').fill('https://developer.mozilla.org/');
    await page.getByLabel('Resource 1 label').fill('MDN');
    // Rows 2 to 4, one tree each
    for (const [index, tree] of [css, reading, html].entries()) {
      await page.getByRole('button', { name: '+ Add tree' }).click();
      await page.getByLabel(`Resource ${index + 2} tree`).selectOption({ label: tree.title });
    }
    await page
      .getByRole('group', { name: 'Resource 3' })
      .getByLabel('Counts toward readiness')
      .uncheck();
    // The link goes last, one row at a time (waiting for each move to show)
    for (const from of [1, 2, 3]) {
      await page.getByRole('button', { name: `Move resource ${from} down` }).click();
      await expect(page.getByLabel(`Resource ${from + 1} URL`)).toHaveValue(
        'https://developer.mozilla.org/',
      );
    }
    await page.getByRole('button', { name: 'Save' }).click();

    // (54 + 100) / 2 = 77: the reading list doesn't count
    await expect(
      page.getByText(`77%, the average of the linked trees ${css.title} and ${html.title}.`),
    ).toBeVisible();
    await expect(page.getByRole('heading', { level: 3 })).toHaveText(['Trees', 'Links']);
    await expect(page.getByRole('listitem').filter({ hasText: reading.title })).toContainText(
      'for reference',
    );
    await expect(page.getByRole('link', { name: 'MDN' })).toHaveAttribute(
      'href',
      'https://developer.mozilla.org/',
    );
    const saved = (await api.nodes()).find((n) => n.id === node.id)!;
    expect(saved.resources.map((r) => r.type)).toEqual(['tree', 'tree', 'tree', 'url']);
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
    await page.getByRole('button', { name: '+ Add tree' }).click();
    await page.getByLabel('Resource 1 tree').selectOption({ label: tree.title });
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
