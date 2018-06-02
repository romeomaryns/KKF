import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { AdresKkfComponent } from './adres-kkf.component';
import { AdresKkfDetailComponent } from './adres-kkf-detail.component';
import { AdresKkfPopupComponent } from './adres-kkf-dialog.component';
import { AdresKkfDeletePopupComponent } from './adres-kkf-delete-dialog.component';

export const adresRoute: Routes = [
    {
        path: 'adres-kkf',
        component: AdresKkfComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.adres.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'adres-kkf/:id',
        component: AdresKkfDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.adres.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const adresPopupRoute: Routes = [
    {
        path: 'adres-kkf-new',
        component: AdresKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.adres.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'adres-kkf/:id/edit',
        component: AdresKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.adres.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'adres-kkf/:id/delete',
        component: AdresKkfDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.adres.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
