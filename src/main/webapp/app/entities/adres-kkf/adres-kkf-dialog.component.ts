import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AdresKkf } from './adres-kkf.model';
import { AdresKkfPopupService } from './adres-kkf-popup.service';
import { AdresKkfService } from './adres-kkf.service';
import { AdresTypeKkf, AdresTypeKkfService } from '../adres-type-kkf';

@Component({
    selector: 'jhi-adres-kkf-dialog',
    templateUrl: './adres-kkf-dialog.component.html'
})
export class AdresKkfDialogComponent implements OnInit {

    adres: AdresKkf;
    isSaving: boolean;

    adrestypes: AdresTypeKkf[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private adresService: AdresKkfService,
        private adresTypeService: AdresTypeKkfService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.adresTypeService.query()
            .subscribe((res: HttpResponse<AdresTypeKkf[]>) => { this.adrestypes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.adres.id !== undefined) {
            this.subscribeToSaveResponse(
                this.adresService.update(this.adres));
        } else {
            this.subscribeToSaveResponse(
                this.adresService.create(this.adres));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<AdresKkf>>) {
        result.subscribe((res: HttpResponse<AdresKkf>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: AdresKkf) {
        this.eventManager.broadcast({ name: 'adresListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackAdresTypeById(index: number, item: AdresTypeKkf) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-adres-kkf-popup',
    template: ''
})
export class AdresKkfPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private adresPopupService: AdresKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.adresPopupService
                    .open(AdresKkfDialogComponent as Component, params['id']);
            } else {
                this.adresPopupService
                    .open(AdresKkfDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
