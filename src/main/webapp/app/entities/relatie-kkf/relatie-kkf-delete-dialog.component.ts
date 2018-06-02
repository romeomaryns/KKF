import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RelatieKkf } from './relatie-kkf.model';
import { RelatieKkfPopupService } from './relatie-kkf-popup.service';
import { RelatieKkfService } from './relatie-kkf.service';

@Component({
    selector: 'jhi-relatie-kkf-delete-dialog',
    templateUrl: './relatie-kkf-delete-dialog.component.html'
})
export class RelatieKkfDeleteDialogComponent {

    relatie: RelatieKkf;

    constructor(
        private relatieService: RelatieKkfService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.relatieService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'relatieListModification',
                content: 'Deleted an relatie'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-relatie-kkf-delete-popup',
    template: ''
})
export class RelatieKkfDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private relatiePopupService: RelatieKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.relatiePopupService
                .open(RelatieKkfDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
