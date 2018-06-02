/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { KkfTestModule } from '../../../test.module';
import { RelatieKkfDialogComponent } from '../../../../../../main/webapp/app/entities/relatie-kkf/relatie-kkf-dialog.component';
import { RelatieKkfService } from '../../../../../../main/webapp/app/entities/relatie-kkf/relatie-kkf.service';
import { RelatieKkf } from '../../../../../../main/webapp/app/entities/relatie-kkf/relatie-kkf.model';
import { RelatieTypeKkfService } from '../../../../../../main/webapp/app/entities/relatie-type-kkf';
import { PersoonKkfService } from '../../../../../../main/webapp/app/entities/persoon-kkf';

describe('Component Tests', () => {

    describe('RelatieKkf Management Dialog Component', () => {
        let comp: RelatieKkfDialogComponent;
        let fixture: ComponentFixture<RelatieKkfDialogComponent>;
        let service: RelatieKkfService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [RelatieKkfDialogComponent],
                providers: [
                    RelatieTypeKkfService,
                    PersoonKkfService,
                    RelatieKkfService
                ]
            })
            .overrideTemplate(RelatieKkfDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RelatieKkfDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RelatieKkfService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new RelatieKkf(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.relatie = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'relatieListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new RelatieKkf();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.relatie = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'relatieListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
