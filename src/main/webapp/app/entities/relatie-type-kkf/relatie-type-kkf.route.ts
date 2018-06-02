import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { RelatieTypeKkfComponent } from './relatie-type-kkf.component';
import { RelatieTypeKkfDetailComponent } from './relatie-type-kkf-detail.component';
import { RelatieTypeKkfPopupComponent } from './relatie-type-kkf-dialog.component';
import { RelatieTypeKkfDeletePopupComponent } from './relatie-type-kkf-delete-dialog.component';

export const relatieTypeRoute: Routes = [
    {
        path: 'relatie-type-kkf',
        component: RelatieTypeKkfComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.relatieType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'relatie-type-kkf/:id',
        component: RelatieTypeKkfDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.relatieType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const relatieTypePopupRoute: Routes = [
    {
        path: 'relatie-type-kkf-new',
        component: RelatieTypeKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.relatieType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'relatie-type-kkf/:id/edit',
        component: RelatieTypeKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.relatieType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'relatie-type-kkf/:id/delete',
        component: RelatieTypeKkfDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.relatieType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
