import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { GeslachtKkf } from './geslacht-kkf.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<GeslachtKkf>;

@Injectable()
export class GeslachtKkfService {

    private resourceUrl =  SERVER_API_URL + 'api/geslachts';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/geslachts';

    constructor(private http: HttpClient) { }

    create(geslacht: GeslachtKkf): Observable<EntityResponseType> {
        const copy = this.convert(geslacht);
        return this.http.post<GeslachtKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(geslacht: GeslachtKkf): Observable<EntityResponseType> {
        const copy = this.convert(geslacht);
        return this.http.put<GeslachtKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<GeslachtKkf>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<GeslachtKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<GeslachtKkf[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<GeslachtKkf[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<GeslachtKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<GeslachtKkf[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<GeslachtKkf[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: GeslachtKkf = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<GeslachtKkf[]>): HttpResponse<GeslachtKkf[]> {
        const jsonResponse: GeslachtKkf[] = res.body;
        const body: GeslachtKkf[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to GeslachtKkf.
     */
    private convertItemFromServer(geslacht: GeslachtKkf): GeslachtKkf {
        const copy: GeslachtKkf = Object.assign({}, geslacht);
        return copy;
    }

    /**
     * Convert a GeslachtKkf to a JSON which can be sent to the server.
     */
    private convert(geslacht: GeslachtKkf): GeslachtKkf {
        const copy: GeslachtKkf = Object.assign({}, geslacht);
        return copy;
    }
}
