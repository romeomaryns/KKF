/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { KkfTestModule } from '../../../test.module';
import { ContactTypeKkfComponent } from '../../../../../../main/webapp/app/entities/contact-type-kkf/contact-type-kkf.component';
import { ContactTypeKkfService } from '../../../../../../main/webapp/app/entities/contact-type-kkf/contact-type-kkf.service';
import { ContactTypeKkf } from '../../../../../../main/webapp/app/entities/contact-type-kkf/contact-type-kkf.model';

describe('Component Tests', () => {

    describe('ContactTypeKkf Management Component', () => {
        let comp: ContactTypeKkfComponent;
        let fixture: ComponentFixture<ContactTypeKkfComponent>;
        let service: ContactTypeKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [ContactTypeKkfComponent],
                providers: [
                    ContactTypeKkfService
                ]
            })
            .overrideTemplate(ContactTypeKkfComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContactTypeKkfComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContactTypeKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ContactTypeKkf(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.contactTypes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
