import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { GeslachtKkf } from './geslacht-kkf.model';
import { GeslachtKkfPopupService } from './geslacht-kkf-popup.service';
import { GeslachtKkfService } from './geslacht-kkf.service';

@Component({
    selector: 'jhi-geslacht-kkf-delete-dialog',
    templateUrl: './geslacht-kkf-delete-dialog.component.html'
})
export class GeslachtKkfDeleteDialogComponent {

    geslacht: GeslachtKkf;

    constructor(
        private geslachtService: GeslachtKkfService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.geslachtService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'geslachtListModification',
                content: 'Deleted an geslacht'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-geslacht-kkf-delete-popup',
    template: ''
})
export class GeslachtKkfDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private geslachtPopupService: GeslachtKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.geslachtPopupService
                .open(GeslachtKkfDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
