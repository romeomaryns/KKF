import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KkfSharedModule } from '../../shared';
import {
    ContactTypeKkfService,
    ContactTypeKkfPopupService,
    ContactTypeKkfComponent,
    ContactTypeKkfDetailComponent,
    ContactTypeKkfDialogComponent,
    ContactTypeKkfPopupComponent,
    ContactTypeKkfDeletePopupComponent,
    ContactTypeKkfDeleteDialogComponent,
    contactTypeRoute,
    contactTypePopupRoute,
} from './';

const ENTITY_STATES = [
    ...contactTypeRoute,
    ...contactTypePopupRoute,
];

@NgModule({
    imports: [
        KkfSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ContactTypeKkfComponent,
        ContactTypeKkfDetailComponent,
        ContactTypeKkfDialogComponent,
        ContactTypeKkfDeleteDialogComponent,
        ContactTypeKkfPopupComponent,
        ContactTypeKkfDeletePopupComponent,
    ],
    entryComponents: [
        ContactTypeKkfComponent,
        ContactTypeKkfDialogComponent,
        ContactTypeKkfPopupComponent,
        ContactTypeKkfDeleteDialogComponent,
        ContactTypeKkfDeletePopupComponent,
    ],
    providers: [
        ContactTypeKkfService,
        ContactTypeKkfPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KkfContactTypeKkfModule {}
