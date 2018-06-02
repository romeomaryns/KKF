import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AdresTypeKkf } from './adres-type-kkf.model';
import { AdresTypeKkfPopupService } from './adres-type-kkf-popup.service';
import { AdresTypeKkfService } from './adres-type-kkf.service';

@Component({
    selector: 'jhi-adres-type-kkf-dialog',
    templateUrl: './adres-type-kkf-dialog.component.html'
})
export class AdresTypeKkfDialogComponent implements OnInit {

    adresType: AdresTypeKkf;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private adresTypeService: AdresTypeKkfService,
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
        if (this.adresType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.adresTypeService.update(this.adresType));
        } else {
            this.subscribeToSaveResponse(
                this.adresTypeService.create(this.adresType));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<AdresTypeKkf>>) {
        result.subscribe((res: HttpResponse<AdresTypeKkf>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: AdresTypeKkf) {
        this.eventManager.broadcast({ name: 'adresTypeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-adres-type-kkf-popup',
    template: ''
})
export class AdresTypeKkfPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private adresTypePopupService: AdresTypeKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.adresTypePopupService
                    .open(AdresTypeKkfDialogComponent as Component, params['id']);
            } else {
                this.adresTypePopupService
                    .open(AdresTypeKkfDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
