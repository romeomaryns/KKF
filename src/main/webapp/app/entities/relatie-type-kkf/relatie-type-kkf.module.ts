import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KkfSharedModule } from '../../shared';
import {
    RelatieTypeKkfService,
    RelatieTypeKkfPopupService,
    RelatieTypeKkfComponent,
    RelatieTypeKkfDetailComponent,
    RelatieTypeKkfDialogComponent,
    RelatieTypeKkfPopupComponent,
    RelatieTypeKkfDeletePopupComponent,
    RelatieTypeKkfDeleteDialogComponent,
    relatieTypeRoute,
    relatieTypePopupRoute,
} from './';

const ENTITY_STATES = [
    ...relatieTypeRoute,
    ...relatieTypePopupRoute,
];

@NgModule({
    imports: [
        KkfSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        RelatieTypeKkfComponent,
        RelatieTypeKkfDetailComponent,
        RelatieTypeKkfDialogComponent,
        RelatieTypeKkfDeleteDialogComponent,
        RelatieTypeKkfPopupComponent,
        RelatieTypeKkfDeletePopupComponent,
    ],
    entryComponents: [
        RelatieTypeKkfComponent,
        RelatieTypeKkfDialogComponent,
        RelatieTypeKkfPopupComponent,
        RelatieTypeKkfDeleteDialogComponent,
        RelatieTypeKkfDeletePopupComponent,
    ],
    providers: [
        RelatieTypeKkfService,
        RelatieTypeKkfPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KkfRelatieTypeKkfModule {}
