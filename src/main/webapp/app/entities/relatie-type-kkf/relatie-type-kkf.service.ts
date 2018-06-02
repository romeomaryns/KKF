import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { RelatieTypeKkf } from './relatie-type-kkf.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<RelatieTypeKkf>;

@Injectable()
export class RelatieTypeKkfService {

    private resourceUrl =  SERVER_API_URL + 'api/relatie-types';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/relatie-types';

    constructor(private http: HttpClient) { }

    create(relatieType: RelatieTypeKkf): Observable<EntityResponseType> {
        const copy = this.convert(relatieType);
        return this.http.post<RelatieTypeKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(relatieType: RelatieTypeKkf): Observable<EntityResponseType> {
        const copy = this.convert(relatieType);
        return this.http.put<RelatieTypeKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<RelatieTypeKkf>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<RelatieTypeKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<RelatieTypeKkf[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<RelatieTypeKkf[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<RelatieTypeKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<RelatieTypeKkf[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<RelatieTypeKkf[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: RelatieTypeKkf = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<RelatieTypeKkf[]>): HttpResponse<RelatieTypeKkf[]> {
        const jsonResponse: RelatieTypeKkf[] = res.body;
        const body: RelatieTypeKkf[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to RelatieTypeKkf.
     */
    private convertItemFromServer(relatieType: RelatieTypeKkf): RelatieTypeKkf {
        const copy: RelatieTypeKkf = Object.assign({}, relatieType);
        return copy;
    }

    /**
     * Convert a RelatieTypeKkf to a JSON which can be sent to the server.
     */
    private convert(relatieType: RelatieTypeKkf): RelatieTypeKkf {
        const copy: RelatieTypeKkf = Object.assign({}, relatieType);
        return copy;
    }
}
