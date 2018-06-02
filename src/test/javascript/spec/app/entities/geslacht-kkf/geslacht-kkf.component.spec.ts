/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { KkfTestModule } from '../../../test.module';
import { GeslachtKkfComponent } from '../../../../../../main/webapp/app/entities/geslacht-kkf/geslacht-kkf.component';
import { GeslachtKkfService } from '../../../../../../main/webapp/app/entities/geslacht-kkf/geslacht-kkf.service';
import { GeslachtKkf } from '../../../../../../main/webapp/app/entities/geslacht-kkf/geslacht-kkf.model';

describe('Component Tests', () => {

    describe('GeslachtKkf Management Component', () => {
        let comp: GeslachtKkfComponent;
        let fixture: ComponentFixture<GeslachtKkfComponent>;
        let service: GeslachtKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [GeslachtKkfComponent],
                providers: [
                    GeslachtKkfService
                ]
            })
            .overrideTemplate(GeslachtKkfComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GeslachtKkfComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GeslachtKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new GeslachtKkf(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.geslachts[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
