import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { GeslachtKkf } from './geslacht-kkf.model';
import { GeslachtKkfPopupService } from './geslacht-kkf-popup.service';
import { GeslachtKkfService } from './geslacht-kkf.service';

@Component({
    selector: 'jhi-geslacht-kkf-dialog',
    templateUrl: './geslacht-kkf-dialog.component.html'
})
export class GeslachtKkfDialogComponent implements OnInit {

    geslacht: GeslachtKkf;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private geslachtService: GeslachtKkfService,
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
        if (this.geslacht.id !== undefined) {
            this.subscribeToSaveResponse(
                this.geslachtService.update(this.geslacht));
        } else {
            this.subscribeToSaveResponse(
                this.geslachtService.create(this.geslacht));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<GeslachtKkf>>) {
        result.subscribe((res: HttpResponse<GeslachtKkf>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: GeslachtKkf) {
        this.eventManager.broadcast({ name: 'geslachtListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-geslacht-kkf-popup',
    template: ''
})
export class GeslachtKkfPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private geslachtPopupService: GeslachtKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.geslachtPopupService
                    .open(GeslachtKkfDialogComponent as Component, params['id']);
            } else {
                this.geslachtPopupService
                    .open(GeslachtKkfDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
