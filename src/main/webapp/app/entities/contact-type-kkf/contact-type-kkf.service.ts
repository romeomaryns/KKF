import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { ContactTypeKkf } from './contact-type-kkf.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ContactTypeKkf>;

@Injectable()
export class ContactTypeKkfService {

    private resourceUrl =  SERVER_API_URL + 'api/contact-types';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/contact-types';

    constructor(private http: HttpClient) { }

    create(contactType: ContactTypeKkf): Observable<EntityResponseType> {
        const copy = this.convert(contactType);
        return this.http.post<ContactTypeKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(contactType: ContactTypeKkf): Observable<EntityResponseType> {
        const copy = this.convert(contactType);
        return this.http.put<ContactTypeKkf>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ContactTypeKkf>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ContactTypeKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<ContactTypeKkf[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ContactTypeKkf[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<ContactTypeKkf[]>> {
        const options = createRequestOption(req);
        return this.http.get<ContactTypeKkf[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ContactTypeKkf[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ContactTypeKkf = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ContactTypeKkf[]>): HttpResponse<ContactTypeKkf[]> {
        const jsonResponse: ContactTypeKkf[] = res.body;
        const body: ContactTypeKkf[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ContactTypeKkf.
     */
    private convertItemFromServer(contactType: ContactTypeKkf): ContactTypeKkf {
        const copy: ContactTypeKkf = Object.assign({}, contactType);
        return copy;
    }

    /**
     * Convert a ContactTypeKkf to a JSON which can be sent to the server.
     */
    private convert(contactType: ContactTypeKkf): ContactTypeKkf {
        const copy: ContactTypeKkf = Object.assign({}, contactType);
        return copy;
    }
}
