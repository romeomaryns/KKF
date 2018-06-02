/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { KkfTestModule } from '../../../test.module';
import { ContactTypeKkfDetailComponent } from '../../../../../../main/webapp/app/entities/contact-type-kkf/contact-type-kkf-detail.component';
import { ContactTypeKkfService } from '../../../../../../main/webapp/app/entities/contact-type-kkf/contact-type-kkf.service';
import { ContactTypeKkf } from '../../../../../../main/webapp/app/entities/contact-type-kkf/contact-type-kkf.model';

describe('Component Tests', () => {

    describe('ContactTypeKkf Management Detail Component', () => {
        let comp: ContactTypeKkfDetailComponent;
        let fixture: ComponentFixture<ContactTypeKkfDetailComponent>;
        let service: ContactTypeKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [ContactTypeKkfDetailComponent],
                providers: [
                    ContactTypeKkfService
                ]
            })
            .overrideTemplate(ContactTypeKkfDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContactTypeKkfDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContactTypeKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ContactTypeKkf(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.contactType).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
