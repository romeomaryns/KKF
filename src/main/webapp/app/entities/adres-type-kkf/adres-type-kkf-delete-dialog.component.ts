import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AdresTypeKkf } from './adres-type-kkf.model';
import { AdresTypeKkfPopupService } from './adres-type-kkf-popup.service';
import { AdresTypeKkfService } from './adres-type-kkf.service';

@Component({
    selector: 'jhi-adres-type-kkf-delete-dialog',
    templateUrl: './adres-type-kkf-delete-dialog.component.html'
})
export class AdresTypeKkfDeleteDialogComponent {

    adresType: AdresTypeKkf;

    constructor(
        private adresTypeService: AdresTypeKkfService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.adresTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'adresTypeListModification',
                content: 'Deleted an adresType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-adres-type-kkf-delete-popup',
    template: ''
})
export class AdresTypeKkfDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private adresTypePopupService: AdresTypeKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.adresTypePopupService
                .open(AdresTypeKkfDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
