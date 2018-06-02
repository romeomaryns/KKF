/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { KkfTestModule } from '../../../test.module';
import { ContactKkfComponent } from '../../../../../../main/webapp/app/entities/contact-kkf/contact-kkf.component';
import { ContactKkfService } from '../../../../../../main/webapp/app/entities/contact-kkf/contact-kkf.service';
import { ContactKkf } from '../../../../../../main/webapp/app/entities/contact-kkf/contact-kkf.model';

describe('Component Tests', () => {

    describe('ContactKkf Management Component', () => {
        let comp: ContactKkfComponent;
        let fixture: ComponentFixture<ContactKkfComponent>;
        let service: ContactKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [ContactKkfComponent],
                providers: [
                    ContactKkfService
                ]
            })
            .overrideTemplate(ContactKkfComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContactKkfComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContactKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ContactKkf(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.contacts[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
