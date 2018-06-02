import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KkfSharedModule } from '../../shared';
import {
    PersoonKkfService,
    PersoonKkfPopupService,
    PersoonKkfComponent,
    PersoonKkfDetailComponent,
    PersoonKkfDialogComponent,
    PersoonKkfPopupComponent,
    PersoonKkfDeletePopupComponent,
    PersoonKkfDeleteDialogComponent,
    persoonRoute,
    persoonPopupRoute,
} from './';

const ENTITY_STATES = [
    ...persoonRoute,
    ...persoonPopupRoute,
];

@NgModule({
    imports: [
        KkfSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PersoonKkfComponent,
        PersoonKkfDetailComponent,
        PersoonKkfDialogComponent,
        PersoonKkfDeleteDialogComponent,
        PersoonKkfPopupComponent,
        PersoonKkfDeletePopupComponent,
    ],
    entryComponents: [
        PersoonKkfComponent,
        PersoonKkfDialogComponent,
        PersoonKkfPopupComponent,
        PersoonKkfDeleteDialogComponent,
        PersoonKkfDeletePopupComponent,
    ],
    providers: [
        PersoonKkfService,
        PersoonKkfPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KkfPersoonKkfModule {}
