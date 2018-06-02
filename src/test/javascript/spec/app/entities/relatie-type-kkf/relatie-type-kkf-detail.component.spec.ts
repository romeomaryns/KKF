/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { KkfTestModule } from '../../../test.module';
import { RelatieTypeKkfDetailComponent } from '../../../../../../main/webapp/app/entities/relatie-type-kkf/relatie-type-kkf-detail.component';
import { RelatieTypeKkfService } from '../../../../../../main/webapp/app/entities/relatie-type-kkf/relatie-type-kkf.service';
import { RelatieTypeKkf } from '../../../../../../main/webapp/app/entities/relatie-type-kkf/relatie-type-kkf.model';

describe('Component Tests', () => {

    describe('RelatieTypeKkf Management Detail Component', () => {
        let comp: RelatieTypeKkfDetailComponent;
        let fixture: ComponentFixture<RelatieTypeKkfDetailComponent>;
        let service: RelatieTypeKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [RelatieTypeKkfDetailComponent],
                providers: [
                    RelatieTypeKkfService
                ]
            })
            .overrideTemplate(RelatieTypeKkfDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RelatieTypeKkfDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RelatieTypeKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new RelatieTypeKkf(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.relatieType).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
