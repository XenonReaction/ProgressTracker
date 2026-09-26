import { renderMarkdown } from './markdown';

describe('renderMarkdown', () => {
  it('turns Markdown into HTML', () => {
    const html = renderMarkdown('Use `display: flex` for **flex items**:\n\n- row\n- column');

    expect(html).toContain('<code>display: flex</code>');
    expect(html).toContain('<strong>flex items</strong>');
    expect(html).toContain('<li>row</li>');
  });
});
