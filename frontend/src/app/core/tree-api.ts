import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import {
  EdgeRoute,
  Prerequisite,
  Tree,
  TreeEditSession,
  TreeNode,
  TreeNodeCreateRequest,
  TreeNodePosition,
  TreeNodeUpdateRequest,
  TreeRequest,
} from './api.models';

/** Trees, the nodes placed in them, and their prerequisite edges. */
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

  /** Enters edit mode, saving a restore point. 409 if the tree is already being edited. */
  startEditSession(treeId: number): Observable<TreeEditSession> {
    return this.http.post<TreeEditSession>(`${this.baseUrl}/${treeId}/edit-session`, null);
  }

  /** "Done": keeps the changes and drops the restore point. */
  finishEditSession(treeId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${treeId}/edit-session`);
  }

  /** "Discard changes": puts the tree back as it was when edit mode started. */
  discardEditSession(treeId: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${treeId}/edit-session/discard`, null);
  }

  nodes(treeId: number): Observable<TreeNode[]> {
    return this.http.get<TreeNode[]>(`${this.baseUrl}/${treeId}/nodes`);
  }

  addNode(treeId: number, request: TreeNodeCreateRequest): Observable<TreeNode> {
    return this.http.post<TreeNode>(`${this.baseUrl}/${treeId}/nodes`, request);
  }

  updateNode(treeId: number, treeNodeId: number, request: TreeNodeUpdateRequest): Observable<TreeNode> {
    return this.http.put<TreeNode>(`${this.baseUrl}/${treeId}/nodes/${treeNodeId}`, request);
  }

  /** Saves many positions in one transaction; returns every node in the tree. */
  updatePositions(treeId: number, positions: TreeNodePosition[]): Observable<TreeNode[]> {
    return this.http.put<TreeNode[]>(`${this.baseUrl}/${treeId}/nodes/positions`, { positions });
  }

  /** Removes the node from this tree along with its edges; the library node stays. */
  removeNode(treeId: number, treeNodeId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${treeId}/nodes/${treeNodeId}`);
  }

  prerequisites(treeId: number): Observable<Prerequisite[]> {
    return this.http.get<Prerequisite[]>(`${this.baseUrl}/${treeId}/prerequisites`);
  }

  /** `route` puts back an edge with the shape it had (undo); omit it for the default route. */
  addPrerequisite(
    treeId: number,
    prerequisiteTreeNodeId: number,
    dependentTreeNodeId: number,
    route: EdgeRoute | null = null,
  ): Observable<Prerequisite> {
    return this.http.post<Prerequisite>(`${this.baseUrl}/${treeId}/prerequisites`, {
      prerequisiteTreeNodeId,
      dependentTreeNodeId,
      ...(route ? { route } : {}),
    });
  }

  /** Sets an edge's hand-adjusted route, or resets it to the default with null. */
  updateRoute(treeId: number, prerequisiteId: number, route: EdgeRoute | null): Observable<Prerequisite> {
    return this.http.put<Prerequisite>(`${this.baseUrl}/${treeId}/prerequisites/${prerequisiteId}/route`, { route });
  }

  /** Resets every edge in the tree to its default route (auto-layout does this). */
  resetRoutes(treeId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${treeId}/prerequisites/routes`);
  }

  removePrerequisite(treeId: number, prerequisiteId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${treeId}/prerequisites/${prerequisiteId}`);
  }
}
