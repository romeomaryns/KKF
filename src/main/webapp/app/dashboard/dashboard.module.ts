import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KkfSharedModule } from '../shared';

import { DASHBOARD_ROUTE, DashboardComponent } from './';
import { TellersComponent } from './tellers/tellers.component';

@NgModule({
    imports: [
      KkfSharedModule,
      RouterModule.forRoot([ DASHBOARD_ROUTE ], { useHash: true })
    ],
    declarations: [
      DashboardComponent,
      TellersComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KkfAppDashboardModule {}
