import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { RelatieTypeKkf } from './relatie-type-kkf.model';
import { RelatieTypeKkfService } from './relatie-type-kkf.service';

@Component({
    selector: 'jhi-relatie-type-kkf-detail',
    templateUrl: './relatie-type-kkf-detail.component.html'
})
export class RelatieTypeKkfDetailComponent implements OnInit, OnDestroy {

    relatieType: RelatieTypeKkf;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private relatieTypeService: RelatieTypeKkfService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRelatieTypes();
    }

    load(id) {
        this.relatieTypeService.find(id)
            .subscribe((relatieTypeResponse: HttpResponse<RelatieTypeKkf>) => {
                this.relatieType = relatieTypeResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRelatieTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'relatieTypeListModification',
            (response) => this.load(this.relatieType.id)
        );
    }
}
