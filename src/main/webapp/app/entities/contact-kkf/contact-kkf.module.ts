import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KkfSharedModule } from '../../shared';
import {
    ContactKkfService,
    ContactKkfPopupService,
    ContactKkfComponent,
    ContactKkfDetailComponent,
    ContactKkfDialogComponent,
    ContactKkfPopupComponent,
    ContactKkfDeletePopupComponent,
    ContactKkfDeleteDialogComponent,
    contactRoute,
    contactPopupRoute,
} from './';

const ENTITY_STATES = [
    ...contactRoute,
    ...contactPopupRoute,
];

@NgModule({
    imports: [
        KkfSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ContactKkfComponent,
        ContactKkfDetailComponent,
        ContactKkfDialogComponent,
        ContactKkfDeleteDialogComponent,
        ContactKkfPopupComponent,
        ContactKkfDeletePopupComponent,
    ],
    entryComponents: [
        ContactKkfComponent,
        ContactKkfDialogComponent,
        ContactKkfPopupComponent,
        ContactKkfDeleteDialogComponent,
        ContactKkfDeletePopupComponent,
    ],
    providers: [
        ContactKkfService,
        ContactKkfPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KkfContactKkfModule {}
