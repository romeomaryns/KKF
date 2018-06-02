/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { KkfTestModule } from '../../../test.module';
import { RelatieKkfComponent } from '../../../../../../main/webapp/app/entities/relatie-kkf/relatie-kkf.component';
import { RelatieKkfService } from '../../../../../../main/webapp/app/entities/relatie-kkf/relatie-kkf.service';
import { RelatieKkf } from '../../../../../../main/webapp/app/entities/relatie-kkf/relatie-kkf.model';

describe('Component Tests', () => {

    describe('RelatieKkf Management Component', () => {
        let comp: RelatieKkfComponent;
        let fixture: ComponentFixture<RelatieKkfComponent>;
        let service: RelatieKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [RelatieKkfComponent],
                providers: [
                    RelatieKkfService
                ]
            })
            .overrideTemplate(RelatieKkfComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RelatieKkfComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RelatieKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new RelatieKkf(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.relaties[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
