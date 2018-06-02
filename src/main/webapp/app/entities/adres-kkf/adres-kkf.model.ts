import { BaseEntity } from './../../shared';

export class AdresKkf implements BaseEntity {
    constructor(
        public id?: number,
        public straatnaam?: string,
        public huisnummer?: string,
        public postcode?: string,
        public stad?: string,
        public adresType?: BaseEntity,
    ) {
    }
}
