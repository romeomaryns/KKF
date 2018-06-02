import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { AdresTypeKkf } from './adres-type-kkf.model';
import { AdresTypeKkfService } from './adres-type-kkf.service';

@Injectable()
export class AdresTypeKkfPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private adresTypeService: AdresTypeKkfService

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
                this.adresTypeService.find(id)
                    .subscribe((adresTypeResponse: HttpResponse<AdresTypeKkf>) => {
                        const adresType: AdresTypeKkf = adresTypeResponse.body;
                        this.ngbModalRef = this.adresTypeModalRef(component, adresType);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.adresTypeModalRef(component, new AdresTypeKkf());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    adresTypeModalRef(component: Component, adresType: AdresTypeKkf): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.adresType = adresType;
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
