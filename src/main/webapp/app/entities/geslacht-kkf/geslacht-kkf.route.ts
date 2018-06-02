import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { GeslachtKkfComponent } from './geslacht-kkf.component';
import { GeslachtKkfDetailComponent } from './geslacht-kkf-detail.component';
import { GeslachtKkfPopupComponent } from './geslacht-kkf-dialog.component';
import { GeslachtKkfDeletePopupComponent } from './geslacht-kkf-delete-dialog.component';

export const geslachtRoute: Routes = [
    {
        path: 'geslacht-kkf',
        component: GeslachtKkfComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.geslacht.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'geslacht-kkf/:id',
        component: GeslachtKkfDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.geslacht.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const geslachtPopupRoute: Routes = [
    {
        path: 'geslacht-kkf-new',
        component: GeslachtKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.geslacht.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'geslacht-kkf/:id/edit',
        component: GeslachtKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.geslacht.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'geslacht-kkf/:id/delete',
        component: GeslachtKkfDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.geslacht.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
