/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { KkfTestModule } from '../../../test.module';
import { AdresTypeKkfDetailComponent } from '../../../../../../main/webapp/app/entities/adres-type-kkf/adres-type-kkf-detail.component';
import { AdresTypeKkfService } from '../../../../../../main/webapp/app/entities/adres-type-kkf/adres-type-kkf.service';
import { AdresTypeKkf } from '../../../../../../main/webapp/app/entities/adres-type-kkf/adres-type-kkf.model';

describe('Component Tests', () => {

    describe('AdresTypeKkf Management Detail Component', () => {
        let comp: AdresTypeKkfDetailComponent;
        let fixture: ComponentFixture<AdresTypeKkfDetailComponent>;
        let service: AdresTypeKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [AdresTypeKkfDetailComponent],
                providers: [
                    AdresTypeKkfService
                ]
            })
            .overrideTemplate(AdresTypeKkfDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AdresTypeKkfDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdresTypeKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new AdresTypeKkf(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.adresType).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
