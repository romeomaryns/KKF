import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ContactKkf } from './contact-kkf.model';
import { ContactKkfPopupService } from './contact-kkf-popup.service';
import { ContactKkfService } from './contact-kkf.service';

@Component({
    selector: 'jhi-contact-kkf-delete-dialog',
    templateUrl: './contact-kkf-delete-dialog.component.html'
})
export class ContactKkfDeleteDialogComponent {

    contact: ContactKkf;

    constructor(
        private contactService: ContactKkfService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.contactService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'contactListModification',
                content: 'Deleted an contact'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-contact-kkf-delete-popup',
    template: ''
})
export class ContactKkfDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contactPopupService: ContactKkfPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.contactPopupService
                .open(ContactKkfDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
