import { Route } from '@angular/router';

import { UserRouteAccessService } from '../shared';
import { DashboardComponent } from './';

export const DASHBOARD_ROUTE: Route = {
  path: 'dashboard',
  component: DashboardComponent,
  data: {
    authorities: [],
    pageTitle: 'dashboard.title'
  },
  canActivate: [UserRouteAccessService]
};
