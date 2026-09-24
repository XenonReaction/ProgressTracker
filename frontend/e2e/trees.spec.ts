import { expect, nodeBox, test } from './fixtures';

test.describe('Trees', () => {
  test('shows the sample tree with its nodes and arrows', async ({ page }) => {
    await page.goto('/trees');
    await page.getByRole('link', { name: 'Java Fundamentals' }).click();

    await expect(page.getByRole('heading', { level: 1, name: 'Java Fundamentals' })).toBeVisible();
    await expect(page.locator('svg g.node')).toHaveCount(5);
    await expect(page.locator('svg g.edge')).toHaveCount(5);
    // Collections needs OOP (80%), which meets its default 80/70 thresholds. Streams needs
    // Collections (70%, from its linked tree) and Generics (40%) against 85/75: the weakest is
    // 40 / 75 = 0.53 of the way, so it's early
    await expect(nodeBox(page, 'Collections Framework')).toHaveAttribute('aria-label', /, ready$/);
    await expect(nodeBox(page, 'Streams API')).toHaveAttribute('aria-label', /, early$/);
  });

  test('creates a tree, edits its details, then deletes it', async ({ page, api, unique }) => {
    const title = unique('Algorithms');

    await page.goto('/trees');
    await page.getByRole('link', { name: '+ New tree' }).click();
    await page.getByLabel('Title').fill(title);
    await page.getByLabel('Description').fill('Sorting and searching.');
    await page.getByLabel('Category').fill('Computer science');
    await page.getByLabel('Tags (comma-separated)').fill('cs, interviews');
    await page.getByRole('button', { name: 'Save' }).click();

    // Saving opens the new, empty tree
    await expect(page.getByRole('heading', { level: 1, name: title })).toBeVisible();
    await expect(page.getByText('Category: Computer science · Tags: cs, interviews')).toBeVisible();

    // Details are edited from edit mode, which the form returns to
    await page.getByRole('button', { name: 'Edit', exact: true }).click();
    await page.getByRole('link', { name: 'Edit details' }).click();
    // Wait for the saved values to load, or they'd overwrite what's typed
    await expect(page.getByLabel('Category')).toHaveValue('Computer science');
    await page.getByLabel('Category').fill('Algorithms');
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.getByText('Category: Algorithms')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Done' })).toBeVisible();
    await page.getByRole('button', { name: 'Done' }).click();

    await page.getByRole('link', { name: 'Trees', exact: true }).click();
    await expect(page.getByRole('row', { name: title })).toContainText('cs, interviews');

    await page.getByRole('link', { name: title }).click();
    await page.getByRole('button', { name: 'Edit', exact: true }).click();
    await page.getByRole('button', { name: 'Delete tree' }).click();
    await expect(page).toHaveURL(/\/trees$/);
    await expect(page.getByRole('row', { name: title })).toHaveCount(0);
    expect((await api.trees()).map((t) => t.title)).not.toContain(title);
  });

  test('deleting a tree keeps its nodes in the library', async ({ page, api, unique }) => {
    const node = await api.createNode(unique('Kept'));
    const tree = await api.createTree(unique('Short-lived'));
    await api.place(tree.id, node.id, 0, 0);

    await page.goto('/trees');
    await page
      .getByRole('row', { name: tree.title })
      .getByRole('button', { name: 'Delete' })
      .click();
    await expect(page.getByRole('row', { name: tree.title })).toHaveCount(0);

    await page.getByRole('link', { name: 'Node library' }).click();
    await expect(page.getByRole('row', { name: node.title })).toBeVisible();
  });
});
