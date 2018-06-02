import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PersoonKkfComponent } from './persoon-kkf.component';
import { PersoonKkfDetailComponent } from './persoon-kkf-detail.component';
import { PersoonKkfPopupComponent } from './persoon-kkf-dialog.component';
import { PersoonKkfDeletePopupComponent } from './persoon-kkf-delete-dialog.component';

export const persoonRoute: Routes = [
    {
        path: 'persoon-kkf',
        component: PersoonKkfComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.persoon.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'persoon-kkf/:id',
        component: PersoonKkfDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.persoon.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const persoonPopupRoute: Routes = [
    {
        path: 'persoon-kkf-new',
        component: PersoonKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.persoon.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'persoon-kkf/:id/edit',
        component: PersoonKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.persoon.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'persoon-kkf/:id/delete',
        component: PersoonKkfDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.persoon.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
