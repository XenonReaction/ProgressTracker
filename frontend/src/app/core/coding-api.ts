import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import {
  CodingQuestion,
  CodingQuestionRequest,
  QuestionSet,
  QuestionSetRequest,
} from './api.models';

@Injectable({ providedIn: 'root' })
export class CodingApi {
  private readonly http = inject(HttpClient);
  private readonly setsUrl = '/api/v1/question-sets';

  sets(): Observable<QuestionSet[]> {
    return this.http.get<QuestionSet[]>(this.setsUrl);
  }

  set(id: number): Observable<QuestionSet> {
    return this.http.get<QuestionSet>(`${this.setsUrl}/${id}`);
  }

  createSet(request: QuestionSetRequest): Observable<QuestionSet> {
    return this.http.post<QuestionSet>(this.setsUrl, request);
  }

  updateSet(id: number, request: QuestionSetRequest): Observable<QuestionSet> {
    return this.http.put<QuestionSet>(`${this.setsUrl}/${id}`, request);
  }

  /** Deletes the set with its questions and attempts; refused (409) while a node lists it. */
  deleteSet(id: number): Observable<void> {
    return this.http.delete<void>(`${this.setsUrl}/${id}`);
  }

  /** The set's questions, solutions hidden unless revealed. */
  questions(setId: number): Observable<CodingQuestion[]> {
    return this.http.get<CodingQuestion[]>(`${this.setsUrl}/${setId}/questions`);
  }

  /**
   * One question. With `includeSolution` (for the edit form) the solution comes back even if
   * it hasn't been revealed, without counting as a reveal.
   */
  question(setId: number, questionId: number, includeSolution = false): Observable<CodingQuestion> {
    const params: Record<string, string> = includeSolution ? { includeSolution: 'true' } : {};
    return this.http.get<CodingQuestion>(`${this.setsUrl}/${setId}/questions/${questionId}`, {
      params,
    });
  }

  createQuestion(setId: number, request: CodingQuestionRequest): Observable<CodingQuestion> {
    return this.http.post<CodingQuestion>(`${this.setsUrl}/${setId}/questions`, request);
  }

  updateQuestion(
    setId: number,
    questionId: number,
    request: CodingQuestionRequest,
  ): Observable<CodingQuestion> {
    return this.http.put<CodingQuestion>(
      `${this.setsUrl}/${setId}/questions/${questionId}`,
      request,
    );
  }

  deleteQuestion(setId: number, questionId: number): Observable<void> {
    return this.http.delete<void>(`${this.setsUrl}/${setId}/questions/${questionId}`);
  }

  /** Shows the solution, recording that it was revealed. */
  reveal(setId: number, questionId: number): Observable<CodingQuestion> {
    return this.http.post<CodingQuestion>(
      `${this.setsUrl}/${setId}/questions/${questionId}/reveal`,
      null,
    );
  }

  markSolved(setId: number, questionId: number): Observable<CodingQuestion> {
    return this.http.post<CodingQuestion>(
      `${this.setsUrl}/${setId}/questions/${questionId}/solved`,
      null,
    );
  }
}
