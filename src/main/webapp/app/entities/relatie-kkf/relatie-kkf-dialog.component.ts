import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { RelatieKkf } from './relatie-kkf.model';
import { RelatieKkfPopupService } from './relatie-kkf-popup.service';
import { RelatieKkfService } from './relatie-kkf.service';
import { RelatieTypeKkf, RelatieTypeKkfService } from '../relatie-type-kkf';
import { PersoonKkf, PersoonKkfService } from '../persoon-kkf';

@Component({
    selector: 'jhi-relatie-kkf-dialog',
    templateUrl: './relatie-kkf-dialog.component.html'
})
export class RelatieKkfDialogComponent implements OnInit {

    relatie: RelatieKkf;
    isSaving: boolean;

    relatietypes: RelatieTypeKkf[];

    persoons: PersoonKkf[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private relatieService: RelatieKkfService,
        private relatieTypeService: RelatieTypeKkfService,
        private persoonService: PersoonKkfService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.relatieTypeService.query()
            .subscribe((res: HttpResponse<RelatieTypeKkf[]>) => { this.relatietypes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.persoonService.query()
            .subscribe((res: HttpResponse<PersoonKkf[]>) => { this.persoons = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.relatie.id !== undefined) {
            this.subscribeToSaveResponse(
                this.relatieService.update(this.relatie));
        } else {
            this.subscribeToSaveResponse(
                this.relatieService.create(this.relatie));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<RelatieKkf>>) {
        result.subscribe((res: HttpResponse<RelatieKkf>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: RelatieKkf) {
        this.eventManager.broadcast({ name: 'relatieListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackRelatieTypeById(index: number, item: RelatieTypeKkf) {
        return item.id;
    }

    trackPersoonById(index: number, item: PersoonKkf) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-relatie-kkf-popup',
    template: ''
})
export class RelatieKkfPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private relatiePopupService: RelatieKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.relatiePopupService
                    .open(RelatieKkfDialogComponent as Component, params['id']);
            } else {
                this.relatiePopupService
                    .open(RelatieKkfDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
