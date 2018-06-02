/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { KkfTestModule } from '../../../test.module';
import { AdresKkfDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/adres-kkf/adres-kkf-delete-dialog.component';
import { AdresKkfService } from '../../../../../../main/webapp/app/entities/adres-kkf/adres-kkf.service';

describe('Component Tests', () => {

    describe('AdresKkf Management Delete Component', () => {
        let comp: AdresKkfDeleteDialogComponent;
        let fixture: ComponentFixture<AdresKkfDeleteDialogComponent>;
        let service: AdresKkfService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [AdresKkfDeleteDialogComponent],
                providers: [
                    AdresKkfService
                ]
            })
            .overrideTemplate(AdresKkfDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AdresKkfDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdresKkfService);
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
