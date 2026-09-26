import { RouterStateSnapshot, Routes } from '@angular/router';

import { DeckForm } from './flashcards/deck-form';
import { DeckList } from './flashcards/deck-list';
import { DeckStudy } from './flashcards/deck-study';
import { DeckView } from './flashcards/deck-view';
import { ReviewPage } from './flashcards/review-page';
import { NodeForm } from './nodes/node-form';
import { NodeList } from './nodes/node-list';
import { NodeView } from './nodes/node-view';
import { TreeForm } from './trees/tree-form';
import { TreeList } from './trees/tree-list';
import { TreeView } from './trees/tree-view';

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
      (view: TreeView, _route: unknown, _state: unknown, next: RouterStateSnapshot) => view.canLeave(next.url),
    ],
  },
  { path: 'decks', component: DeckList, title: 'Flashcards' },
  { path: 'decks/new', component: DeckForm, title: 'New deck' },
  { path: 'decks/:id', component: DeckView, title: 'Deck' },
  { path: 'decks/:id/edit', component: DeckForm, title: 'Edit deck' },
  { path: 'decks/:id/study', component: DeckStudy, title: 'Study' },
  { path: 'review', component: ReviewPage, title: 'Review' },
  { path: '**', redirectTo: 'nodes' },
];
