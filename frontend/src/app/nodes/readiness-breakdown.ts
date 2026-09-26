import { DatePipe } from '@angular/common';
import { Component, computed, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { NodeResource } from '../core/api.models';
import {
  RESOURCE_TYPE_NAMES,
  countingResources,
  isSelfReported,
  resourceLink,
  resourceTitle,
} from './resources';

/**
 * What a derived readiness is made of, e.g. "Deck Flexbox cards: 75%, Material Guide: 60%
 * (self-reported) → 68%", with each part linking to its page, marking parts with a review
 * due, and when anything beneath was last reviewed.
 * Only for a node whose resources count.
 */
@Component({
  selector: 'app-readiness-breakdown',
  imports: [RouterLink, DatePipe],
  template: `
    <p class="breakdown">
      @for (part of parts(); track $index; let last = $last) {
        {{ typeNames[part.type] }}
        @if (links() && link(part); as target) {
          <a [routerLink]="target">{{ title(part) }}</a>
        } @else {
          {{ title(part) }}
        }
        : {{ part.readiness }}%{{ selfReported(part) ? ' (self-reported)' : ''
        }}@if (part.reviewDue) {<strong class="review-due"> (review due)</strong>}{{ last ? '' : ',' }}
      }
      → <strong>{{ readiness() }}%</strong>
    </p>
    <p class="muted">
      Last reviewed: {{ lastReviewedAt() ? (lastReviewedAt() | date: 'medium') : 'never' }}
    </p>
  `,
})
export class ReadinessBreakdown {
  readonly resources = input.required<NodeResource[]>();
  readonly readiness = input.required<number>();
  readonly lastReviewedAt = input<string | null>(null);
  /** Link each part to its tree or deck page. */
  readonly links = input(true);

  protected readonly parts = computed(() => countingResources(this.resources()));
  protected readonly typeNames = RESOURCE_TYPE_NAMES;
  protected readonly title = resourceTitle;
  protected readonly link = resourceLink;
  protected readonly selfReported = isSelfReported;
}
