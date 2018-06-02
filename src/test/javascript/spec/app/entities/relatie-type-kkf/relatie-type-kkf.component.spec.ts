/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { KkfTestModule } from '../../../test.module';
import { RelatieTypeKkfComponent } from '../../../../../../main/webapp/app/entities/relatie-type-kkf/relatie-type-kkf.component';
import { RelatieTypeKkfService } from '../../../../../../main/webapp/app/entities/relatie-type-kkf/relatie-type-kkf.service';
import { RelatieTypeKkf } from '../../../../../../main/webapp/app/entities/relatie-type-kkf/relatie-type-kkf.model';

describe('Component Tests', () => {

    describe('RelatieTypeKkf Management Component', () => {
        let comp: RelatieTypeKkfComponent;
        let fixture: ComponentFixture<RelatieTypeKkfComponent>;
        let service: RelatieTypeKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [RelatieTypeKkfComponent],
                providers: [
                    RelatieTypeKkfService
                ]
            })
            .overrideTemplate(RelatieTypeKkfComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RelatieTypeKkfComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RelatieTypeKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new RelatieTypeKkf(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.relatieTypes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
