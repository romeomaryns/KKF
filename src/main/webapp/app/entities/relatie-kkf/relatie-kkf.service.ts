import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { RelatieKkf } from './relatie-kkf.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<RelatieKkf>;

@Injectable()
export class RelatieKkfService {

    private resourceUrl =  SERVER_API_URL + 'api/relaties';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/relaties';

    constructor(private http: HttpClient) { }

    create(relatie: RelatieKkf): Observable<EntityResponseType> {
        const copy = this.convert(relatie);
        return this.http.post<RelatieKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(relatie: RelatieKkf): Observable<EntityResponseType> {
        const copy = this.convert(relatie);
        return this.http.put<RelatieKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<RelatieKkf>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<RelatieKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<RelatieKkf[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<RelatieKkf[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<RelatieKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<RelatieKkf[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<RelatieKkf[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: RelatieKkf = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<RelatieKkf[]>): HttpResponse<RelatieKkf[]> {
        const jsonResponse: RelatieKkf[] = res.body;
        const body: RelatieKkf[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to RelatieKkf.
     */
    private convertItemFromServer(relatie: RelatieKkf): RelatieKkf {
        const copy: RelatieKkf = Object.assign({}, relatie);
        return copy;
    }

    /**
     * Convert a RelatieKkf to a JSON which can be sent to the server.
     */
    private convert(relatie: RelatieKkf): RelatieKkf {
        const copy: RelatieKkf = Object.assign({}, relatie);
        return copy;
    }
}
