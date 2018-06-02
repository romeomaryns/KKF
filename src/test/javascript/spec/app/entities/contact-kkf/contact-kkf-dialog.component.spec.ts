/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { KkfTestModule } from '../../../test.module';
import { ContactKkfDialogComponent } from '../../../../../../main/webapp/app/entities/contact-kkf/contact-kkf-dialog.component';
import { ContactKkfService } from '../../../../../../main/webapp/app/entities/contact-kkf/contact-kkf.service';
import { ContactKkf } from '../../../../../../main/webapp/app/entities/contact-kkf/contact-kkf.model';
import { ContactTypeKkfService } from '../../../../../../main/webapp/app/entities/contact-type-kkf';

describe('Component Tests', () => {

    describe('ContactKkf Management Dialog Component', () => {
        let comp: ContactKkfDialogComponent;
        let fixture: ComponentFixture<ContactKkfDialogComponent>;
        let service: ContactKkfService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [ContactKkfDialogComponent],
                providers: [
                    ContactTypeKkfService,
                    ContactKkfService
                ]
            })
            .overrideTemplate(ContactKkfDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContactKkfDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContactKkfService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ContactKkf(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.contact = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'contactListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ContactKkf();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.contact = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'contactListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
