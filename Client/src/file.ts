import { Parent } from './parent';
export class File extends Parent{
  	type: string;


constructor(name:string, plateform:string, date:string){
  super(name, plateform,date);
 	this.type="dossier";
}
 // liste de fichier à terme
}
