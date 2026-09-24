import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Node, NodeRequest, TreeRef } from './api.models';

@Injectable({ providedIn: 'root' })
export class NodeApi {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/v1/nodes';

  list(): Observable<Node[]> {
    return this.http.get<Node[]>(this.baseUrl);
  }

  get(id: number): Observable<Node> {
    return this.http.get<Node>(`${this.baseUrl}/${id}`);
  }

  create(request: NodeRequest): Observable<Node> {
    return this.http.post<Node>(this.baseUrl, request);
  }

  update(id: number, request: NodeRequest): Observable<Node> {
    return this.http.put<Node>(`${this.baseUrl}/${id}`, request);
  }

  /** Sets only the hand-entered readiness; refused (409) for a node linked to a tree. */
  updateReadiness(id: number, readiness: number): Observable<Node> {
    return this.http.put<Node>(`${this.baseUrl}/${id}/readiness`, { readiness });
  }

  /** The trees the node is placed in. */
  treesUsing(id: number): Observable<TreeRef[]> {
    return this.http.get<TreeRef[]>(`${this.baseUrl}/${id}/trees`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
