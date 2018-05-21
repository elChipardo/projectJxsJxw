package Files;

import org.json.JSONArray;
import org.json.JSONObject;


import javax.json.JsonArray;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TreatementFiles {



    public static ArrayList<File> treatFilesGoogle(JSONObject files, String accessToken) throws IOException {

        ArrayList<Files.File> listeFiles = new ArrayList<File>();
        ArrayList<Files.File> listeFolders = new ArrayList<File>();


        System.out.println("coucou"+files);
        System.out.println(files.getJSONArray("items"));

        JSONArray listfiles = files.getJSONArray("items");
        for (int i=0; i< listfiles.length();i++){


            String id = listfiles.getJSONObject(i).getString("id");
            String name;
            String date;
            String type="file";

                 name = listfiles.getJSONObject(i).getString("title");
                    System.out.println(name);
                 date = listfiles.getJSONObject(i).getString("modifiedDate");

                 type = "file";
                // detection des dossiers
                if (listfiles.getJSONObject(i).getString("mimeType").equals("application/vnd.google-apps.folder")) {
                    type = "folder";
                    File newFile = new File(name,id, "GoogleDrive","",date, type);
                    listeFolders.add(newFile);
                    System.out.println("dossier " + id);
                } else {
                    File newFile = new File(name, id, "GoogleDrive", "", date, type);
                    listeFiles.add(newFile);
                }

            }

        listeFolders.addAll(listeFiles);
        System.out.println(afficherFichiers(listeFolders));
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
