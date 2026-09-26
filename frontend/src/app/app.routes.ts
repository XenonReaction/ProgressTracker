import { RouterStateSnapshot, Routes } from '@angular/router';

import { NodeForm } from './nodes/node-form';
import { NodeList } from './nodes/node-list';
import { NodeView } from './nodes/node-view';
import { TreeForm } from './trees/tree-form';
import { TreeList } from './trees/tree-list';
import { TreeView } from './trees/tree-view';

/**
 * The node and tree pages load with the app. The learning modules' pages (flashcards,
 * materials, lessons, coding) load on first visit, which keeps the initial bundle within
 * budget and keeps `marked` out of it.
 */
export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'nodes' },
  { path: 'nodes', component: NodeList, title: 'Node library' },
  { path: 'nodes/new', component: NodeForm, title: 'New node' },
  { path: 'nodes/:id', component: NodeView, title: 'Node' },
  { path: 'nodes/:id/edit', component: NodeForm, title: 'Edit node' },
  { path: 'trees', component: TreeList, title: 'Trees' },
  { path: 'trees/new', component: TreeForm, title: 'New tree' },
  { path: 'trees/:id/edit', component: TreeForm, title: 'Edit tree' },
  // Leaving a tree in edit mode asks first, and discards the changes if the user goes anyway
  {
    path: 'trees/:id',
    component: TreeView,
    title: 'Tree',
    canDeactivate: [
      (view: TreeView, _route: unknown, _state: unknown, next: RouterStateSnapshot) =>
        view.canLeave(next.url),
    ],
  },
  {
    path: 'decks',
    loadComponent: () => import('./flashcards/deck-list').then((m) => m.DeckList),
    title: 'Flashcards',
  },
  {
    path: 'decks/new',
    loadComponent: () => import('./flashcards/deck-form').then((m) => m.DeckForm),
    title: 'New deck',
  },
  {
    path: 'decks/:id',
    loadComponent: () => import('./flashcards/deck-view').then((m) => m.DeckView),
    title: 'Deck',
  },
  {
    path: 'decks/:id/edit',
    loadComponent: () => import('./flashcards/deck-form').then((m) => m.DeckForm),
    title: 'Edit deck',
  },
  {
    path: 'decks/:id/study',
    loadComponent: () => import('./flashcards/deck-study').then((m) => m.DeckStudy),
    title: 'Study',
  },
  {
    path: 'review',
    loadComponent: () => import('./flashcards/review-page').then((m) => m.ReviewPage),
    title: 'Review',
  },
  {
    path: 'materials',
    loadComponent: () => import('./materials/material-list').then((m) => m.MaterialList),
    title: 'Materials',
  },
  {
    path: 'materials/new',
    loadComponent: () => import('./materials/material-form').then((m) => m.MaterialForm),
    title: 'New material',
  },
  {
    path: 'materials/:id',
    loadComponent: () => import('./materials/material-view').then((m) => m.MaterialView),
    title: 'Material',
  },
  {
    path: 'materials/:id/edit',
    loadComponent: () => import('./materials/material-form').then((m) => m.MaterialForm),
    title: 'Edit material',
  },
  {
    path: 'lessons',
    loadComponent: () => import('./lessons/lesson-list').then((m) => m.LessonList),
    title: 'Lessons',
  },
  {
    path: 'lessons/new',
    loadComponent: () => import('./lessons/lesson-form').then((m) => m.LessonForm),
    title: 'New lesson',
  },
  {
    path: 'lessons/:id',
    loadComponent: () => import('./lessons/lesson-view').then((m) => m.LessonView),
    title: 'Lesson',
  },
  {
    path: 'lessons/:id/edit',
    loadComponent: () => import('./lessons/lesson-form').then((m) => m.LessonForm),
    title: 'Edit lesson',
  },
  {
    path: 'question-sets',
    loadComponent: () => import('./coding/question-set-list').then((m) => m.QuestionSetList),
    title: 'Coding questions',
  },
  {
    path: 'question-sets/new',
    loadComponent: () => import('./coding/question-set-form').then((m) => m.QuestionSetForm),
    title: 'New question set',
  },
  {
    path: 'question-sets/:id',
    loadComponent: () => import('./coding/question-set-view').then((m) => m.QuestionSetView),
    title: 'Question set',
  },
  {
    path: 'question-sets/:id/edit',
    loadComponent: () => import('./coding/question-set-form').then((m) => m.QuestionSetForm),
    title: 'Edit question set',
  },
  {
    path: 'question-sets/:id/questions/new',
    loadComponent: () => import('./coding/question-form').then((m) => m.QuestionForm),
    title: 'New question',
  },
  {
    path: 'question-sets/:id/questions/:questionId',
    loadComponent: () => import('./coding/question-view').then((m) => m.QuestionView),
    title: 'Question',
  },
  {
    path: 'question-sets/:id/questions/:questionId/edit',
    loadComponent: () => import('./coding/question-form').then((m) => m.QuestionForm),
    title: 'Edit question',
  },
  { path: '**', redirectTo: 'nodes' },
];
