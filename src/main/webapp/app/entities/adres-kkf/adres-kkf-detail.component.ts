import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { AdresKkf } from './adres-kkf.model';
import { AdresKkfService } from './adres-kkf.service';

@Component({
    selector: 'jhi-adres-kkf-detail',
    templateUrl: './adres-kkf-detail.component.html'
})
export class AdresKkfDetailComponent implements OnInit, OnDestroy {

    adres: AdresKkf;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private adresService: AdresKkfService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAdres();
    }

    load(id) {
        this.adresService.find(id)
            .subscribe((adresResponse: HttpResponse<AdresKkf>) => {
                this.adres = adresResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAdres() {
        this.eventSubscriber = this.eventManager.subscribe(
            'adresListModification',
            (response) => this.load(this.adres.id)
        );
    }
}
