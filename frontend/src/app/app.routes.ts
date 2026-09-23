import { Routes } from '@angular/router';

import { NodeForm } from './nodes/node-form';
import { NodeList } from './nodes/node-list';
import { TreeList } from './trees/tree-list';
import { TreeView } from './trees/tree-view';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'nodes' },
  { path: 'nodes', component: NodeList, title: 'Node library' },
  { path: 'nodes/new', component: NodeForm, title: 'New node' },
  { path: 'nodes/:id/edit', component: NodeForm, title: 'Edit node' },
  { path: 'trees', component: TreeList, title: 'Trees' },
  { path: 'trees/:id', component: TreeView, title: 'Tree' },
  { path: '**', redirectTo: 'nodes' },
];
