/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { KkfTestModule } from '../../../test.module';
import { GeslachtKkfDetailComponent } from '../../../../../../main/webapp/app/entities/geslacht-kkf/geslacht-kkf-detail.component';
import { GeslachtKkfService } from '../../../../../../main/webapp/app/entities/geslacht-kkf/geslacht-kkf.service';
import { GeslachtKkf } from '../../../../../../main/webapp/app/entities/geslacht-kkf/geslacht-kkf.model';

describe('Component Tests', () => {

    describe('GeslachtKkf Management Detail Component', () => {
        let comp: GeslachtKkfDetailComponent;
        let fixture: ComponentFixture<GeslachtKkfDetailComponent>;
        let service: GeslachtKkfService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KkfTestModule],
                declarations: [GeslachtKkfDetailComponent],
                providers: [
                    GeslachtKkfService
                ]
            })
            .overrideTemplate(GeslachtKkfDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GeslachtKkfDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GeslachtKkfService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new GeslachtKkf(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.geslacht).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
