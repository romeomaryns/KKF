import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { RelatieKkfComponent } from './relatie-kkf.component';
import { RelatieKkfDetailComponent } from './relatie-kkf-detail.component';
import { RelatieKkfPopupComponent } from './relatie-kkf-dialog.component';
import { RelatieKkfDeletePopupComponent } from './relatie-kkf-delete-dialog.component';

export const relatieRoute: Routes = [
    {
        path: 'relatie-kkf',
        component: RelatieKkfComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.relatie.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'relatie-kkf/:id',
        component: RelatieKkfDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.relatie.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const relatiePopupRoute: Routes = [
    {
        path: 'relatie-kkf-new',
        component: RelatieKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.relatie.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'relatie-kkf/:id/edit',
        component: RelatieKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.relatie.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'relatie-kkf/:id/delete',
        component: RelatieKkfDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.relatie.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
