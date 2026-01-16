import { Routes } from '@angular/router';

import { Search } from './pages/search/search';
import { Details } from './pages/details/details';

export const routes: Routes = [
  { 
    path: '', component: Search, title: 'Flickr Search'
  },
  { 
    path: 'detail/:id', component: Details, title: 'Image Detail'
  },
  {
    path: '**', redirectTo: ''
  }
];