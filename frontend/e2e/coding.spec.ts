import { expect, test } from './fixtures';

test.describe('Coding questions', () => {
  test('lists the sample question set with one of two questions solved', async ({ page }) => {
    await page.goto('/');
    await page.getByRole('link', { name: 'Coding' }).click();

    const row = page.getByRole('row', { name: /CSS Flexbox exercises/ });
    await expect(row).toContainText('1 of 2');
    await expect(row).toContainText('50%');
  });

  test('writes a question, keeps the solution hidden until asked, and marks it solved', async ({
    page,
    unique,
  }) => {
    const setTitle = unique('Grid exercises');

    await page.goto('/question-sets');
    await page.getByRole('link', { name: '+ New question set' }).click();
    await page.getByLabel('Title').fill(setTitle);
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.getByRole('heading', { level: 1, name: setTitle })).toBeVisible();

    await page.getByRole('link', { name: '+ New question' }).click();
    await page.getByLabel('Title').fill('Two columns');
    await page.getByLabel('Language').selectOption({ label: 'CSS' });
    await page.getByLabel('Problem (Markdown)').fill('Lay out `.page` as **two equal columns**.');
    await page
      .getByLabel('Worked examples (Markdown, optional)')
      .fill('```html\n<div class="page"></div>\n```');
    await page.getByLabel('Solution (hidden until asked for)').fill('.page {\n  display: grid;\n}');
    await page.getByRole('button', { name: 'Save' }).click();

    await expect(page.getByRole('row', { name: /Two columns/ })).toContainText('Not solved yet');
    await page.getByRole('link', { name: 'Two columns' }).click();
    await expect(page.locator('.markdown strong')).toHaveText('two equal columns');
    await expect(page.locator('.solution')).toHaveCount(0);

    // Seeing the solution first is recorded (the page accepts the confirmation)
    await page.getByRole('button', { name: 'Show solution' }).click();
    await expect(page.locator('.solution')).toContainText('display: grid;');
    await expect(page.getByText('CSS · Solution seen, not solved yet')).toBeVisible();

    await page.getByRole('button', { name: 'Mark solved' }).click();
    await expect(page.getByText('CSS · Solved (after seeing the solution)')).toBeVisible();

    await page.getByRole('link', { name: `← ${setTitle}` }).click();
    await expect(page.getByText('100%: 1 of 1 questions solved.')).toBeVisible();
  });

  test('marking questions solved changes the node that counts the set', async ({
    page,
    api,
    unique,
  }) => {
    const set = await api.createQuestionSet(unique('Flexbox exercises'));
    await api.createQuestion(set.id, 'Centre a box');
    await api.createQuestion(set.id, 'Spread a nav bar');
    const node = await api.createNode(unique('CSS Flexbox'), 0);

    // Attach it in the node's form
    await page.goto(`/nodes/${node.id}/edit`);
    await expect(page.getByLabel('Title')).toHaveValue(node.title);
    await page.getByRole('button', { name: '+ Add question set' }).click();
    await page.getByLabel('Resource 1 question set').selectOption({ label: set.title });
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.locator('.breakdown')).toHaveText(
      new RegExp(`Question set\\s+${set.title}\\s*: 0%\\s*→ 0%`),
    );

    await page.locator('.breakdown').getByRole('link', { name: set.title }).click();
    await page.getByRole('link', { name: 'Centre a box' }).click();
    await page.getByRole('button', { name: 'Mark solved' }).click();
    await expect(page.getByText('CSS · Solved')).toBeVisible();

    await page.goto(`/nodes/${node.id}`);
    await expect(page.locator('.breakdown')).toContainText('→ 50%');
  });

  test("a question set that a node lists can't be deleted", async ({ page, api, unique }) => {
    const set = await api.createQuestionSet(unique('Kept set'));
    const node = await api.setResources(await api.createNode(unique('Uses it'), 0), [
      { type: 'question_set', questionSetId: set.id, counts: false },
    ]);

    await page.goto(`/question-sets/${set.id}`);
    await page.getByRole('button', { name: 'Delete question set' }).click();

    await expect(page.getByRole('alert')).toContainText('is a resource of 1 node(s)');
    await expect(page.getByRole('alert').getByRole('link', { name: node.title })).toBeVisible();
  });
});
