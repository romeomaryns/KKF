import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AdresKkf } from './adres-kkf.model';
import { AdresKkfPopupService } from './adres-kkf-popup.service';
import { AdresKkfService } from './adres-kkf.service';

@Component({
    selector: 'jhi-adres-kkf-delete-dialog',
    templateUrl: './adres-kkf-delete-dialog.component.html'
})
export class AdresKkfDeleteDialogComponent {

    adres: AdresKkf;

    constructor(
        private adresService: AdresKkfService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.adresService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'adresListModification',
                content: 'Deleted an adres'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-adres-kkf-delete-popup',
    template: ''
})
export class AdresKkfDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private adresPopupService: AdresKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.adresPopupService
                .open(AdresKkfDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
