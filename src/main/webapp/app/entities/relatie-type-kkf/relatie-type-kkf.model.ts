import { BaseEntity } from './../../shared';

export class RelatieTypeKkf implements BaseEntity {
    constructor(
        public id?: number,
        public omschrijving?: string,
    ) {
    }
}
