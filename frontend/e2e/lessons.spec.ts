import { expect, test } from './fixtures';

test.describe('Lessons', () => {
  test('lists the sample lesson with its progress, never opened', async ({ page }) => {
    await page.goto('/');
    await page.getByRole('link', { name: 'Lessons' }).click();

    const row = page.getByRole('row', { name: /Flexbox in Ten Minutes/ });
    await expect(row).toContainText('30%');
    await expect(row).toContainText('3');
  });

  test('writes a lesson in Markdown, reads it, and enters progress', async ({ page, unique }) => {
    const title = unique('Grid lesson');

    await page.goto('/lessons');
    await page.getByRole('link', { name: '+ New lesson' }).click();
    await page.getByLabel('Title').fill(title);
    await page.getByLabel('Section 1 heading').fill('Tracks');
    await page.getByLabel('Section 1 body (Markdown)').fill('Use `grid-template-columns`.');
    await page.getByRole('button', { name: '+ Add section' }).click();
    await page.getByLabel('Section 2 heading').fill('Areas');
    await page.getByLabel('Section 2 body (Markdown)').fill('Name them:\n\n- header\n- main');
    await page.getByRole('button', { name: 'Save' }).click();

    // Saving opens the lesson to read, which is a review
    await expect(page.getByRole('heading', { level: 1, name: title })).toBeVisible();
    await expect(page.getByRole('heading', { level: 2, name: 'Tracks' })).toBeVisible();
    await expect(page.locator('.markdown code')).toHaveText('grid-template-columns');
    await expect(page.locator('.markdown li')).toHaveText(['header', 'main']);
    await expect(page.getByText(/Last opened: (?!never)/)).toBeVisible();

    await page.getByLabel('How far through are you?').fill('70');
    await page.getByRole('button', { name: 'Save progress' }).click();
    await expect(page.getByText('70% (self-reported)')).toBeVisible();

    await page.getByRole('link', { name: '← Lessons' }).click();
    const row = page.getByRole('row', { name: new RegExp(title) });
    await expect(row).toContainText('70%');
    await expect(row).not.toContainText('Never');
  });

  test('a lesson counts toward its node, and opening it is when it was last reviewed', async ({
    page,
    api,
    unique,
  }) => {
    const lesson = await api.createLesson(unique('Flexbox lesson'), [
      { title: 'Intro', body: 'Text' },
    ]);
    await api.enterLessonProgress(lesson.id, 100);
    const node = await api.setResources(await api.createNode(unique('Front-end Basics'), 0), [
      { type: 'lesson', lessonId: lesson.id, counts: true },
    ]);

    await page.goto(`/nodes/${node.id}`);
    await expect(page.locator('.breakdown')).toHaveText(
      new RegExp(`Lesson\\s+${lesson.title}\\s*: 100% \\(self-reported\\)\\s*→ 100%`),
    );
    await expect(page.getByText('Last reviewed: never')).toBeVisible();

    await page.locator('.breakdown').getByRole('link', { name: lesson.title }).click();
    await expect(page.getByRole('heading', { level: 1, name: lesson.title })).toBeVisible();

    await page.goto(`/nodes/${node.id}`);
    await expect(page.getByText(/Last reviewed: (?!never)/)).toBeVisible();
  });

  test("a lesson that a node lists can't be deleted", async ({ page, api, unique }) => {
    const lesson = await api.createLesson(unique('Kept lesson'), []);
    const node = await api.setResources(await api.createNode(unique('Uses it'), 0), [
      { type: 'lesson', lessonId: lesson.id, counts: false },
    ]);

    await page.goto(`/lessons/${lesson.id}`);
    await page.getByRole('button', { name: 'Delete lesson' }).click();

    await expect(page.getByRole('alert')).toContainText('is a resource of 1 node(s)');
    await expect(page.getByRole('alert').getByRole('link', { name: node.title })).toBeVisible();
  });
});
