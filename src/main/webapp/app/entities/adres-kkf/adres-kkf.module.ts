import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KkfSharedModule } from '../../shared';
import {
    AdresKkfService,
    AdresKkfPopupService,
    AdresKkfComponent,
    AdresKkfDetailComponent,
    AdresKkfDialogComponent,
    AdresKkfPopupComponent,
    AdresKkfDeletePopupComponent,
    AdresKkfDeleteDialogComponent,
    adresRoute,
    adresPopupRoute,
} from './';

const ENTITY_STATES = [
    ...adresRoute,
    ...adresPopupRoute,
];

@NgModule({
    imports: [
        KkfSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AdresKkfComponent,
        AdresKkfDetailComponent,
        AdresKkfDialogComponent,
        AdresKkfDeleteDialogComponent,
        AdresKkfPopupComponent,
        AdresKkfDeletePopupComponent,
    ],
    entryComponents: [
        AdresKkfComponent,
        AdresKkfDialogComponent,
        AdresKkfPopupComponent,
        AdresKkfDeleteDialogComponent,
        AdresKkfDeletePopupComponent,
    ],
    providers: [
        AdresKkfService,
        AdresKkfPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KkfAdresKkfModule {}
