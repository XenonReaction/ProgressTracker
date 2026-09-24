import { computed, signal } from '@angular/core';
import { Observable } from 'rxjs';

/** One change made in the tree editor, which can be taken back and made again. */
export interface EditCommand {
  /** What the change was, e.g. `move "OOP"`, for the Undo and Redo button tooltips. */
  readonly label: string;
  undo(): Observable<unknown>;
  redo(): Observable<unknown>;
}

/** How many changes can be undone (5.3-V13). */
export const UNDO_LIMIT = 50;

/**
 * The undo and redo stacks for one edit session. Recording a new change clears the redo
 * steps, as in most programs, and only the latest {@link UNDO_LIMIT} changes are kept.
 */
export class UndoHistory {
  private readonly done = signal<EditCommand[]>([]);
  private readonly undone = signal<EditCommand[]>([]);

  readonly nextUndo = computed(() => this.done().at(-1) ?? null);
  readonly nextRedo = computed(() => this.undone().at(-1) ?? null);

  constructor(private readonly limit = UNDO_LIMIT) {}

  record(command: EditCommand): void {
    this.done.update((done) => [...done, command].slice(-this.limit));
    this.undone.set([]);
  }

  /** Moves the latest change to the redo stack, once its undo has been saved. */
  undid(command: EditCommand): void {
    this.done.update((done) => done.filter((c) => c !== command));
    this.undone.update((undone) => [...undone, command]);
  }

  /** Moves the latest undone change back to the undo stack, once its redo has been saved. */
  redid(command: EditCommand): void {
    this.undone.update((undone) => undone.filter((c) => c !== command));
    this.done.update((done) => [...done, command].slice(-this.limit));
  }

  clear(): void {
    this.done.set([]);
    this.undone.set([]);
  }
}
