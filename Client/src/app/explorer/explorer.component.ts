import { Component, OnInit } from '@angular/core';
import {FolderService} from '../folder.service';
import { Folder } from 'folder';
import { Parent } from 'parent';
import { File } from 'file';


@Component({
  selector: 'app-explorer',
  templateUrl: './explorer.component.html',
  styleUrls: ['./explorer.component.css']
})
export class ExplorerComponent implements OnInit {
	apiService: FolderService;
	listParent: Parent[];
		listFolder: Parent[];
		free: string;
		freebis: string;

	public blop;
	public blop2;
	public blop3;
	constructor(serviceFolder : FolderService){
		this.apiService = serviceFolder;
		this.free="";
	}

	ngOnInit(){
   	this.apiService.getAllJSON().subscribe(res => {
   		var rootFolder = new Array<Parent>();
   		this.listParent=rootFolder
   		this.blop=res.items;

   	//	console.log(this.blop);

		for(let file in this.blop){
			if(this.blop[file].type =="folder"){
			rootFolder.push(new Folder(this.blop[file].title,this.blop[file].plateforme,this.blop[file].dateModif.slice(0, 10),this.blop[file].id));
			}else{
			rootFolder.push(new File(this.blop[file].title,this.blop[file].plateforme,this.blop[file].dateModif.slice(0, 10),this.blop[file].id));
			}
		}
		this.listFolder=rootFolder;
    //console.log(this.listFolder);

	});
    //console.log('bonjour');
   /* res.subscribe ((folderList : Folder[]) => {

      console.log(folderList);
   });*/

  this.apiService.getFreeSpace("GoogleDrive").subscribe(res => {
		this.blop2=res;
		this.free="Sur GoogleDrive : "+this.blop2.espaceLibreGoogleDrive+"/"+this.blop2.espaceTotalGoogleDrive+"gB";
	});

this.apiService.getFreeSpace("DropBox").subscribe(res => {
		this.blop3=res;
		this.freebis="Sur DropBox : "+this.blop3.espaceLibreDropBox+"/"+this.blop3.espaceTotalDropBox+"gB";
	console.log(this.free+ " "+this.freebis);
	});
	}


	suppr(){
		console.log('on supprime')
		if (confirm("êtes vous sur de vouloir supprimer le fichier")) { 
        alert("vous êtes d'accord")
        var p= this.check();
 
        

           	this.apiService.deleteData(p.plateforme,p.id).subscribe(res => {
	});
      
	this.listFolder.splice(this.listFolder.findIndex(x=> x==p),1);
        //supprimer le fichier selectionné
    }else{
        alert("vous n'êtes pas d'accord")
        //ne rien faire 
    }	
	}


	 renommer(){
	 console.log('on renomme')
    var newName=prompt('Indiquez ici le nouveau nom de fichier');
    var p= this.check();
    this.apiService.updateRenameData(p.plateforme, p.id, newName, true).subscribe(res => {
	});

	this.listFolder.splice(this.listFolder.findIndex(x=> x==p),1);
	var t;
	if(p.type =="folder"){
			t= new Folder(newName,p.plateforme,p.dateModif,p.id);
			}else{
			t=(new File(newName,p.plateforme,p.dateModif,p.id));
			}
	this.listFolder.unshift(t);



	
	//this.listFolder.push(p);
    //faire un appel au changement de no
}

	 isFile(m : Parent): boolean{
		if(m.type =='fichier'){
			return true;
		}else{
			return false;
		}
	}

	check():Parent {
		var inputs = document.getElementsByTagName('input');
		var inputsLength = inputs.length;
		for (var i = 0 ; i < inputsLength ; i++) {
			if (inputs[i].type == 'radio' && inputs[i].checked) {
				//alert('La case cochée est la n°'+ inputs[i].id);
				

				//console.log(this.listFolder[0].id==inputs[i].id);
				for (var b=0; b<this.listFolder.length; b++){
					
					//console.log('1enIVztIQDwlVQ7OVnLX5x2nTwW59opJBHAS-7IDjucc'=='1enIVztIQDwlVQ7OVnLX5x2nTwW59opJBHAS-7IDjucc');
					if(inputs[i].id==this.listFolder[b].id){
					//	alert(this.listFolder[b].id+ "trouvé");
						return this.listFolder[b];
					}
				}
			}
		}
	}
	
actualiser(){
this.apiService.getAllJSON().subscribe(res => {
   		var rootFolder = new Array<Parent>();
   		this.listParent=rootFolder
   		this.blop=res.items;

   		console.log(this.blop);

		for(let file in this.blop){
			if(this.blop[file].type =="dossier"){
			rootFolder.push(new Folder(this.blop[file].title,this.blop[file].plateforme,this.blop[file].dateModif,this.blop[file].id));
			}else{
			rootFolder.push(new File(this.blop[file].title,this.blop[file].plateforme,this.blop[file].dateModif,this.blop[file].id));
			}
		}
		this.listFolder=rootFolder;
    console.log(this.listFolder);

	});
}

move(){
	    var foldName=prompt('Indiquez ici le nom du dossier de destination');
    var p= this.check();
    var dossierDest = this.listFolder.find( par => par.nom == foldName);
    console.log(dossierDest);
    console.log("bonjour");
     console.log(p.id);

    if (dossierDest.plateforme=="GoogleDrive"){
    	 this.apiService.updateMoveData("GoogleDrive",p.id,dossierDest.id,  true).subscribe(res => {
	});
 console.log("au revoir");
 console.log(dossierDest.id);
    }else{
    this.apiService.updateMoveData(p.plateforme, p.id, foldName, true).subscribe(res => {
	});

}

}

}
