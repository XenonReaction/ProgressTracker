import { expect, test } from './fixtures';

test.describe('Flashcards', () => {
  test('lists the sample deck with how many of its cards have passed', async ({ page }) => {
    await page.goto('/');
    await page.getByRole('link', { name: 'Flashcards' }).click();

    const row = page.getByRole('row', { name: /CSS Flexbox/ });
    await expect(row).toContainText('1 of 5');
    await expect(row).toContainText('20%');
    await expect(row).not.toContainText('Complete');
  });

  test('creates a deck, adds and edits cards, studies it, then deletes it', async ({
    page,
    api,
    unique,
  }) => {
    const title = unique('HTML Forms');

    await page.goto('/decks');
    await page.getByRole('link', { name: '+ New deck' }).click();
    await page.getByLabel('Title').fill(title);
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.getByRole('heading', { level: 1, name: title })).toBeVisible();
    await expect(page.getByText('No cards yet.')).toBeVisible();

    await addCard(page, 'Which element groups form controls?', 'fieldset');
    await expect(page.getByRole('row', { name: /fieldset/ })).toContainText('Not answered yet');
    await addCard(page, 'Which attribute links a label to its input?', 'fr');
    await expect(page.getByText('0 of 2 cards passed.')).toBeVisible();

    // Fix the typo in place
    await page
      .getByRole('row', { name: /its input/ })
      .getByRole('button', { name: 'Edit' })
      .click();
    await page.getByRole('row').getByLabel('Back').fill('for');
    await page.getByRole('button', { name: 'Save card' }).click();
    await expect(page.getByRole('cell', { name: 'for', exact: true })).toBeVisible();

    // Study: the front first, then the answer, then your own verdict
    await page.getByRole('link', { name: 'Study' }).click();
    await expect(page.getByRole('heading', { level: 1, name: `Study: ${title}` })).toBeVisible();
    await expect(page.getByText('Card 1 of 2 · Not answered yet')).toBeVisible();
    await expect(page.getByRole('heading', { level: 2 })).toHaveText(
      'Which element groups form controls?',
    );
    await expect(page.getByLabel('Answer')).toHaveCount(0);
    await page.getByRole('button', { name: 'Show answer' }).click();
    await expect(page.getByLabel('Answer')).toHaveText('fieldset');
    await page.getByRole('button', { name: 'Correct' }).click();
    await expect(page.getByText('Card 2 of 2')).toBeVisible();
    await page.getByRole('button', { name: 'Show answer' }).click();
    await page.getByRole('button', { name: 'Wrong' }).click();
    await expect(page.getByText('You got 1 of 2 right.')).toBeVisible();

    await page.getByRole('link', { name: `← ${title}` }).click();
    await expect(page.getByRole('row', { name: /fieldset/ })).toContainText(
      '1 of 3 correct in a row',
    );
    await expect(page.getByRole('row', { name: /its input/ })).toContainText(
      '0 of 3 correct in a row',
    );
    await expect(page.getByText(/Last reviewed: (?!never)/)).toBeVisible();

    await page.getByRole('button', { name: 'Delete deck' }).click();
    await expect(page).toHaveURL(/\/decks$/);
    await expect(page.getByRole('row', { name: title })).toHaveCount(0);
  });

  test('a third correct answer in a row passes a card and completes its deck', async ({
    page,
    api,
    unique,
  }) => {
    const deck = await api.createDeck(unique('Semantic HTML'));
    const card = await api.createCard(deck.id, 'Which element holds the main content?', 'main');
    await api.answer(card, false, true, true);

    await page.goto(`/decks/${deck.id}/study`);
    await expect(page.getByText('Card 1 of 1 · 2 of 3 correct in a row')).toBeVisible();
    await page.getByRole('button', { name: 'Show answer' }).click();
    await page.getByRole('button', { name: 'Correct' }).click();
    await expect(page.getByText('1 card has now passed.')).toBeVisible();

    // Passed cards leave the study list; they can still be studied on purpose
    await page.getByRole('button', { name: 'Study again' }).click();
    await expect(page.getByText('Every card in this deck has passed.')).toBeVisible();
    await page.getByRole('button', { name: 'Study all cards anyway' }).click();
    await expect(page.getByText('Card 1 of 1 · Passed')).toBeVisible();

    await page.goto(`/decks/${deck.id}`);
    await expect(page.getByText('100%: 1 of 1 cards passed.')).toBeVisible();
    await expect(page.getByText('Complete: at least 80% of its cards have passed.')).toBeVisible();
    expect((await api.deck(deck.id)).complete).toBe(true);
  });

  test("the review page studies cards from every deck, naming each card's deck", async ({
    page,
  }) => {
    // Only looks: answering here would change the sample data other tests read
    await page.goto('/decks');
    await page.getByRole('link', { name: 'Review cards not yet passed' }).click();

    await expect(page.getByRole('heading', { level: 1, name: 'Review' })).toBeVisible();
    await expect(page.getByText(/^Card 1 of \d+ · .+ · /)).toBeVisible();
    await page.getByRole('button', { name: 'Show answer' }).click();
    await expect(page.getByRole('button', { name: 'Correct' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Wrong' })).toBeVisible();
  });
});

async function addCard(
  page: import('@playwright/test').Page,
  front: string,
  back: string,
): Promise<void> {
  const form = page.locator('form').last();
  await form.getByLabel('Front').fill(front);
  await form.getByLabel('Back').fill(back);
  await form.getByRole('button', { name: 'Add card' }).click();
  await expect(page.getByRole('cell', { name: front })).toBeVisible();
}
