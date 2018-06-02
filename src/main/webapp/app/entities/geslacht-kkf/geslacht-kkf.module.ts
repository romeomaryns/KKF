import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KkfSharedModule } from '../../shared';
import {
    GeslachtKkfService,
    GeslachtKkfPopupService,
    GeslachtKkfComponent,
    GeslachtKkfDetailComponent,
    GeslachtKkfDialogComponent,
    GeslachtKkfPopupComponent,
    GeslachtKkfDeletePopupComponent,
    GeslachtKkfDeleteDialogComponent,
    geslachtRoute,
    geslachtPopupRoute,
} from './';

const ENTITY_STATES = [
    ...geslachtRoute,
    ...geslachtPopupRoute,
];

@NgModule({
    imports: [
        KkfSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        GeslachtKkfComponent,
        GeslachtKkfDetailComponent,
        GeslachtKkfDialogComponent,
        GeslachtKkfDeleteDialogComponent,
        GeslachtKkfPopupComponent,
        GeslachtKkfDeletePopupComponent,
    ],
    entryComponents: [
        GeslachtKkfComponent,
        GeslachtKkfDialogComponent,
        GeslachtKkfPopupComponent,
        GeslachtKkfDeleteDialogComponent,
        GeslachtKkfDeletePopupComponent,
    ],
    providers: [
        GeslachtKkfService,
        GeslachtKkfPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KkfGeslachtKkfModule {}
