import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RelatieTypeKkf } from './relatie-type-kkf.model';
import { RelatieTypeKkfPopupService } from './relatie-type-kkf-popup.service';
import { RelatieTypeKkfService } from './relatie-type-kkf.service';

@Component({
    selector: 'jhi-relatie-type-kkf-delete-dialog',
    templateUrl: './relatie-type-kkf-delete-dialog.component.html'
})
export class RelatieTypeKkfDeleteDialogComponent {

    relatieType: RelatieTypeKkf;

    constructor(
        private relatieTypeService: RelatieTypeKkfService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.relatieTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'relatieTypeListModification',
                content: 'Deleted an relatieType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-relatie-type-kkf-delete-popup',
    template: ''
})
export class RelatieTypeKkfDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private relatieTypePopupService: RelatieTypeKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.relatieTypePopupService
                .open(RelatieTypeKkfDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
