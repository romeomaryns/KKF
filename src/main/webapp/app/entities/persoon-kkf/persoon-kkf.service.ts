import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { PersoonKkf } from './persoon-kkf.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<PersoonKkf>;

@Injectable()
export class PersoonKkfService {

    private resourceUrl =  SERVER_API_URL + 'api/persoons';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/persoons';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(persoon: PersoonKkf): Observable<EntityResponseType> {
        const copy = this.convert(persoon);
        return this.http.post<PersoonKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(persoon: PersoonKkf): Observable<EntityResponseType> {
        const copy = this.convert(persoon);
        return this.http.put<PersoonKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<PersoonKkf>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<PersoonKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<PersoonKkf[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<PersoonKkf[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<PersoonKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<PersoonKkf[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<PersoonKkf[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: PersoonKkf = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<PersoonKkf[]>): HttpResponse<PersoonKkf[]> {
        const jsonResponse: PersoonKkf[] = res.body;
        const body: PersoonKkf[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to PersoonKkf.
     */
    private convertItemFromServer(persoon: PersoonKkf): PersoonKkf {
        const copy: PersoonKkf = Object.assign({}, persoon);
        copy.geboortedatum = this.dateUtils
            .convertLocalDateFromServer(persoon.geboortedatum);
        return copy;
    }

    /**
     * Convert a PersoonKkf to a JSON which can be sent to the server.
     */
    private convert(persoon: PersoonKkf): PersoonKkf {
        const copy: PersoonKkf = Object.assign({}, persoon);
        copy.geboortedatum = this.dateUtils
            .convertLocalDateToServer(persoon.geboortedatum);
        return copy;
    }
}
