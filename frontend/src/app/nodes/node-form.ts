import { Component, OnInit, inject, input, signal } from '@angular/core';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { NodeLink, NodeRequest } from '../core/api.models';
import { NodeApi } from '../core/node-api';
import { errorMessage } from '../core/problem';

type LinkGroup = FormGroup<{ url: FormControl<string>; label: FormControl<string> }>;

/** Create (`/nodes/new`) or edit (`/nodes/:id/edit`) a library node, including its readiness. */
@Component({
  selector: 'app-node-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './node-form.html',
})
export class NodeForm implements OnInit {
  private readonly nodeApi = inject(NodeApi);
  private readonly router = inject(Router);

  /** Route param; absent when creating. */
  readonly id = input<string>();
  /** Query param: where to go after saving, e.g. back to the tree the user came from. */
  readonly returnTo = input<string>();

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
    links: new FormArray<LinkGroup>([]),
  });

  protected readonly error = signal<string | null>(null);
  protected readonly saving = signal(false);

  ngOnInit(): void {
    const id = this.id();
    if (id) {
      this.nodeApi.get(Number(id)).subscribe({
        next: (node) => {
          this.form.patchValue({
            title: node.title,
            description: node.description ?? '',
            readiness: node.readiness,
          });
          node.links.forEach((link) => this.addLink(link));
        },
        error: (error) => this.error.set(errorMessage(error)),
      });
    }
  }

  protected get links(): FormArray<LinkGroup> {
    return this.form.controls.links;
  }

  protected addLink(link?: NodeLink): void {
    this.links.push(
      new FormGroup({
        url: new FormControl(link?.url ?? '', {
          nonNullable: true,
          validators: [Validators.required, Validators.pattern(/^https?:\/\/\S+$/)],
        }),
        label: new FormControl(link?.label ?? '', { nonNullable: true }),
      }),
    );
  }

  protected removeLink(index: number): void {
    this.links.removeAt(index);
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
      links: value.links.map((link) => ({ url: link.url.trim(), label: link.label.trim() || null })),
    };
    const id = this.id();
    const save$ = id ? this.nodeApi.update(Number(id), request) : this.nodeApi.create(request);
    this.saving.set(true);
    this.error.set(null);
    save$.subscribe({
      next: () => this.router.navigateByUrl(this.safeReturnUrl()),
      error: (error) => {
        this.saving.set(false);
        this.error.set(errorMessage(error));
      },
    });
  }

  /** Only follow in-app paths, never an absolute URL from the query string. */
  protected safeReturnUrl(): string {
    const returnTo = this.returnTo();
    return returnTo?.startsWith('/') && !returnTo.startsWith('//') ? returnTo : '/nodes';
  }
}
