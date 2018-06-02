import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ContactKkf } from './contact-kkf.model';
import { ContactKkfPopupService } from './contact-kkf-popup.service';
import { ContactKkfService } from './contact-kkf.service';
import { ContactTypeKkf, ContactTypeKkfService } from '../contact-type-kkf';

@Component({
    selector: 'jhi-contact-kkf-dialog',
    templateUrl: './contact-kkf-dialog.component.html'
})
export class ContactKkfDialogComponent implements OnInit {

    contact: ContactKkf;
    isSaving: boolean;

    contacttypes: ContactTypeKkf[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private contactService: ContactKkfService,
        private contactTypeService: ContactTypeKkfService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.contactTypeService.query()
            .subscribe((res: HttpResponse<ContactTypeKkf[]>) => { this.contacttypes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.contact.id !== undefined) {
            this.subscribeToSaveResponse(
                this.contactService.update(this.contact));
        } else {
            this.subscribeToSaveResponse(
                this.contactService.create(this.contact));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ContactKkf>>) {
        result.subscribe((res: HttpResponse<ContactKkf>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ContactKkf) {
        this.eventManager.broadcast({ name: 'contactListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackContactTypeById(index: number, item: ContactTypeKkf) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-contact-kkf-popup',
    template: ''
})
export class ContactKkfPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contactPopupService: ContactKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.contactPopupService
                    .open(ContactKkfDialogComponent as Component, params['id']);
            } else {
                this.contactPopupService
                    .open(ContactKkfDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
