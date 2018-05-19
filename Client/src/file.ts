import { Parent } from './parent';
export class File extends Parent{
  	type: string;


constructor(name:string, plateform:string, date:string, id:string){
  super(id,name, plateform,date);
  
 	this.type="fichier";
}
 // liste de fichier à terme
}
