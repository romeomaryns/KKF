import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ContactTypeKkf } from './contact-type-kkf.model';
import { ContactTypeKkfService } from './contact-type-kkf.service';

@Component({
    selector: 'jhi-contact-type-kkf-detail',
    templateUrl: './contact-type-kkf-detail.component.html'
})
export class ContactTypeKkfDetailComponent implements OnInit, OnDestroy {

    contactType: ContactTypeKkf;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private contactTypeService: ContactTypeKkfService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInContactTypes();
    }

    load(id) {
        this.contactTypeService.find(id)
            .subscribe((contactTypeResponse: HttpResponse<ContactTypeKkf>) => {
                this.contactType = contactTypeResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInContactTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'contactTypeListModification',
            (response) => this.load(this.contactType.id)
        );
    }
}
