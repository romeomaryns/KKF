import { BaseEntity } from './../../shared';

export class AdresTypeKkf implements BaseEntity {
    constructor(
        public id?: number,
        public omschrijving?: string,
    ) {
    }
}
