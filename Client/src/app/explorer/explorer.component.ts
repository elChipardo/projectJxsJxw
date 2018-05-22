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
	listChildrenFiles: Parent[];
	listFolder: Parent[];
	free: string;
	freebis: string;
	idDossierParent: string;
	lienTelechargement: string;

	public blop;
	public blop2;
	public blop3;
	public blop4;
	public blop5;
	
	constructor(serviceFolder : FolderService){
		this.apiService = serviceFolder;
		this.free="";
	}

	//opérations à l'initialisation de la page
	ngOnInit(){

   		this.apiService.getAllJSON().subscribe(res => {
   			var rootFolder = new Array<Parent>();
   			this.blop=res.items;

   		//parcours du JSON reçu pour créer un tableu de fichiers et dossiers 
			for(let file in this.blop){
				if(this.blop[file].type =="folder"){
					rootFolder.push(new Folder(this.blop[file].title,this.blop[file].plateforme,this.blop[file].dateModif.slice(0, 10),this.blop[file].id));
				}else{
					rootFolder.push(new File(this.blop[file].title,this.blop[file].plateforme,this.blop[file].dateModif.slice(0, 10),this.blop[file].id));
				}
			}
			this.listFolder=rootFolder;
		});

   	//Récupération de l'espace libre sur les drives 
  		this.apiService.getFreeSpace("GoogleDrive").subscribe(res => {
			this.blop2=res;
			this.free="Sur GoogleDrive : "+this.blop2.espaceLibreGoogleDrive+"/"+this.blop2.espaceTotalGoogleDrive+"gB";
		});

		this.apiService.getFreeSpace("DropBox").subscribe(res => {
			this.blop3=res;
			this.freebis="Sur DropBox : "+this.blop3.espaceLibreDropBox+"/"+this.blop3.espaceTotalDropBox+"gB";
		});
	}


	//lors de la demande de supppression d'un fichier
	suppr(){
		console.log('on supprime')
		if (confirm("êtes vous sur de vouloir supprimer le fichier")) { 
       		var p= this.check();
       		console.log(p);
 			this.apiService.deleteData(p.plateforme,p.id).subscribe(res => {
			});
      		
      		//suppresion du fichier dans la liste des fichiers
      		if(this.listFolder.includes(p)){
      			this.listFolder.splice(this.listFolder.findIndex(x=> x==p),1);
      		}else{
      			this.listChildrenFiles.splice(this.listChildrenFiles.findIndex(x=> x==p),1);
      		}
			
    	}else{
        	alert("vous n'êtes pas d'accord")
        //ne rien faire 
   		 }	
	}

	//pour renommer un fichier
	renommer(){
	//récupération du nouveau nom de fichier 
    	var newName=prompt('Indiquez ici le nouveau nom de fichier');
    	if (newName !=null){
    //récupération du fichier selectionné
    		var p= this.check();
    //appel htttp pour rename
    		this.apiService.updateRenameData(p.plateforme, p.id, newName, true).subscribe(res => {
			});
			var t;
				if(p.type =="folder"){
					t= new Folder(newName,p.plateforme,p.dateModif,p.id);
				}else{
					t=(new File(newName,p.plateforme,p.dateModif,p.id));
				}
    	//Suppression de l'ancien fichier
    		if(this.listFolder.includes(p)){
				this.listFolder.splice(this.listFolder.findIndex(x=> x==p),1);
			//ajout du fichier en début de tableau
				this.listFolder.unshift(t);
			}else{
      			this.listChildrenFiles.splice(this.listChildrenFiles.findIndex(x=> x==p),1);
      			this.listChildrenFiles.unshift(t);
      		}
      	}
	}


	//vérifie le type du parent
	 isFile(m : string): boolean{
	 	
		if(m =="dossier"){
			return true;
		}else{
			return false;
		}
	}

	// recherche le parent qui a été coché 
	check():Parent {
		var inputs = document.getElementsByTagName('input');
		var inputsLength = inputs.length;
		for (var i = 0 ; i < inputsLength ; i++) {
			if (inputs[i].type == 'radio' && inputs[i].checked) {
				for (var b=0; b<this.listFolder.length; b++){
					
					if(inputs[i].id==this.listFolder[b].id){
						console.log(this.listFolder[b].nom)
						return this.listFolder[b];
					}
				}
				for(var a=0; a<this.listChildrenFiles.length;a++){
					if(inputs[i].id==this.listChildrenFiles[a].id){
						console.log(this.listChildrenFiles[a].nom)
						return this.listChildrenFiles[a];
					}
				}
			}
		}
	}
	

	//déplace un fichier dans un dossier existant
	move(){
	    var foldName=prompt('Indiquez ici le nom du dossier de destination');

	    if (foldName!=null){
    		var p= this.check();
    		var dossierDest = this.listFolder.find( par => par.nom == foldName);


    		if (dossierDest.plateforme=="GoogleDrive"){
    			this.apiService.updateMoveData("GoogleDrive",p.id,dossierDest.id,  true).subscribe(res => {
				});
    		}else{
    			this.apiService.updateMoveData(p.plateforme, p.id, foldName, true).subscribe(res => {
				});
			}
		}
	}


	askChildren(id:string){
		this.idDossierParent=id;
		var dossierDest = this.listFolder.find( par => par.id == this.idDossierParent);
	
 		if (dossierDest.plateforme=="GoogleDrive"){
 			console.log("coucou")
    	 	this.apiService.getFilesJSON(dossierDest.plateforme, dossierDest.id).subscribe(res => {
    	 		this.blop4=res.items;
    	 		console.log(this.blop4);
    	 		var rootFolder = new Array<Parent>();

				for(let file in this.blop4){
					if(this.blop[file].type =="folder"){
						rootFolder.push(new Folder(this.blop4[file].title,this.blop4[file].plateforme,this.blop4[file].dateModif.slice(0, 10),this.blop4[file].id));
					}else{
						rootFolder.push(new File(this.blop4[file].title,this.blop4[file].plateforme,this.blop4[file].dateModif.slice(0, 10),this.blop4[file].id));
					}
				}
				this.listChildrenFiles=rootFolder;
	
    			console.log("les enfants"+this.listChildrenFiles);
			});
    	}else{
    		this.apiService.getFilesJSON(dossierDest.plateforme, dossierDest.nom).subscribe(res => {
    			this.blop5=res.items;
    	 		var rootFolder = new Array<Parent>();

				for(let file in this.blop5){
					if(this.blop5[file].type =="folder"){
						rootFolder.push(new Folder(this.blop5[file].title,this.blop5[file].plateforme,this.blop5[file].dateModif.slice(0, 10),this.blop5[file].id));
					}else{
						rootFolder.push(new File(this.blop5[file].title,this.blop5[file].plateforme,this.blop5[file].dateModif.slice(0, 10),this.blop5[file].id));
					}
				}
				this.listChildrenFiles=rootFolder;
    			console.log("les enfants"+this.listChildrenFiles);
			});
		}
	}

	addFile(){
		var chemin=prompt('Indiquez ici le chemin absolu du fichier');
		var extens=prompt('Indiquez ici l extension de votre fichier');
		this.apiService.postData("GoogleDrive", chemin, extens, true).subscribe(res => {
		});

	}

	download(){
		var p= this.check();
		var extens=prompt('Indiquez ici le nom de lextension du fichier à télécharge');

		if(p.plateforme =="GoogleDrive"){
			this.apiService.downloadFileGoogleDrive(p.id, extens).subscribe(res =>{
			});
			this.lienTelechargement="http://localhost:8080/ServeurDrive/DownloadGoogleDrive?id="+p.id+"&extension="+extens;
			console.log("LIEN DE TELECHARGEMENT: "+this.lienTelechargement);
		}else {
			this.apiService.downloadFileDropBox(p.id, extens, true);
		}

	}




}
