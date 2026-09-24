import { of } from 'rxjs';

import { EditCommand, UNDO_LIMIT, UndoHistory } from './undo-history';

describe('UndoHistory', () => {
  const command = (label: string): EditCommand => ({ label, undo: () => of(null), redo: () => of(null) });

  it('undoes the latest change first and can redo it', () => {
    const history = new UndoHistory();
    const first = command('first');
    const second = command('second');
    history.record(first);
    history.record(second);

    expect(history.nextUndo()).toBe(second);
    history.undid(second);
    expect(history.nextUndo()).toBe(first);
    expect(history.nextRedo()).toBe(second);

    history.redid(second);
    expect(history.nextUndo()).toBe(second);
    expect(history.nextRedo()).toBeNull();
  });

  it('clears the redo steps when a new change is made after undoing', () => {
    const history = new UndoHistory();
    const first = command('first');
    history.record(first);
    history.undid(first);

    history.record(command('new'));

    expect(history.nextRedo()).toBeNull();
    expect(history.nextUndo()?.label).toBe('new');
  });

  it(`keeps only the latest ${UNDO_LIMIT} changes`, () => {
    const history = new UndoHistory();
    for (let i = 1; i <= UNDO_LIMIT + 5; i++) {
      history.record(command(`change ${i}`));
    }

    let undone = 0;
    for (let next = history.nextUndo(); next; next = history.nextUndo()) {
      history.undid(next);
      undone++;
    }
    expect(undone).toBe(UNDO_LIMIT);
    expect(history.nextRedo()?.label).toBe('change 6');
  });

  it('clears everything', () => {
    const history = new UndoHistory();
    history.record(command('a'));
    history.clear();
    expect(history.nextUndo()).toBeNull();
    expect(history.nextRedo()).toBeNull();
  });
});
