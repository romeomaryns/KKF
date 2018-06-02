import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RelatieTypeKkf } from './relatie-type-kkf.model';
import { RelatieTypeKkfPopupService } from './relatie-type-kkf-popup.service';
import { RelatieTypeKkfService } from './relatie-type-kkf.service';

@Component({
    selector: 'jhi-relatie-type-kkf-dialog',
    templateUrl: './relatie-type-kkf-dialog.component.html'
})
export class RelatieTypeKkfDialogComponent implements OnInit {

    relatieType: RelatieTypeKkf;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private relatieTypeService: RelatieTypeKkfService,
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
        if (this.relatieType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.relatieTypeService.update(this.relatieType));
        } else {
            this.subscribeToSaveResponse(
                this.relatieTypeService.create(this.relatieType));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<RelatieTypeKkf>>) {
        result.subscribe((res: HttpResponse<RelatieTypeKkf>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: RelatieTypeKkf) {
        this.eventManager.broadcast({ name: 'relatieTypeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-relatie-type-kkf-popup',
    template: ''
})
export class RelatieTypeKkfPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private relatieTypePopupService: RelatieTypeKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.relatieTypePopupService
                    .open(RelatieTypeKkfDialogComponent as Component, params['id']);
            } else {
                this.relatieTypePopupService
                    .open(RelatieTypeKkfDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
