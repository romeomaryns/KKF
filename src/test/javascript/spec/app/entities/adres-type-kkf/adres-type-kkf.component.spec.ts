/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { KkfTestModule } from '../../../test.module';
import { AdresTypeKkfComponent } from '../../../../../../main/webapp/app/entities/adres-type-kkf/adres-type-kkf.component';
import { AdresTypeKkfService } from '../../../../../../main/webapp/app/entities/adres-type-kkf/adres-type-kkf.service';
import { AdresTypeKkf } from '../../../../../../main/webapp/app/entities/adres-type-kkf/adres-type-kkf.model';

describe('Component Tests', () => {

    describe('AdresTypeKkf Management Component', () => {
        let comp: AdresTypeKkfComponent;
        let fixture: ComponentFixture<AdresTypeKkfComponent>;
        let service: AdresTypeKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [AdresTypeKkfComponent],
                providers: [
                    AdresTypeKkfService
                ]
            })
            .overrideTemplate(AdresTypeKkfComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AdresTypeKkfComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdresTypeKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new AdresTypeKkf(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.adresTypes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
