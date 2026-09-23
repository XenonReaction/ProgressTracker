import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Node, NodeRequest } from './api.models';

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

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
