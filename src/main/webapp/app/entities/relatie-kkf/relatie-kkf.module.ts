import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KkfSharedModule } from '../../shared';
import {
    RelatieKkfService,
    RelatieKkfPopupService,
    RelatieKkfComponent,
    RelatieKkfDetailComponent,
    RelatieKkfDialogComponent,
    RelatieKkfPopupComponent,
    RelatieKkfDeletePopupComponent,
    RelatieKkfDeleteDialogComponent,
    relatieRoute,
    relatiePopupRoute,
} from './';

const ENTITY_STATES = [
    ...relatieRoute,
    ...relatiePopupRoute,
];

@NgModule({
    imports: [
        KkfSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        RelatieKkfComponent,
        RelatieKkfDetailComponent,
        RelatieKkfDialogComponent,
        RelatieKkfDeleteDialogComponent,
        RelatieKkfPopupComponent,
        RelatieKkfDeletePopupComponent,
    ],
    entryComponents: [
        RelatieKkfComponent,
        RelatieKkfDialogComponent,
        RelatieKkfPopupComponent,
        RelatieKkfDeleteDialogComponent,
        RelatieKkfDeletePopupComponent,
    ],
    providers: [
        RelatieKkfService,
        RelatieKkfPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KkfRelatieKkfModule {}
