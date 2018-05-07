import { Component, OnInit } from '@angular/core';
import {FolderService} from '../folder.service';
import { Folder } from 'folder';

@Component({
  selector: 'app-explorer',
  templateUrl: './explorer.component.html',
  styleUrls: ['./explorer.component.css']
})
export class ExplorerComponent implements OnInit {  
	apiService: FolderService;
	listFolder: Folder[];
	public blop;
	constructor(serviceFolder : FolderService){
		this.apiService = serviceFolder;
	}
	
	ngOnInit(){
   	this.apiService.getAllJSON().subscribe(res => {
   		var rootFolder = new Array<Folder>();
   		this.blop=res.items;

   		console.log(this.blop);

		for(let file in this.blop){
			rootFolder.push(new Folder(this.blop[file].title,this.blop[file].plateforme,[]));
		
		}
		this.listFolder=rootFolder;

	});
    console.log('bonjour');
   /* res.subscribe ((folderList : Folder[]) => {

      console.log(folderList);
   });*/
	}
}