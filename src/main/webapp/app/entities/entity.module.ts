import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { KkfPersoonKkfModule } from './persoon-kkf/persoon-kkf.module';
import { KkfContactKkfModule } from './contact-kkf/contact-kkf.module';
import { KkfContactTypeKkfModule } from './contact-type-kkf/contact-type-kkf.module';
import { KkfAdresTypeKkfModule } from './adres-type-kkf/adres-type-kkf.module';
import { KkfAdresKkfModule } from './adres-kkf/adres-kkf.module';
import { KkfGeslachtKkfModule } from './geslacht-kkf/geslacht-kkf.module';
import { KkfRelatieKkfModule } from './relatie-kkf/relatie-kkf.module';
import { KkfRelatieTypeKkfModule } from './relatie-type-kkf/relatie-type-kkf.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        KkfPersoonKkfModule,
        KkfContactKkfModule,
        KkfContactTypeKkfModule,
        KkfAdresTypeKkfModule,
        KkfAdresKkfModule,
        KkfGeslachtKkfModule,
        KkfRelatieKkfModule,
        KkfRelatieTypeKkfModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KkfEntityModule {}
