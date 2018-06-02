import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { ContactTypeKkf } from './contact-type-kkf.model';
import { ContactTypeKkfService } from './contact-type-kkf.service';

@Injectable()
export class ContactTypeKkfPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private contactTypeService: ContactTypeKkfService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.contactTypeService.find(id)
                    .subscribe((contactTypeResponse: HttpResponse<ContactTypeKkf>) => {
                        const contactType: ContactTypeKkf = contactTypeResponse.body;
                        this.ngbModalRef = this.contactTypeModalRef(component, contactType);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.contactTypeModalRef(component, new ContactTypeKkf());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    contactTypeModalRef(component: Component, contactType: ContactTypeKkf): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.contactType = contactType;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
