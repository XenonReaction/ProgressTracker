import { RouterStateSnapshot, Routes } from '@angular/router';

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
  { path: '**', redirectTo: 'nodes' },
];
