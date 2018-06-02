import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { PersoonKkf } from './persoon-kkf.model';
import { PersoonKkfPopupService } from './persoon-kkf-popup.service';
import { PersoonKkfService } from './persoon-kkf.service';
import { ContactKkf, ContactKkfService } from '../contact-kkf';
import { GeslachtKkf, GeslachtKkfService } from '../geslacht-kkf';
import { AdresKkf, AdresKkfService } from '../adres-kkf';
import { RelatieKkf, RelatieKkfService } from '../relatie-kkf';

@Component({
    selector: 'jhi-persoon-kkf-dialog',
    templateUrl: './persoon-kkf-dialog.component.html'
})
export class PersoonKkfDialogComponent implements OnInit {

    persoon: PersoonKkf;
    isSaving: boolean;

    contacts: ContactKkf[];

    geslachts: GeslachtKkf[];

    adres: AdresKkf[];

    relaties: RelatieKkf[];
    geboortedatumDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private persoonService: PersoonKkfService,
        private contactService: ContactKkfService,
        private geslachtService: GeslachtKkfService,
        private adresService: AdresKkfService,
        private relatieService: RelatieKkfService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.contactService.query()
            .subscribe((res: HttpResponse<ContactKkf[]>) => { this.contacts = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.geslachtService.query()
            .subscribe((res: HttpResponse<GeslachtKkf[]>) => { this.geslachts = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.adresService.query()
            .subscribe((res: HttpResponse<AdresKkf[]>) => { this.adres = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.relatieService.query()
            .subscribe((res: HttpResponse<RelatieKkf[]>) => { this.relaties = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.persoon.id !== undefined) {
            this.subscribeToSaveResponse(
                this.persoonService.update(this.persoon));
        } else {
            this.subscribeToSaveResponse(
                this.persoonService.create(this.persoon));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<PersoonKkf>>) {
        result.subscribe((res: HttpResponse<PersoonKkf>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: PersoonKkf) {
        this.eventManager.broadcast({ name: 'persoonListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackContactById(index: number, item: ContactKkf) {
        return item.id;
    }

    trackGeslachtById(index: number, item: GeslachtKkf) {
        return item.id;
    }

    trackAdresById(index: number, item: AdresKkf) {
        return item.id;
    }

    trackRelatieById(index: number, item: RelatieKkf) {
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
    selector: 'jhi-persoon-kkf-popup',
    template: ''
})
export class PersoonKkfPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private persoonPopupService: PersoonKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.persoonPopupService
                    .open(PersoonKkfDialogComponent as Component, params['id']);
            } else {
                this.persoonPopupService
                    .open(PersoonKkfDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
