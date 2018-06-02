import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ContactKkf } from './contact-kkf.model';
import { ContactKkfService } from './contact-kkf.service';

@Component({
    selector: 'jhi-contact-kkf-detail',
    templateUrl: './contact-kkf-detail.component.html'
})
export class ContactKkfDetailComponent implements OnInit, OnDestroy {

    contact: ContactKkf;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private contactService: ContactKkfService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInContacts();
    }

    load(id) {
        this.contactService.find(id)
            .subscribe((contactResponse: HttpResponse<ContactKkf>) => {
                this.contact = contactResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInContacts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'contactListModification',
            (response) => this.load(this.contact.id)
        );
    }
}
