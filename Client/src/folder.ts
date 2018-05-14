import { Parent } from './parent';
export class Folder extends Parent{
	type: string;

// liste de fichier à terme
 constructor(name: string, plat: string, dateMod:string){
	super(name, plat,dateMod);
 	this.type="dossier";
 }
}
