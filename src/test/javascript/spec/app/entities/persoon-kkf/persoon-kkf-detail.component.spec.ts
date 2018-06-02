/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { KkfTestModule } from '../../../test.module';
import { PersoonKkfDetailComponent } from '../../../../../../main/webapp/app/entities/persoon-kkf/persoon-kkf-detail.component';
import { PersoonKkfService } from '../../../../../../main/webapp/app/entities/persoon-kkf/persoon-kkf.service';
import { PersoonKkf } from '../../../../../../main/webapp/app/entities/persoon-kkf/persoon-kkf.model';

describe('Component Tests', () => {

    describe('PersoonKkf Management Detail Component', () => {
        let comp: PersoonKkfDetailComponent;
        let fixture: ComponentFixture<PersoonKkfDetailComponent>;
        let service: PersoonKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [PersoonKkfDetailComponent],
                providers: [
                    PersoonKkfService
                ]
            })
            .overrideTemplate(PersoonKkfDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PersoonKkfDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PersoonKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new PersoonKkf(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.persoon).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
