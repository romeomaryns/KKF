import { BaseEntity } from './../../shared';

export class GeslachtKkf implements BaseEntity {
    constructor(
        public id?: number,
        public omschrijving?: string,
    ) {
    }
}
