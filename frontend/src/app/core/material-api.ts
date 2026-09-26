import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Material, MaterialRequest, ProgressUpdate, ProgressUpdateRequest } from './api.models';

@Injectable({ providedIn: 'root' })
export class MaterialApi {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/v1/materials';

  list(): Observable<Material[]> {
    return this.http.get<Material[]>(this.baseUrl);
  }

  get(id: number): Observable<Material> {
    return this.http.get<Material>(`${this.baseUrl}/${id}`);
  }

  create(request: MaterialRequest): Observable<Material> {
    return this.http.post<Material>(this.baseUrl, request);
  }

  /** Changes the details only; it isn't a progress update. */
  update(id: number, request: MaterialRequest): Observable<Material> {
    return this.http.put<Material>(`${this.baseUrl}/${id}`, request);
  }

  /** Deletes the material with its history; refused (409) while a node lists it. */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  /** Every progress update, newest first. */
  history(id: number): Observable<ProgressUpdate[]> {
    return this.http.get<ProgressUpdate[]>(`${this.baseUrl}/${id}/progress`);
  }

  /** Records new progress; returns the material with it. Earlier updates are kept. */
  recordProgress(id: number, request: ProgressUpdateRequest): Observable<Material> {
    return this.http.post<Material>(`${this.baseUrl}/${id}/progress`, request);
  }
}
