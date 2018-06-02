import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ContactKkfComponent } from './contact-kkf.component';
import { ContactKkfDetailComponent } from './contact-kkf-detail.component';
import { ContactKkfPopupComponent } from './contact-kkf-dialog.component';
import { ContactKkfDeletePopupComponent } from './contact-kkf-delete-dialog.component';

export const contactRoute: Routes = [
    {
        path: 'contact-kkf',
        component: ContactKkfComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.contact.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'contact-kkf/:id',
        component: ContactKkfDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.contact.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const contactPopupRoute: Routes = [
    {
        path: 'contact-kkf-new',
        component: ContactKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.contact.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contact-kkf/:id/edit',
        component: ContactKkfPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.contact.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contact-kkf/:id/delete',
        component: ContactKkfDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kkfApp.contact.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
