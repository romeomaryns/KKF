import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ContactTypeKkf } from './contact-type-kkf.model';
import { ContactTypeKkfPopupService } from './contact-type-kkf-popup.service';
import { ContactTypeKkfService } from './contact-type-kkf.service';

@Component({
    selector: 'jhi-contact-type-kkf-delete-dialog',
    templateUrl: './contact-type-kkf-delete-dialog.component.html'
})
export class ContactTypeKkfDeleteDialogComponent {

    contactType: ContactTypeKkf;

    constructor(
        private contactTypeService: ContactTypeKkfService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.contactTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'contactTypeListModification',
                content: 'Deleted an contactType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-contact-type-kkf-delete-popup',
    template: ''
})
export class ContactTypeKkfDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contactTypePopupService: ContactTypeKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.contactTypePopupService
                .open(ContactTypeKkfDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
