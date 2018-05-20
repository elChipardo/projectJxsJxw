export class Parent{
  nom: string;
  plateforme: string;
  dateModif: string;
  type: string;
  id: string;
  constructor( ident: string,name: string, plat: string, dateMod:string){
   this.nom=name;
   this.plateforme=plat;
   this.dateModif=dateMod;
   this.type= "quelquechose";
   this.id=ident;
  }
}
