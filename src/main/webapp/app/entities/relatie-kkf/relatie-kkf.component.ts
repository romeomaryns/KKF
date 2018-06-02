import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { RelatieKkf } from './relatie-kkf.model';
import { RelatieKkfService } from './relatie-kkf.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';

@Component({
    selector: 'jhi-relatie-kkf',
    templateUrl: './relatie-kkf.component.html'
})
export class RelatieKkfComponent implements OnInit, OnDestroy {

    relaties: RelatieKkf[];
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
        private relatieService: RelatieKkfService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.relaties = [];
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
            this.relatieService.search({
                query: this.currentSearch,
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            }).subscribe(
                (res: HttpResponse<RelatieKkf[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
            return;
        }
        this.relatieService.query({
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort()
        }).subscribe(
            (res: HttpResponse<RelatieKkf[]>) => this.onSuccess(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    reset() {
        this.page = 0;
        this.relaties = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.relaties = [];
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
        this.relaties = [];
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
        this.registerChangeInRelaties();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: RelatieKkf) {
        return item.id;
    }
    registerChangeInRelaties() {
        this.eventSubscriber = this.eventManager.subscribe('relatieListModification', (response) => this.reset());
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
            this.relaties.push(data[i]);
        }
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
