/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { KkfTestModule } from '../../../test.module';
import { AdresKkfDetailComponent } from '../../../../../../main/webapp/app/entities/adres-kkf/adres-kkf-detail.component';
import { AdresKkfService } from '../../../../../../main/webapp/app/entities/adres-kkf/adres-kkf.service';
import { AdresKkf } from '../../../../../../main/webapp/app/entities/adres-kkf/adres-kkf.model';

describe('Component Tests', () => {

    describe('AdresKkf Management Detail Component', () => {
        let comp: AdresKkfDetailComponent;
        let fixture: ComponentFixture<AdresKkfDetailComponent>;
        let service: AdresKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [AdresKkfDetailComponent],
                providers: [
                    AdresKkfService
                ]
            })
            .overrideTemplate(AdresKkfDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AdresKkfDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdresKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new AdresKkf(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.adres).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
