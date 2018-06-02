/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { KkfTestModule } from '../../../test.module';
import { AdresKkfComponent } from '../../../../../../main/webapp/app/entities/adres-kkf/adres-kkf.component';
import { AdresKkfService } from '../../../../../../main/webapp/app/entities/adres-kkf/adres-kkf.service';
import { AdresKkf } from '../../../../../../main/webapp/app/entities/adres-kkf/adres-kkf.model';

describe('Component Tests', () => {

    describe('AdresKkf Management Component', () => {
        let comp: AdresKkfComponent;
        let fixture: ComponentFixture<AdresKkfComponent>;
        let service: AdresKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [AdresKkfComponent],
                providers: [
                    AdresKkfService
                ]
            })
            .overrideTemplate(AdresKkfComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AdresKkfComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdresKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new AdresKkf(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.adres[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
