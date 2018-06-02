import { BaseEntity } from './../../shared';

export class ContactTypeKkf implements BaseEntity {
    constructor(
        public id?: number,
        public omschrijving?: string,
    ) {
    }
}
