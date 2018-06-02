import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { AdresKkf } from './adres-kkf.model';
import { AdresKkfService } from './adres-kkf.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';

@Component({
    selector: 'jhi-adres-kkf',
    templateUrl: './adres-kkf.component.html'
})
export class AdresKkfComponent implements OnInit, OnDestroy {

    adres: AdresKkf[];
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    queryCount: any;
    reverse: any;
    totalItems: number;
    currentSearch: string;

    constructor(
        private adresService: AdresKkfService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.adres = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.adresService.search({
                query: this.currentSearch,
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            }).subscribe(
                (res: HttpResponse<AdresKkf[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
            return;
        }
        this.adresService.query({
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort()
        }).subscribe(
            (res: HttpResponse<AdresKkf[]>) => this.onSuccess(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    reset() {
        this.page = 0;
        this.adres = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.adres = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = '';
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.adres = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = '_score';
        this.reverse = false;
        this.currentSearch = query;
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInAdres();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: AdresKkf) {
        return item.id;
    }
    registerChangeInAdres() {
        this.eventSubscriber = this.eventManager.subscribe('adresListModification', (response) => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        for (let i = 0; i < data.length; i++) {
            this.adres.push(data[i]);
        }
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
