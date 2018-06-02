/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { KkfTestModule } from '../../../test.module';
import { PersoonKkfComponent } from '../../../../../../main/webapp/app/entities/persoon-kkf/persoon-kkf.component';
import { PersoonKkfService } from '../../../../../../main/webapp/app/entities/persoon-kkf/persoon-kkf.service';
import { PersoonKkf } from '../../../../../../main/webapp/app/entities/persoon-kkf/persoon-kkf.model';

describe('Component Tests', () => {

    describe('PersoonKkf Management Component', () => {
        let comp: PersoonKkfComponent;
        let fixture: ComponentFixture<PersoonKkfComponent>;
        let service: PersoonKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [PersoonKkfComponent],
                providers: [
                    PersoonKkfService
                ]
            })
            .overrideTemplate(PersoonKkfComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PersoonKkfComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PersoonKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new PersoonKkf(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.persoons[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
