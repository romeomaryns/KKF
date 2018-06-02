import { BaseEntity } from './../../shared';

export class PersoonKkf implements BaseEntity {
    constructor(
        public id?: number,
        public voornaam?: string,
        public familienaam?: string,
        public geboortedatum?: any,
        public contactInfo?: BaseEntity,
        public geslacht?: BaseEntity,
        public adresInfo?: BaseEntity,
        public relaties?: BaseEntity[],
        public contact?: BaseEntity,
    ) {
    }
}
