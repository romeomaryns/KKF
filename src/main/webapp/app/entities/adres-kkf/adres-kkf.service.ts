import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { AdresKkf } from './adres-kkf.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<AdresKkf>;

@Injectable()
export class AdresKkfService {

    private resourceUrl =  SERVER_API_URL + 'api/adres';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/adres';

    constructor(private http: HttpClient) { }

    create(adres: AdresKkf): Observable<EntityResponseType> {
        const copy = this.convert(adres);
        return this.http.post<AdresKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(adres: AdresKkf): Observable<EntityResponseType> {
        const copy = this.convert(adres);
        return this.http.put<AdresKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<AdresKkf>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<AdresKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<AdresKkf[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<AdresKkf[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<AdresKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<AdresKkf[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<AdresKkf[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: AdresKkf = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<AdresKkf[]>): HttpResponse<AdresKkf[]> {
        const jsonResponse: AdresKkf[] = res.body;
        const body: AdresKkf[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to AdresKkf.
     */
    private convertItemFromServer(adres: AdresKkf): AdresKkf {
        const copy: AdresKkf = Object.assign({}, adres);
        return copy;
    }

    /**
     * Convert a AdresKkf to a JSON which can be sent to the server.
     */
    private convert(adres: AdresKkf): AdresKkf {
        const copy: AdresKkf = Object.assign({}, adres);
        return copy;
    }
}
