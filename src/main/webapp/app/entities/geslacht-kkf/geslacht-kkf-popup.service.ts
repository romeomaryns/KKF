import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { GeslachtKkf } from './geslacht-kkf.model';
import { GeslachtKkfService } from './geslacht-kkf.service';

@Injectable()
export class GeslachtKkfPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private geslachtService: GeslachtKkfService

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
                this.geslachtService.find(id)
                    .subscribe((geslachtResponse: HttpResponse<GeslachtKkf>) => {
                        const geslacht: GeslachtKkf = geslachtResponse.body;
                        this.ngbModalRef = this.geslachtModalRef(component, geslacht);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.geslachtModalRef(component, new GeslachtKkf());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    geslachtModalRef(component: Component, geslacht: GeslachtKkf): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.geslacht = geslacht;
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
