import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { AdresTypeKkfComponent } from './adres-type-kkf.component';
import { AdresTypeKkfDetailComponent } from './adres-type-kkf-detail.component';
import { AdresTypeKkfPopupComponent } from './adres-type-kkf-dialog.component';
import { AdresTypeKkfDeletePopupComponent } from './adres-type-kkf-delete-dialog.component';

export const adresTypeRoute: Routes = [
    {
        path: 'adres-type-kkf',
        component: AdresTypeKkfComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.adresType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'adres-type-kkf/:id',
        component: AdresTypeKkfDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.adresType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const adresTypePopupRoute: Routes = [
    {
        path: 'adres-type-kkf-new',
        component: AdresTypeKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.adresType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'adres-type-kkf/:id/edit',
        component: AdresTypeKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.adresType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'adres-type-kkf/:id/delete',
        component: AdresTypeKkfDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.adresType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
