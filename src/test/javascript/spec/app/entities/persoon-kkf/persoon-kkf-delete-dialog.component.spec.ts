/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { KkfTestModule } from '../../../test.module';
import { PersoonKkfDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/persoon-kkf/persoon-kkf-delete-dialog.component';
import { PersoonKkfService } from '../../../../../../main/webapp/app/entities/persoon-kkf/persoon-kkf.service';

describe('Component Tests', () => {

    describe('PersoonKkf Management Delete Component', () => {
        let comp: PersoonKkfDeleteDialogComponent;
        let fixture: ComponentFixture<PersoonKkfDeleteDialogComponent>;
        let service: PersoonKkfService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [PersoonKkfDeleteDialogComponent],
                providers: [
                    PersoonKkfService
                ]
            })
            .overrideTemplate(PersoonKkfDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PersoonKkfDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PersoonKkfService);
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
