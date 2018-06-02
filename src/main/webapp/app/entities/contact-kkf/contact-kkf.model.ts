import { BaseEntity } from './../../shared';

export class ContactKkf implements BaseEntity {
    constructor(
        public id?: number,
        public omschrijving?: string,
        public persoons?: BaseEntity[],
        public contactType?: BaseEntity,
    ) {
    }
}
