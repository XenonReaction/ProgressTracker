import { expect, test } from './fixtures';

test.describe('Node library', () => {
  test('creates a node, sets its readiness on its page, then edits it', async ({
    page,
    unique,
  }) => {
    const title = unique('Recursion');

    await page.goto('/nodes');
    await page.getByRole('link', { name: '+ New node' }).click();
    await page.getByLabel('Title').fill(title);
    await page.getByLabel('Description').fill('Functions that call themselves.');
    await page.getByLabel('Tags (comma-separated)').fill('algorithms, basics');
    await page.getByRole('button', { name: '+ Add link' }).click();
    await page.getByLabel('Link 1 URL').fill('https://example.com/recursion');
    await page.getByLabel('Link 1 label').fill('Notes');
    await page.getByRole('button', { name: 'Save' }).click();

    // Saving opens the node's page, where readiness is set
    await expect(page.getByRole('heading', { level: 1, name: title })).toBeVisible();
    await expect(page.getByText('Tags: algorithms, basics')).toBeVisible();
    await expect(page.getByRole('link', { name: 'Notes' })).toHaveAttribute(
      'href',
      'https://example.com/recursion',
    );
    await page.getByLabel('Readiness').fill('35');
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.getByText('35%', { exact: true })).toBeVisible();

    await page.getByRole('link', { name: 'Edit' }).click();
    await expect(page.getByLabel('Title')).toHaveValue(title);
    await expect(page.getByLabel('Link 1 URL')).toHaveValue('https://example.com/recursion');
    await page.getByLabel('Description').fill('A function that calls itself.');
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.getByText('A function that calls itself.')).toBeVisible();

    await page.getByRole('link', { name: '← Node library' }).click();
    const row = page.getByRole('row', { name: title });
    await expect(row).toContainText('35%');
    await expect(row.getByRole('cell').nth(2)).toHaveText('1');
  });

  test('refuses to save a node without a title', async ({ page }) => {
    await page.goto('/nodes/new');
    await page.getByLabel('Title').focus();
    await page.getByLabel('Description').focus();

    await expect(page.getByText('Title is required')).toBeVisible();
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page).toHaveURL(/\/nodes\/new$/);
  });

  test('deletes an unused node', async ({ page, api, unique }) => {
    const node = await api.createNode(unique('Unused'));

    await page.goto('/nodes');
    await page
      .getByRole('row', { name: node.title })
      .getByRole('button', { name: 'Delete' })
      .click();

    await expect(page.getByRole('row', { name: node.title })).toHaveCount(0);
    expect((await api.nodes()).map((n) => n.title)).not.toContain(node.title);
  });

  test('refuses to delete a node that a tree uses, and names the tree', async ({
    page,
    api,
    unique,
  }) => {
    const node = await api.createNode(unique('Used'));
    const tree = await api.createTree(unique('Uses it'));
    await api.place(tree.id, node.id, 0, 0);

    await page.goto('/nodes');
    await page
      .getByRole('row', { name: node.title })
      .getByRole('button', { name: 'Delete' })
      .click();

    const alert = page.getByRole('alert');
    await expect(alert).toBeVisible();
    await expect(alert.getByRole('link', { name: tree.title })).toBeVisible();
    await expect(page.getByRole('row', { name: node.title })).toHaveCount(1);

    await alert.getByRole('link', { name: tree.title }).click();
    await expect(page.getByRole('heading', { level: 1, name: tree.title })).toBeVisible();
  });
});
