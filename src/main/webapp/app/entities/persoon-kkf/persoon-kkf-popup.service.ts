import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { PersoonKkf } from './persoon-kkf.model';
import { PersoonKkfService } from './persoon-kkf.service';

@Injectable()
export class PersoonKkfPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private persoonService: PersoonKkfService

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
                this.persoonService.find(id)
                    .subscribe((persoonResponse: HttpResponse<PersoonKkf>) => {
                        const persoon: PersoonKkf = persoonResponse.body;
                        if (persoon.geboortedatum) {
                            persoon.geboortedatum = {
                                year: persoon.geboortedatum.getFullYear(),
                                month: persoon.geboortedatum.getMonth() + 1,
                                day: persoon.geboortedatum.getDate()
                            };
                        }
                        this.ngbModalRef = this.persoonModalRef(component, persoon);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.persoonModalRef(component, new PersoonKkf());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    persoonModalRef(component: Component, persoon: PersoonKkf): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.persoon = persoon;
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
