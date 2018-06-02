/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { KkfTestModule } from '../../../test.module';
import { RelatieTypeKkfDialogComponent } from '../../../../../../main/webapp/app/entities/relatie-type-kkf/relatie-type-kkf-dialog.component';
import { RelatieTypeKkfService } from '../../../../../../main/webapp/app/entities/relatie-type-kkf/relatie-type-kkf.service';
import { RelatieTypeKkf } from '../../../../../../main/webapp/app/entities/relatie-type-kkf/relatie-type-kkf.model';

describe('Component Tests', () => {

    describe('RelatieTypeKkf Management Dialog Component', () => {
        let comp: RelatieTypeKkfDialogComponent;
        let fixture: ComponentFixture<RelatieTypeKkfDialogComponent>;
        let service: RelatieTypeKkfService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [RelatieTypeKkfDialogComponent],
                providers: [
                    RelatieTypeKkfService
                ]
            })
            .overrideTemplate(RelatieTypeKkfDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RelatieTypeKkfDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RelatieTypeKkfService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new RelatieTypeKkf(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.relatieType = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'relatieTypeListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new RelatieTypeKkf();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.relatieType = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'relatieTypeListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
