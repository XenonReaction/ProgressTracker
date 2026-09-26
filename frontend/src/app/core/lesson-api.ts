import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Lesson, LessonRequest } from './api.models';

@Injectable({ providedIn: 'root' })
export class LessonApi {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/v1/lessons';

  list(): Observable<Lesson[]> {
    return this.http.get<Lesson[]>(this.baseUrl);
  }

  /** Reads a lesson without counting as a review. */
  get(id: number): Observable<Lesson> {
    return this.http.get<Lesson>(`${this.baseUrl}/${id}`);
  }

  create(request: LessonRequest): Observable<Lesson> {
    return this.http.post<Lesson>(this.baseUrl, request);
  }

  update(id: number, request: LessonRequest): Observable<Lesson> {
    return this.http.put<Lesson>(`${this.baseUrl}/${id}`, request);
  }

  /** Deletes the lesson with its activity; refused (409) while a node lists it. */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  /** Records that the user opened the lesson to read it; returns the lesson. */
  recordOpen(id: number): Observable<Lesson> {
    return this.http.post<Lesson>(`${this.baseUrl}/${id}/opens`, null);
  }

  /** Records how far through the lesson the user is; returns the lesson. */
  recordProgress(id: number, progress: number): Observable<Lesson> {
    return this.http.post<Lesson>(`${this.baseUrl}/${id}/progress`, { progress });
  }
}
