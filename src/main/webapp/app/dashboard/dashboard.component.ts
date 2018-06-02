import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [
    'dashboard.css'
  ]
})
export class DashboardComponent implements OnInit {

  message: string;

  constructor() {
    this.message = 'Dit is bericht uit constructor';
  }

  ngOnInit() {
  }

}
