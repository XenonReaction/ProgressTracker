import { DatePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Lesson } from '../core/api.models';
import { LessonApi } from '../core/lesson-api';
import { errorMessage, problemOf } from '../core/problem';

/** The user's lessons (`/lessons`), each with the progress entered and when it was last opened. */
@Component({
  selector: 'app-lesson-list',
  imports: [RouterLink, DatePipe],
  templateUrl: './lesson-list.html',
})
export class LessonList {
  private readonly lessonApi = inject(LessonApi);

  /** null until the first load finishes. */
  protected readonly lessons = signal<Lesson[] | null>(null);
  protected readonly error = signal<string | null>(null);
  /** Nodes that blocked the last delete, so the user can go and remove the lesson from them. */
  protected readonly blockingNodes = signal<{ id: number; title: string }[]>([]);

  constructor() {
    this.load();
  }

  protected delete(lesson: Lesson): void {
    if (
      !confirm(
        `Delete the lesson "${lesson.title}"? Its sections and your progress on it are deleted too.`,
      )
    ) {
      return;
    }
    this.error.set(null);
    this.blockingNodes.set([]);
    this.lessonApi.delete(lesson.id).subscribe({
      next: () => this.load(),
      error: (error) => {
        this.error.set(errorMessage(error));
        this.blockingNodes.set(problemOf(error)?.nodes ?? []);
      },
    });
  }

  private load(): void {
    this.lessonApi.list().subscribe({
      next: (lessons) => this.lessons.set(lessons),
      error: (error) => this.error.set(errorMessage(error)),
    });
  }
}
