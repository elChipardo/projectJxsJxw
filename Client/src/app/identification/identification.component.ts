import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
	selector: 'app-identification',
	templateUrl: './identification.component.html',
	styleUrls: ['./identification.component.css']
})
export class IdentificationComponent implements OnInit {

	constructor(private router: Router) { }

	ngOnInit() {
	}

	GoExplorer = function () {
		this.router.navigateByUrl('explorer');
	};

}
