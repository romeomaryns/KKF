import { BaseEntity } from './../../shared';

export class RelatieKkf implements BaseEntity {
    constructor(
        public id?: number,
        public omschrijving?: string,
        public relatieType?: BaseEntity,
        public personens?: BaseEntity[],
    ) {
    }
}
