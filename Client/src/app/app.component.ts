import { Component } from '@angular/core';
import {FolderService} from './folder.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  
	apiService: FolderService;
	
	constructor(api: FolderService){
		this.apiService = api;
	}
	
}
