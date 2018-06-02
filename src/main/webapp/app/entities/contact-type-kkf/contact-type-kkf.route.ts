import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ContactTypeKkfComponent } from './contact-type-kkf.component';
import { ContactTypeKkfDetailComponent } from './contact-type-kkf-detail.component';
import { ContactTypeKkfPopupComponent } from './contact-type-kkf-dialog.component';
import { ContactTypeKkfDeletePopupComponent } from './contact-type-kkf-delete-dialog.component';

export const contactTypeRoute: Routes = [
    {
        path: 'contact-type-kkf',
        component: ContactTypeKkfComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.contactType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'contact-type-kkf/:id',
        component: ContactTypeKkfDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.contactType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const contactTypePopupRoute: Routes = [
    {
        path: 'contact-type-kkf-new',
        component: ContactTypeKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.contactType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contact-type-kkf/:id/edit',
        component: ContactTypeKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.contactType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contact-type-kkf/:id/delete',
        component: ContactTypeKkfDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.contactType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
