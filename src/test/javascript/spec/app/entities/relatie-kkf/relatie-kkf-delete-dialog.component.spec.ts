/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { KkfTestModule } from '../../../test.module';
import { RelatieKkfDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/relatie-kkf/relatie-kkf-delete-dialog.component';
import { RelatieKkfService } from '../../../../../../main/webapp/app/entities/relatie-kkf/relatie-kkf.service';

describe('Component Tests', () => {

    describe('RelatieKkf Management Delete Component', () => {
        let comp: RelatieKkfDeleteDialogComponent;
        let fixture: ComponentFixture<RelatieKkfDeleteDialogComponent>;
        let service: RelatieKkfService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [RelatieKkfDeleteDialogComponent],
                providers: [
                    RelatieKkfService
                ]
            })
            .overrideTemplate(RelatieKkfDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RelatieKkfDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RelatieKkfService);
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
