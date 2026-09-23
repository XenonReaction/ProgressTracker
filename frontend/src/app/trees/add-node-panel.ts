import { Component, input, output } from '@angular/core';

import { Node } from '../core/api.models';
import { Point } from './tree-layout';

/**
 * Shown after clicking the canvas with the "Add node" tool: place an existing library node
 * at that spot, or create a new library node and place it in one step.
 */
@Component({
  selector: 'app-add-node-panel',
  templateUrl: './add-node-panel.html',
})
export class AddNodePanel {
  readonly point = input.required<Point>();
  /** Library nodes not already in the tree. */
  readonly nodes = input.required<Node[]>();

  readonly placeExisting = output<number>();
  readonly createNew = output<string>();
  readonly dismiss = output<void>();

  protected place(nodeId: string): void {
    if (nodeId) {
      this.placeExisting.emit(Number(nodeId));
    }
  }

  protected create(event: Event, title: string): void {
    event.preventDefault();
    const trimmed = title.trim();
    if (trimmed) {
      this.createNew.emit(trimmed);
    }
  }
}
