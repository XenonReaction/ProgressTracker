import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Tree, TreeNode } from './api.models';

/** Read-only for now: tree editing arrives in Phase 4. */
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

  nodes(treeId: number): Observable<TreeNode[]> {
    return this.http.get<TreeNode[]>(`${this.baseUrl}/${treeId}/nodes`);
  }
}
