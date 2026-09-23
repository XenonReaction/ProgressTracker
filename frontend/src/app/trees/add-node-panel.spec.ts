import { ComponentFixture, TestBed } from '@angular/core/testing';

import { aNode } from '../core/test-data';
import { AddNodePanel } from './add-node-panel';

describe('AddNodePanel', () => {
  let fixture: ComponentFixture<AddNodePanel>;
  let page: HTMLElement;

  beforeEach(async () => {
    fixture = TestBed.createComponent(AddNodePanel);
    fixture.componentRef.setInput('point', { x: 120, y: -40 });
    fixture.componentRef.setInput('nodes', [aNode({ id: 4, title: 'Generics', readiness: 40 })]);
    page = fixture.nativeElement;
    await fixture.whenStable();
  });

  it('shows where the node will go', () => {
    expect(page.textContent).toContain('At (120, -40)');
  });

  it('places the chosen library node', async () => {
    const placed = vi.fn();
    fixture.componentInstance.placeExisting.subscribe(placed);
    const select = page.querySelector('select')!;

    button('Place').click();
    expect(placed).not.toHaveBeenCalled(); // nothing chosen yet

    select.value = '4';
    button('Place').click();
    expect(placed).toHaveBeenCalledWith(4);
  });

  it('creates a node from a trimmed title, ignoring blank titles', () => {
    const created = vi.fn();
    fixture.componentInstance.createNew.subscribe(created);
    const title = page.querySelector('input')!;
    const form = page.querySelector('form')!;

    title.value = '   ';
    form.dispatchEvent(new Event('submit'));
    expect(created).not.toHaveBeenCalled();

    title.value = '  Streams API ';
    form.dispatchEvent(new Event('submit'));
    expect(created).toHaveBeenCalledWith('Streams API');
  });

  it('explains when every library node is already in the tree', async () => {
    fixture.componentRef.setInput('nodes', []);
    await fixture.whenStable();
    expect(page.querySelector('select')).toBeNull();
    expect(page.textContent).toContain('Every library node is already in this tree');
  });

  it('can be dismissed', () => {
    const dismissed = vi.fn();
    fixture.componentInstance.dismiss.subscribe(dismissed);
    button('Cancel').click();
    expect(dismissed).toHaveBeenCalled();
  });

  function button(label: string): HTMLButtonElement {
    return Array.from(page.querySelectorAll('button')).find((b) => b.textContent?.trim() === label) as HTMLButtonElement;
  }
});
