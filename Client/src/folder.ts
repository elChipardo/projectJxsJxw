import { Parent } from './parent';
export class Folder extends Parent{
	type: string;

// liste de fichier à terme
 constructor(name: string, plat: string, dateMod:string, id :string){
	super(id,name, plat,dateMod);
 	this.type="dossier";
 }
}
