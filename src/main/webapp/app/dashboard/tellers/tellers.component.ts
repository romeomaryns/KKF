import {PersoonKkfService} from '../../entities/persoon-kkf/persoon-kkf.service';
import {PersoonKkf} from '../../entities/persoon-kkf/persoon-kkf.model';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-tellers',
  templateUrl: './tellers.component.html',
  styles: []
})
export class TellersComponent implements OnInit {

  personen: PersoonKkf[];

  constructor(
    private persoonService: PersoonKkfService
  ) {
    this.personen = [];
  }

  ngOnInit() {
  }

}
