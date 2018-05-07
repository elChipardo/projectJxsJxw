export class Folder {
	nom: string;
 	plateforme: string;
 	files: string[];
// liste de fichier à terme
 constructor(name: string, plat: string, fichiers:string[]){
 	this.nom=name;
 	this.plateforme=plat;
 	this.files=fichiers;
 }
}