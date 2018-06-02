import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { GeslachtKkf } from './geslacht-kkf.model';
import { GeslachtKkfService } from './geslacht-kkf.service';

@Component({
    selector: 'jhi-geslacht-kkf-detail',
    templateUrl: './geslacht-kkf-detail.component.html'
})
export class GeslachtKkfDetailComponent implements OnInit, OnDestroy {

    geslacht: GeslachtKkf;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private geslachtService: GeslachtKkfService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInGeslachts();
    }

    load(id) {
        this.geslachtService.find(id)
            .subscribe((geslachtResponse: HttpResponse<GeslachtKkf>) => {
                this.geslacht = geslachtResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInGeslachts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'geslachtListModification',
            (response) => this.load(this.geslacht.id)
        );
    }
}
