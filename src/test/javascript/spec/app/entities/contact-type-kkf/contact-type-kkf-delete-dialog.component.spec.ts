/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { KkfTestModule } from '../../../test.module';
import { ContactTypeKkfDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/contact-type-kkf/contact-type-kkf-delete-dialog.component';
import { ContactTypeKkfService } from '../../../../../../main/webapp/app/entities/contact-type-kkf/contact-type-kkf.service';

describe('Component Tests', () => {

    describe('ContactTypeKkf Management Delete Component', () => {
        let comp: ContactTypeKkfDeleteDialogComponent;
        let fixture: ComponentFixture<ContactTypeKkfDeleteDialogComponent>;
        let service: ContactTypeKkfService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [ContactTypeKkfDeleteDialogComponent],
                providers: [
                    ContactTypeKkfService
                ]
            })
            .overrideTemplate(ContactTypeKkfDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContactTypeKkfDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContactTypeKkfService);
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
