import { expect, nodeBox, test } from './fixtures';

test.describe('Decks as node resources', () => {
  test("studying a deck raises its node, the node's tree and a tree above it", async ({
    page,
    api,
    unique,
  }) => {
    const deck = await api.createDeck(unique('Flexbox cards'));
    const first = await api.createCard(deck.id, unique('Main axis?'), 'flex-direction');
    await api.createCard(deck.id, unique('Cross axis?'), 'align-items');
    await api.answer(first, true, true);
    const flexbox = await api.createNode(unique('CSS Flexbox'), 0);
    const css = await api.createTree(unique('CSS'));
    await api.place(css.id, flexbox.id, 0, 0);
    await api.place(css.id, (await api.createNode(unique('Selectors'), 50)).id, 200, 0);
    const frontEnd = await api.link(await api.createNode(unique('Front end'), 0), css.id);
    const web = await api.createTree(unique('Web'));
    await api.place(web.id, frontEnd.id, 0, 0);

    // Attach the deck in the node's form
    await page.goto(`/nodes/${flexbox.id}/edit`);
    await expect(page.getByLabel('Title')).toHaveValue(flexbox.title);
    await page.getByRole('button', { name: '+ Add deck' }).click();
    await page.getByLabel('Resource 1 deck').selectOption({ label: deck.title });
    await expect(page.getByLabel('Counts toward readiness')).toBeChecked();
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.locator('.breakdown')).toHaveText(
      new RegExp(`Deck\\s+${deck.title}\\s*: 0%\\s*→ 0%`),
    );
    // Two answers so far: none passed yet, but the deck has been reviewed
    await expect(page.getByText(/Last reviewed: (?!never)/)).toBeVisible();

    // A third right answer passes the first card: 1 of 2 cards
    await page.locator('.breakdown').getByRole('link', { name: deck.title }).click();
    await page.getByRole('link', { name: 'Study' }).click();
    // Never-answered cards come first: get that one wrong, then pass the first card
    await expect(page.getByText('Card 1 of 2 · Not answered yet')).toBeVisible();
    await page.getByRole('button', { name: 'Show answer' }).click();
    await page.getByRole('button', { name: 'Wrong' }).click();
    await expect(page.getByText('Card 2 of 2 · 2 of 3 correct in a row')).toBeVisible();
    await page.getByRole('button', { name: 'Show answer' }).click();
    await page.getByRole('button', { name: 'Correct' }).click();
    await expect(page.getByText('1 card has now passed.')).toBeVisible();

    await page.goto(`/nodes/${flexbox.id}`);
    await expect(page.locator('.breakdown')).toHaveText(
      new RegExp(`Deck\\s+${deck.title}\\s*: 50%\\s*→ 50%`),
    );
    await expect(page.getByText(/Last reviewed: (?!never)/)).toBeVisible();

    // CSS is (50 + 50) / 2 = 50%, and Front end and Web take it from there
    await page.goto('/trees');
    await expect(page.getByRole('row', { name: css.title })).toContainText('50%');
    await expect(page.getByRole('row', { name: web.title })).toContainText('50%');
    await expect(page.getByRole('row', { name: web.title })).not.toContainText('Never');
    await page.getByRole('link', { name: web.title }).click();
    await expect(page.locator('.tree-readiness')).toContainText('Readiness: 50%');
    await expect(nodeBox(page, frontEnd.title)).toHaveAttribute(
      'aria-label',
      new RegExp(`50% ready, from tree ${css.title}`),
    );
  });

  test("a deck that a node lists can't be deleted", async ({ page, api, unique }) => {
    const deck = await api.createDeck(unique('Kept deck'));
    const node = await api.setResources(await api.createNode(unique('Uses it'), 0), [
      { type: 'deck', deckId: deck.id, counts: false },
    ]);

    await page.goto(`/decks/${deck.id}`);
    await page.getByRole('button', { name: 'Delete deck' }).click();

    await expect(page.getByRole('alert')).toContainText('is a resource of 1 node(s)');
    await page.getByRole('alert').getByRole('link', { name: node.title }).click();
    await expect(page.getByRole('heading', { level: 1, name: node.title })).toBeVisible();
    await expect(page.getByRole('listitem').filter({ hasText: deck.title })).toContainText(
      'for reference',
    );
  });
});
