import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Tree, TreeNode, TreeRequest } from './api.models';

/** Tree metadata CRUD, plus reading a tree's contents. Editing contents arrives in Phase 4. */
@Injectable({ providedIn: 'root' })
export class TreeApi {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/v1/trees';

  list(): Observable<Tree[]> {
    return this.http.get<Tree[]>(this.baseUrl);
  }

  get(id: number): Observable<Tree> {
    return this.http.get<Tree>(`${this.baseUrl}/${id}`);
  }

  create(request: TreeRequest): Observable<Tree> {
    return this.http.post<Tree>(this.baseUrl, request);
  }

  update(id: number, request: TreeRequest): Observable<Tree> {
    return this.http.put<Tree>(`${this.baseUrl}/${id}`, request);
  }

  /** Also removes the tree's node placements and prerequisite edges; library nodes stay. */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  nodes(treeId: number): Observable<TreeNode[]> {
    return this.http.get<TreeNode[]>(`${this.baseUrl}/${treeId}/nodes`);
  }
}
