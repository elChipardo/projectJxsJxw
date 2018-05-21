import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Folder } from '../folder';
import 'rxjs/add/operator/map';

@Injectable()
export class FolderService {

	//getting adresse
	private baseUrl = 'http://localhost:4200/';
	//private baseUrl:string= "../assets/files.json";
	constructor(private http: HttpClient) { }

	//------- GETTING DATA -------
	getAllJSON(): Observable<any> {
		//return this.http.get<any>(this.baseUrl + "ServeurDrive/Files").subscribe((res) => {
			return this.http.get<any>(this.baseUrl+ "ServeurDrive/Files");
	}

	getFolderJSON(name: string): Observable<any> {
		return this.http.get(this.baseUrl+"/"+name);
	}

	//get file ? url/nameFolder/nameFile

	//------- POST DATA -------
	//define url format
	//data = folder or file
	//create new data
	postData(url: string, data){
		let body = JSON.stringify(data);
		return this.http.post(url+"/post", body);
	}

	//------- PUT DATA -------
	//update name, move
	updateRenameData(plat: string, id: string, newname: string, data){
		//let body = JSON.stringify(data);
		console.log("http://localhost:8080/ServeurDrive/Rename"+plat+"?fileId="+id+"&title="+newname, data);
		return this.http.put("http://localhost:8080/ServeurDrive/Rename"+plat+"?fileId="+id+"&title="+newname, data);
	}

	updateMoveData(urlOld: string, newUrl: string, data){
		//let body = JSON.stringify(data);
		return this.http.put(urlOld+"/move/"+data.name+"?new=/"+ newUrl, data);
	}

	//------- DELETE DATA -------
	//remove data
	deleteData(plat: string, id: string): Observable<any> {
		console.log("http://localhost:8080/ServeurDrive/Delete"+plat+"?fileId="+id);
		return this.http.delete<any>('http://localhost:8080/ServeurDrive/Delete'+plat+'?fileId='+id);
	}
}


//move to component
	function mapFolder(response: HttpResponse<any>): Folder[] {
		return response.body.json().map(toFolder);
	}

	function mapOneFolder(response: Response): Folder {
		return toFolder(response.json());
	}

	function toFolder(r: any): Folder {
		return r;
	}
