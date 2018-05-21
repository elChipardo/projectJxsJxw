package Files;

import org.json.JSONArray;
import org.json.JSONObject;


import javax.json.JsonArray;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TreatementFiles {



    public static ArrayList<File> treatFilesGoogle(JSONObject files, boolean childrensOrNot, String accessToken) throws IOException {

        ArrayList<Files.File> listeFiles = new ArrayList<File>();
        ArrayList<Files.File> listeFolders = new ArrayList<File>();


        JSONArray listfiles = files.getJSONArray("items");
        System.out.println("taille :"+listfiles.length());
        for (int i=0; i< listfiles.length();i++){

            String id = listfiles.getJSONObject(i).getString("id");
            String name;
            String date;
            String type="file";
            if(childrensOrNot){

                    String url = "https://www.googleapis.com/drive/v2/files/"+id;

                //les propiétés
                HashMap<String, String> properties = new HashMap<>();
                properties.put("Host", "www.googleapis.com");
                properties.put("Authorization", "Bearer " + accessToken);

                // on execute la requête
                String response = HttpRequest.Request.setRequest(url, "GET", "", properties);

                JSONObject jsonFile = new JSONObject(response);

                name = jsonFile.getString("title");
                date = jsonFile.getString("modifiedDate");
                if (jsonFile.getString("mimeType").equals("application/vnd.google-apps.folder")) {
                    type = "folder";
                    File newFile = new File(name,id, "GoogleDrive","",date, type);
                    listeFolders.add(newFile);

                    System.out.println("dossier " + id);
                }
                else {

                    File newFile = new File(name,id, "GoogleDrive","",date, type);
                    listeFiles.add(newFile);

                }


            }
            else {
                 name = listfiles.getJSONObject(i).getString("title");
                // fonctionne pas : String sharePerson = listfiles.getJSONObject(i).getJSONObject("sharingUser").getString("displayName");
                 date = listfiles.getJSONObject(i).getString("modifiedDate");

                 type = "file";
                // detection des dossiers
                if (listfiles.getJSONObject(i).getString("mimeType").equals("application/vnd.google-apps.folder")) {
                    type = "folder";
                    System.out.println("dossier " + id);
                }

            }






        }

        listeFolders.addAll(listeFiles);
        return listeFolders;

    }

    public static ArrayList<File> treatFilesDropBox(JSONObject files) {
        ArrayList<File> listeFiles = new ArrayList<File>();
        System.out.println(files);

        JSONArray listfiles = files.getJSONArray("entries");


        for (int i = 0; i < listfiles.length(); i++) {
            String id = listfiles.getJSONObject(i).getString("id");

            String name = listfiles.getJSONObject(i).getString("name");
            // fonctionne pas : String sharePerson = listfiles.getJSONObject(i).getJSONObject("sharingUser").getString("displayName");



            String type = listfiles.getJSONObject(i).getString(".tag");
            String date="";
            if(type.equals("file")){
                date = listfiles.getJSONObject(i).getString("client_modified");
                System.out.println(date);

            }
            File newFile = new File(name, id, "DropBox", "", date, type);


            listeFiles.add(newFile);


        }

        return listeFiles;

    }

    public static JSONObject generateJSONFiles(ArrayList<File> listeFiles){

        afficherFichiers(listeFiles);

        JSONObject json = new JSONObject();

        JSONArray array = new JSONArray();

        for (int i=0; i<listeFiles.size();i++){
            JSONObject item = new JSONObject();
            item.put("title", listeFiles.get(i).nameFile);
            item.put("id", listeFiles.get(i).idFile);
            item.put("type", listeFiles.get(i).type);
            item.put("plateforme", listeFiles.get(i).namePlateforme);
            item.put("dateModif", listeFiles.get(i).dateModif);


            array.put(item);
        }

        json.put("items",array);

        return json;



    }

    public static String afficherFichiers(ArrayList<File> listeFiles){
        String res="";
        for (int i=0; i<listeFiles.size();i++){
            res = res + listeFiles.get(i).toString() + "\n";
        }
        return res;
    }


}
