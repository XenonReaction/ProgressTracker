import { Component, effect, inject, input, output, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';

import { Node } from '../core/api.models';
import { NodeApi } from '../core/node-api';
import { errorMessage } from '../core/problem';

/**
 * Updates a node's hand-entered readiness in place. It's the one change the view pages
 * allow, so readiness can be kept up to date while studying without entering edit mode.
 */
@Component({
  selector: 'app-readiness-editor',
  imports: [ReactiveFormsModule],
  template: `
    <form class="readiness-editor" (submit)="save($event)">
      <label>
        Readiness
        <input type="number" min="0" max="100" [formControl]="value" />%
      </label>
      <button type="submit" [disabled]="saving() || value.invalid || value.value === readiness()">Save</button>
      @if (value.invalid) {
        <br /><span class="error">Enter a whole number from 0 to 100.</span>
      }
      @if (error(); as message) {
        <br /><span class="error" role="alert">{{ message }}</span>
      }
    </form>
  `,
  styles: `
    input {
      width: 4rem;
    }
  `,
})
export class ReadinessEditor {
  private readonly nodeApi = inject(NodeApi);

  readonly nodeId = input.required<number>();
  /** The saved value; the input resets to it whenever it changes. */
  readonly readiness = input.required<number>();
  readonly saved = output<Node>();

  protected readonly value = new FormControl(0, {
    nonNullable: true,
    validators: [Validators.required, Validators.min(0), Validators.max(100), Validators.pattern(/^\d+$/)],
  });
  protected readonly saving = signal(false);
  protected readonly error = signal<string | null>(null);

  constructor() {
    effect(() => this.value.setValue(this.readiness()));
  }

  protected save(event: Event): void {
    event.preventDefault(); // a plain form, not a FormGroup, so stop the browser submitting it
    if (this.value.invalid) {
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    this.nodeApi.updateReadiness(this.nodeId(), this.value.value).subscribe({
      next: (node) => {
        this.saving.set(false);
        this.saved.emit(node);
      },
      error: (error) => {
        this.saving.set(false);
        this.error.set(errorMessage(error));
      },
    });
  }
}
