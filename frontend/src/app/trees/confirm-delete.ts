/** Asks before deleting a tree, spelling out what goes with it and what stays. */
export function confirmTreeDelete(title: string): boolean {
  return confirm(
    `Delete the tree "${title}"?\n\n` +
      'Its node placements and prerequisite edges will be removed too. ' +
      'The nodes themselves stay in your library.',
  );
}
