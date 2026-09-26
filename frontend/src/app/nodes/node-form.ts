import { Component, OnInit, computed, inject, input, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  AbstractControl,
  FormArray,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import {
  DeckRef,
  MaterialRef,
  NodeRequest,
  NodeResource,
  NodeResourceType,
  TreeRef,
} from '../core/api.models';
import { FlashcardApi } from '../core/flashcard-api';
import { MaterialApi } from '../core/material-api';
import { NodeApi } from '../core/node-api';
import { errorMessage } from '../core/problem';
import { TreeApi } from '../core/tree-api';
import { MAX_TAG_LENGTH, parseTags, tagsValidator } from '../trees/tags';

type ResourceGroup = FormGroup<{
  type: FormControl<NodeResourceType>;
  url: FormControl<string>;
  treeId: FormControl<number | null>;
  deckId: FormControl<number | null>;
  materialId: FormControl<number | null>;
  label: FormControl<string>;
  counts: FormControl<boolean>;
}>;

const HTTP_URL = /^https?:\/\/\S+$/i;

/** A link needs an http(s) URL; a tree, deck or material needs one chosen. */
function resourceTarget(group: AbstractControl): ValidationErrors | null {
  const { type, url, treeId, deckId, materialId } = group.value;
  if (type === 'url') {
    return HTTP_URL.test((url ?? '').trim()) ? null : { url: true };
  }
  if (type === 'deck') {
    return deckId == null ? { deckRequired: true } : null;
  }
  if (type === 'material') {
    return materialId == null ? { materialRequired: true } : null;
  }
  return treeId == null ? { treeRequired: true } : null;
}

/** The id a tree, deck or material row points to. */
function targetOf(row: {
  type?: NodeResourceType;
  treeId?: number | null;
  deckId?: number | null;
  materialId?: number | null;
}): number | null | undefined {
  return row.type === 'tree' ? row.treeId : row.type === 'deck' ? row.deckId : row.materialId;
}

/**
 * Create (`/nodes/new`) or edit (`/nodes/:id/edit`) a library node: its title, description,
 * tags and resources. Resources are links, trees, flashcard decks and materials, in the order
 * the user sets; any but a link can count toward readiness, and then the node's readiness is the
 * average of the resources that count. The hand-entered value itself is set on the node's view page, so this form only
 * carries it through unchanged.
 */
@Component({
  selector: 'app-node-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './node-form.html',
})
export class NodeForm implements OnInit {
  private readonly nodeApi = inject(NodeApi);
  private readonly treeApi = inject(TreeApi);
  private readonly flashcardApi = inject(FlashcardApi);
  private readonly materialApi = inject(MaterialApi);
  private readonly router = inject(Router);

  /** Route param; absent when creating. */
  readonly id = input<string>();
  /** Query param: where to go after saving, e.g. back to the tree the user came from. */
  readonly returnTo = input<string>();

  protected readonly maxTagLength = MAX_TAG_LENGTH;

  protected readonly form = new FormGroup({
    title: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(200)],
    }),
    description: new FormControl('', { nonNullable: true }),
    readiness: new FormControl(0, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0), Validators.max(100)],
    }),
    resources: new FormArray<ResourceGroup>([]),
    /** Comma-separated in the form; sent to the API as a list. */
    tags: new FormControl('', { nonNullable: true, validators: [tagsValidator] }),
  });

  protected readonly error = signal<string | null>(null);
  protected readonly saving = signal(false);
  /** Trees to choose from; loaded the first time a tree resource is shown. */
  protected readonly trees = signal<TreeRef[] | null>(null);
  private treesRequested = false;
  /** Decks to choose from; loaded the first time a deck resource is shown. */
  protected readonly decks = signal<DeckRef[] | null>(null);
  private decksRequested = false;
  /** Materials to choose from; loaded the first time a material resource is shown. */
  protected readonly materials = signal<MaterialRef[] | null>(null);
  private materialsRequested = false;
  /** The node's readiness as last saved, and which resources counted then. */
  private readonly saved = signal<{ countingKey: string; readiness: number } | null>(null);

  private readonly formValue = toSignal(this.form.valueChanges, {
    initialValue: this.form.getRawValue(),
  });
  /** The trees, decks and materials that count, as the form stands. */
  private readonly countingKey = computed(() =>
    (this.formValue().resources ?? [])
      .filter((r) => r.type !== 'url' && r.counts && targetOf(r) != null)
      .map((r) => `${r.type}:${targetOf(r)}`)
      .join(','),
  );
  protected readonly anyCounts = computed(() => this.countingKey() !== '');
  /** The saved readiness, while the resources that count are the ones it was worked out from. */
  protected readonly currentReadiness = computed(() => {
    const saved = this.saved();
    return saved && saved.countingKey === this.countingKey() ? saved.readiness : null;
  });

  ngOnInit(): void {
    const id = this.id();
    if (id) {
      this.nodeApi.get(Number(id)).subscribe({
        next: (node) => {
          this.form.patchValue({
            title: node.title,
            description: node.description ?? '',
            readiness: node.manualReadiness,
            tags: node.tags.join(', '),
          });
          node.resources.forEach((resource) => this.addResource(resource.type, resource));
          this.saved.set({ countingKey: this.countingKey(), readiness: node.readiness });
        },
        error: (error) => this.error.set(errorMessage(error)),
      });
    }
  }

  protected get resources(): FormArray<ResourceGroup> {
    return this.form.controls.resources;
  }

  protected addResource(type: NodeResourceType, resource?: NodeResource): void {
    this.resources.push(
      new FormGroup(
        {
          type: new FormControl(type, { nonNullable: true }),
          url: new FormControl(resource?.url ?? '', { nonNullable: true }),
          treeId: new FormControl<number | null>(resource?.tree?.id ?? null),
          deckId: new FormControl<number | null>(resource?.deck?.id ?? null),
          materialId: new FormControl<number | null>(resource?.material?.id ?? null),
          label: new FormControl(resource?.label ?? '', {
            nonNullable: true,
            validators: [Validators.maxLength(200)],
          }),
          // A new tree, deck or material counts unless the user says otherwise; a link never does
          counts: new FormControl(resource ? resource.counts : type !== 'url', {
            nonNullable: true,
          }),
        },
        { validators: resourceTarget },
      ),
    );
    if (type === 'tree') {
      this.loadTrees();
    }
    if (type === 'deck') {
      this.loadDecks();
    }
    if (type === 'material') {
      this.loadMaterials();
    }
  }

  protected removeResource(index: number): void {
    this.resources.removeAt(index);
  }

  /** Swaps the resource with its neighbour, `by` -1 (up) or +1 (down). */
  protected move(index: number, by: -1 | 1): void {
    const other = index + by;
    if (other < 0 || other >= this.resources.length) {
      return;
    }
    const control = this.resources.at(index);
    this.resources.removeAt(index);
    this.resources.insert(other, control);
  }

  protected save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const value = this.form.getRawValue();
    const request: NodeRequest = {
      title: value.title.trim(),
      description: value.description.trim() || null,
      readiness: value.readiness,
      resources: value.resources.map((r) => {
        const label = r.label.trim() || null;
        switch (r.type) {
          case 'url':
            return { type: 'url', url: r.url.trim(), label, counts: false };
          case 'deck':
            return { type: 'deck', deckId: r.deckId, label, counts: r.counts };
          case 'material':
            return { type: 'material', materialId: r.materialId, label, counts: r.counts };
          default:
            return { type: 'tree', treeId: r.treeId, label, counts: r.counts };
        }
      }),
      tags: parseTags(value.tags),
    };
    const id = this.id();
    const save$ = id ? this.nodeApi.update(Number(id), request) : this.nodeApi.create(request);
    this.saving.set(true);
    this.error.set(null);
    save$.subscribe({
      next: (node) => this.router.navigateByUrl(this.safeReturnUrl() ?? `/nodes/${node.id}`),
      error: (error) => {
        this.saving.set(false);
        this.error.set(errorMessage(error));
      },
    });
  }

  /** Loads the trees to choose from, once. */
  private loadTrees(): void {
    if (this.treesRequested) {
      return;
    }
    this.treesRequested = true;
    this.treeApi.list().subscribe({
      next: (trees) => this.trees.set(trees.map(({ id, title }) => ({ id, title }))),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }

  /** Loads the materials to choose from, once. */
  private loadMaterials(): void {
    if (this.materialsRequested) {
      return;
    }
    this.materialsRequested = true;
    this.materialApi.list().subscribe({
      next: (materials) => this.materials.set(materials.map(({ id, title }) => ({ id, title }))),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }

  /** Loads the decks to choose from, once. */
  private loadDecks(): void {
    if (this.decksRequested) {
      return;
    }
    this.decksRequested = true;
    this.flashcardApi.decks().subscribe({
      next: (decks) => this.decks.set(decks.map(({ id, title }) => ({ id, title }))),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }

  protected cancelUrl(): string {
    const id = this.id();
    return this.safeReturnUrl() ?? (id ? `/nodes/${id}` : '/nodes');
  }

  /** `returnTo` if it's an in-app path; never an absolute URL from the query string. */
  protected safeReturnUrl(): string | null {
    const returnTo = this.returnTo();
    return returnTo?.startsWith('/') && !returnTo.startsWith('//') ? returnTo : null;
  }
}
