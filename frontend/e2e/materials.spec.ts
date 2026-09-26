import { expect, test } from './fixtures';

test.describe('Materials', () => {
  test('lists the sample material with the progress last reported', async ({ page }) => {
    await page.goto('/');
    await page.getByRole('link', { name: 'Materials' }).click();

    await expect(page.getByRole('row', { name: /A Complete Guide to Flexbox/ })).toContainText(
      '60%',
    );
  });

  test('creates a material, reports progress twice and keeps both reports', async ({
    page,
    unique,
  }) => {
    const title = unique('Grid guide');

    await page.goto('/materials');
    await page.getByRole('link', { name: '+ New material' }).click();
    await page.getByLabel('Title').fill(title);
    await page.getByLabel('URL').fill('https://css-tricks.com/snippets/css/complete-guide-grid/');
    await page.getByRole('button', { name: 'Save' }).click();

    await expect(page.getByRole('heading', { level: 1, name: title })).toBeVisible();
    await expect(page.getByText('No progress reported yet.')).toBeVisible();

    await page.getByLabel('How far through are you now?').fill('40');
    await page.getByLabel('What you covered (optional)').fill('The container');
    await page.getByRole('button', { name: 'Save progress' }).click();
    await expect(page.getByText('40% (self-reported)')).toBeVisible();

    // The form starts from the last report
    await expect(page.getByLabel('How far through are you now?')).toHaveValue('40');
    await page.getByLabel('How far through are you now?').fill('60');
    await page.getByRole('button', { name: 'Save progress' }).click();
    await expect(page.getByText('60% (self-reported)')).toBeVisible();

    const history = page.getByRole('row').filter({ hasText: '%' });
    await expect(history).toHaveCount(2);
    await expect(history.nth(0)).toContainText('60%');
    await expect(history.nth(1)).toContainText('40%');
    await expect(history.nth(1)).toContainText('The container');
  });

  test('reporting progress changes the node and tree that count the material', async ({
    page,
    api,
    unique,
  }) => {
    const material = await api.createMaterial(unique('Flexbox article'));
    await api.reportProgress(material.id, 40);
    const node = await api.createNode(unique('CSS Flexbox'), 0);
    const tree = await api.createTree(unique('CSS'));
    await api.place(tree.id, node.id, 0, 0);
    await api.place(tree.id, (await api.createNode(unique('Selectors'), 80)).id, 200, 0);

    // Attach it in the node's form
    await page.goto(`/nodes/${node.id}/edit`);
    await expect(page.getByLabel('Title')).toHaveValue(node.title);
    await page.getByRole('button', { name: '+ Add material' }).click();
    await page.getByLabel('Resource 1 material').selectOption({ label: material.title });
    await expect(page.getByLabel('Counts toward readiness')).toBeChecked();
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.locator('.breakdown')).toHaveText(
      new RegExp(`Material\\s+${material.title}\\s*: 40% \\(self-reported\\)\\s*→ 40%`),
    );

    // Report more progress from the material's page
    await page.locator('.breakdown').getByRole('link', { name: material.title }).click();
    await page.getByLabel('How far through are you now?').fill('100');
    await page.getByRole('button', { name: 'Save progress' }).click();
    await expect(page.getByText('100% (self-reported)')).toBeVisible();

    await page.goto(`/nodes/${node.id}`);
    await expect(page.locator('.breakdown')).toContainText('→ 100%');
    await page.goto('/trees');
    // (100 + 80) / 2
    await expect(page.getByRole('row', { name: tree.title })).toContainText('90%');
  });

  test("a material that a node lists can't be deleted", async ({ page, api, unique }) => {
    const material = await api.createMaterial(unique('Kept material'));
    const node = await api.setResources(await api.createNode(unique('Uses it'), 0), [
      { type: 'material', materialId: material.id, counts: false },
    ]);

    await page.goto(`/materials/${material.id}`);
    await page.getByRole('button', { name: 'Delete material' }).click();

    await expect(page.getByRole('alert')).toContainText('is a resource of 1 node(s)');
    await expect(page.getByRole('alert').getByRole('link', { name: node.title })).toBeVisible();
  });
});
