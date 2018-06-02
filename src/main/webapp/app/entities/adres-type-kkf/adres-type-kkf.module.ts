import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KkfSharedModule } from '../../shared';
import {
    AdresTypeKkfService,
    AdresTypeKkfPopupService,
    AdresTypeKkfComponent,
    AdresTypeKkfDetailComponent,
    AdresTypeKkfDialogComponent,
    AdresTypeKkfPopupComponent,
    AdresTypeKkfDeletePopupComponent,
    AdresTypeKkfDeleteDialogComponent,
    adresTypeRoute,
    adresTypePopupRoute,
} from './';

const ENTITY_STATES = [
    ...adresTypeRoute,
    ...adresTypePopupRoute,
];

@NgModule({
    imports: [
        KkfSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AdresTypeKkfComponent,
        AdresTypeKkfDetailComponent,
        AdresTypeKkfDialogComponent,
        AdresTypeKkfDeleteDialogComponent,
        AdresTypeKkfPopupComponent,
        AdresTypeKkfDeletePopupComponent,
    ],
    entryComponents: [
        AdresTypeKkfComponent,
        AdresTypeKkfDialogComponent,
        AdresTypeKkfPopupComponent,
        AdresTypeKkfDeleteDialogComponent,
        AdresTypeKkfDeletePopupComponent,
    ],
    providers: [
        AdresTypeKkfService,
        AdresTypeKkfPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KkfAdresTypeKkfModule {}
