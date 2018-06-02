/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { KkfTestModule } from '../../../test.module';
import { AdresTypeKkfDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/adres-type-kkf/adres-type-kkf-delete-dialog.component';
import { AdresTypeKkfService } from '../../../../../../main/webapp/app/entities/adres-type-kkf/adres-type-kkf.service';

describe('Component Tests', () => {

    describe('AdresTypeKkf Management Delete Component', () => {
        let comp: AdresTypeKkfDeleteDialogComponent;
        let fixture: ComponentFixture<AdresTypeKkfDeleteDialogComponent>;
        let service: AdresTypeKkfService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [AdresTypeKkfDeleteDialogComponent],
                providers: [
                    AdresTypeKkfService
                ]
            })
            .overrideTemplate(AdresTypeKkfDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AdresTypeKkfDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdresTypeKkfService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
