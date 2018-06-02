import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { AdresTypeKkf } from './adres-type-kkf.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<AdresTypeKkf>;

@Injectable()
export class AdresTypeKkfService {

    private resourceUrl =  SERVER_API_URL + 'api/adres-types';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/adres-types';

    constructor(private http: HttpClient) { }

    create(adresType: AdresTypeKkf): Observable<EntityResponseType> {
        const copy = this.convert(adresType);
        return this.http.post<AdresTypeKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(adresType: AdresTypeKkf): Observable<EntityResponseType> {
        const copy = this.convert(adresType);
        return this.http.put<AdresTypeKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<AdresTypeKkf>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<AdresTypeKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<AdresTypeKkf[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<AdresTypeKkf[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<AdresTypeKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<AdresTypeKkf[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<AdresTypeKkf[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: AdresTypeKkf = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<AdresTypeKkf[]>): HttpResponse<AdresTypeKkf[]> {
        const jsonResponse: AdresTypeKkf[] = res.body;
        const body: AdresTypeKkf[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to AdresTypeKkf.
     */
    private convertItemFromServer(adresType: AdresTypeKkf): AdresTypeKkf {
        const copy: AdresTypeKkf = Object.assign({}, adresType);
        return copy;
    }

    /**
     * Convert a AdresTypeKkf to a JSON which can be sent to the server.
     */
    private convert(adresType: AdresTypeKkf): AdresTypeKkf {
        const copy: AdresTypeKkf = Object.assign({}, adresType);
        return copy;
    }
}
