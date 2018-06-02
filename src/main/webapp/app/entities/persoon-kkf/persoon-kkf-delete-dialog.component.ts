import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { PersoonKkf } from './persoon-kkf.model';
import { PersoonKkfPopupService } from './persoon-kkf-popup.service';
import { PersoonKkfService } from './persoon-kkf.service';

@Component({
    selector: 'jhi-persoon-kkf-delete-dialog',
    templateUrl: './persoon-kkf-delete-dialog.component.html'
})
export class PersoonKkfDeleteDialogComponent {

    persoon: PersoonKkf;

    constructor(
        private persoonService: PersoonKkfService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.persoonService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'persoonListModification',
                content: 'Deleted an persoon'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-persoon-kkf-delete-popup',
    template: ''
})
export class PersoonKkfDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private persoonPopupService: PersoonKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.persoonPopupService
                .open(PersoonKkfDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
