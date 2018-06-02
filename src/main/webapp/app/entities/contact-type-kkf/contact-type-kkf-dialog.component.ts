import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ContactTypeKkf } from './contact-type-kkf.model';
import { ContactTypeKkfPopupService } from './contact-type-kkf-popup.service';
import { ContactTypeKkfService } from './contact-type-kkf.service';

@Component({
    selector: 'jhi-contact-type-kkf-dialog',
    templateUrl: './contact-type-kkf-dialog.component.html'
})
export class ContactTypeKkfDialogComponent implements OnInit {

    contactType: ContactTypeKkf;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private contactTypeService: ContactTypeKkfService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.contactType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.contactTypeService.update(this.contactType));
        } else {
            this.subscribeToSaveResponse(
                this.contactTypeService.create(this.contactType));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ContactTypeKkf>>) {
        result.subscribe((res: HttpResponse<ContactTypeKkf>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ContactTypeKkf) {
        this.eventManager.broadcast({ name: 'contactTypeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-contact-type-kkf-popup',
    template: ''
})
export class ContactTypeKkfPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contactTypePopupService: ContactTypeKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.contactTypePopupService
                    .open(ContactTypeKkfDialogComponent as Component, params['id']);
            } else {
                this.contactTypePopupService
                    .open(ContactTypeKkfDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
