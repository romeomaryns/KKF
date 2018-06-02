import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { ContactKkf } from './contact-kkf.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ContactKkf>;

@Injectable()
export class ContactKkfService {

    private resourceUrl =  SERVER_API_URL + 'api/contacts';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/contacts';

    constructor(private http: HttpClient) { }

    create(contact: ContactKkf): Observable<EntityResponseType> {
        const copy = this.convert(contact);
        return this.http.post<ContactKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(contact: ContactKkf): Observable<EntityResponseType> {
        const copy = this.convert(contact);
        return this.http.put<ContactKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ContactKkf>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ContactKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<ContactKkf[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ContactKkf[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<ContactKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<ContactKkf[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ContactKkf[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ContactKkf = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ContactKkf[]>): HttpResponse<ContactKkf[]> {
        const jsonResponse: ContactKkf[] = res.body;
        const body: ContactKkf[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ContactKkf.
     */
    private convertItemFromServer(contact: ContactKkf): ContactKkf {
        const copy: ContactKkf = Object.assign({}, contact);
        return copy;
    }

    /**
     * Convert a ContactKkf to a JSON which can be sent to the server.
     */
    private convert(contact: ContactKkf): ContactKkf {
        const copy: ContactKkf = Object.assign({}, contact);
        return copy;
    }
}
