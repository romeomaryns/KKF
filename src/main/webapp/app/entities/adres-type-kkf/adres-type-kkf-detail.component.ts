import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { AdresTypeKkf } from './adres-type-kkf.model';
import { AdresTypeKkfService } from './adres-type-kkf.service';

@Component({
    selector: 'jhi-adres-type-kkf-detail',
    templateUrl: './adres-type-kkf-detail.component.html'
})
export class AdresTypeKkfDetailComponent implements OnInit, OnDestroy {

    adresType: AdresTypeKkf;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private adresTypeService: AdresTypeKkfService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAdresTypes();
    }

    load(id) {
        this.adresTypeService.find(id)
            .subscribe((adresTypeResponse: HttpResponse<AdresTypeKkf>) => {
                this.adresType = adresTypeResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAdresTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'adresTypeListModification',
            (response) => this.load(this.adresType.id)
        );
    }
}
