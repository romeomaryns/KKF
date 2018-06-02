/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { KkfTestModule } from '../../../test.module';
import { ContactKkfDetailComponent } from '../../../../../../main/webapp/app/entities/contact-kkf/contact-kkf-detail.component';
import { ContactKkfService } from '../../../../../../main/webapp/app/entities/contact-kkf/contact-kkf.service';
import { ContactKkf } from '../../../../../../main/webapp/app/entities/contact-kkf/contact-kkf.model';

describe('Component Tests', () => {

    describe('ContactKkf Management Detail Component', () => {
        let comp: ContactKkfDetailComponent;
        let fixture: ComponentFixture<ContactKkfDetailComponent>;
        let service: ContactKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [ContactKkfDetailComponent],
                providers: [
                    ContactKkfService
                ]
            })
            .overrideTemplate(ContactKkfDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContactKkfDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContactKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ContactKkf(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.contact).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
