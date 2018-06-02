import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { RelatieKkf } from './relatie-kkf.model';
import { RelatieKkfService } from './relatie-kkf.service';

@Component({
    selector: 'jhi-relatie-kkf-detail',
    templateUrl: './relatie-kkf-detail.component.html'
})
export class RelatieKkfDetailComponent implements OnInit, OnDestroy {

    relatie: RelatieKkf;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private relatieService: RelatieKkfService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRelaties();
    }

    load(id) {
        this.relatieService.find(id)
            .subscribe((relatieResponse: HttpResponse<RelatieKkf>) => {
                this.relatie = relatieResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRelaties() {
        this.eventSubscriber = this.eventManager.subscribe(
            'relatieListModification',
            (response) => this.load(this.relatie.id)
        );
    }
}
