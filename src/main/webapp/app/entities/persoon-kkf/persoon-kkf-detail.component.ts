import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { PersoonKkf } from './persoon-kkf.model';
import { PersoonKkfService } from './persoon-kkf.service';

@Component({
    selector: 'jhi-persoon-kkf-detail',
    templateUrl: './persoon-kkf-detail.component.html'
})
export class PersoonKkfDetailComponent implements OnInit, OnDestroy {

    persoon: PersoonKkf;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private persoonService: PersoonKkfService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPersoons();
    }

    load(id) {
        this.persoonService.find(id)
            .subscribe((persoonResponse: HttpResponse<PersoonKkf>) => {
                this.persoon = persoonResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPersoons() {
        this.eventSubscriber = this.eventManager.subscribe(
            'persoonListModification',
            (response) => this.load(this.persoon.id)
        );
    }
}
