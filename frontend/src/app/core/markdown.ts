import { marked } from 'marked';

/**
 * Markdown (a lesson section, a coding question) as HTML. Bind it with `[innerHTML]`, which
 * runs Angular's sanitizer, so scripts and event handlers in the Markdown are stripped.
 */
export function renderMarkdown(markdown: string): string {
  return marked.parse(markdown, { async: false, gfm: true, breaks: false });
}
