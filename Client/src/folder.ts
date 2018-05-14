export class Folder {
	nom: string;
 	plateforme: string;
 	dateModif: string;
// liste de fichier à terme
 constructor(name: string, plat: string, dateMod:string){
 	this.nom=name;
 	this.plateforme=plat;
 	this.dateModif=dateMod;
 }
}