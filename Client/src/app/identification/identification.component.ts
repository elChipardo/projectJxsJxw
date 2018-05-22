import { Component, OnInit } from '@angular/core';
import {FolderService} from '../folder.service';
import { Router } from '@angular/router';

@Component({
	selector: 'app-identification',
	templateUrl: './identification.component.html',
	styleUrls: ['./identification.component.css']
})
export class IdentificationComponent implements OnInit {

	apiService: FolderService;
	email: string = "";
	password: string = "";
	
	constructor(serviceFolder : FolderService){
		this.apiService = serviceFolder;
	}

	ngOnInit() {
	}

}
