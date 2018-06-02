/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { KkfTestModule } from '../../../test.module';
import { RelatieTypeKkfDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/relatie-type-kkf/relatie-type-kkf-delete-dialog.component';
import { RelatieTypeKkfService } from '../../../../../../main/webapp/app/entities/relatie-type-kkf/relatie-type-kkf.service';

describe('Component Tests', () => {

    describe('RelatieTypeKkf Management Delete Component', () => {
        let comp: RelatieTypeKkfDeleteDialogComponent;
        let fixture: ComponentFixture<RelatieTypeKkfDeleteDialogComponent>;
        let service: RelatieTypeKkfService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [RelatieTypeKkfDeleteDialogComponent],
                providers: [
                    RelatieTypeKkfService
                ]
            })
            .overrideTemplate(RelatieTypeKkfDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RelatieTypeKkfDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RelatieTypeKkfService);
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
