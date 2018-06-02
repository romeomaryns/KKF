/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { KkfTestModule } from '../../../test.module';
import { PersoonKkfDialogComponent } from '../../../../../../main/webapp/app/entities/persoon-kkf/persoon-kkf-dialog.component';
import { PersoonKkfService } from '../../../../../../main/webapp/app/entities/persoon-kkf/persoon-kkf.service';
import { PersoonKkf } from '../../../../../../main/webapp/app/entities/persoon-kkf/persoon-kkf.model';
import { ContactKkfService } from '../../../../../../main/webapp/app/entities/contact-kkf';
import { GeslachtKkfService } from '../../../../../../main/webapp/app/entities/geslacht-kkf';
import { AdresKkfService } from '../../../../../../main/webapp/app/entities/adres-kkf';
import { RelatieKkfService } from '../../../../../../main/webapp/app/entities/relatie-kkf';

describe('Component Tests', () => {

    describe('PersoonKkf Management Dialog Component', () => {
        let comp: PersoonKkfDialogComponent;
        let fixture: ComponentFixture<PersoonKkfDialogComponent>;
        let service: PersoonKkfService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [PersoonKkfDialogComponent],
                providers: [
                    ContactKkfService,
                    GeslachtKkfService,
                    AdresKkfService,
                    RelatieKkfService,
                    PersoonKkfService
                ]
            })
            .overrideTemplate(PersoonKkfDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PersoonKkfDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PersoonKkfService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new PersoonKkf(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.persoon = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'persoonListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new PersoonKkf();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.persoon = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'persoonListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
