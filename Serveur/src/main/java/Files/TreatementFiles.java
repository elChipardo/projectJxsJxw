package Files;

import org.json.JSONArray;
import org.json.JSONObject;


import javax.json.JsonArray;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreatementFiles {

    static List<File> listeFiles;

    public static void createliste(){

        listeFiles = new ArrayList<>();


    }

    public static void treatFilesGoogle(JSONObject files){


        JSONArray listfiles = files.getJSONArray("items");
        System.out.println("taille :"+listfiles.length());
        for (int i=0; i< listfiles.length();i++){
            String id = listfiles.getJSONObject(i).getString("id");

            String name = listfiles.getJSONObject(i).getString("title");
            // fonctionne pas : String sharePerson = listfiles.getJSONObject(i).getJSONObject("sharingUser").getString("displayName");
            String date = listfiles.getJSONObject(i).getString("modifiedDate");

            String type = "file";
            // detection des dossiers
            if (listfiles.getJSONObject(i).getString("mimeType").equals("application/vnd.google-apps.folder")){
                System.out.println("dossier " + name);
                System.out.println("id : " +id);
                type = "folder";
                //foldersId.add(id);
            }
            File newFile = new File(name,id, "GoogleDrive","",date, type);


            listeFiles.add(newFile);




        }

    }

    public static void treatFilesDropBox(JSONObject files) {

        JSONArray listfiles = files.getJSONArray("entries");


        for (int i = 0; i < listfiles.length(); i++) {
            String id = listfiles.getJSONObject(i).getString("id");

            String name = listfiles.getJSONObject(i).getString("name");
            // fonctionne pas : String sharePerson = listfiles.getJSONObject(i).getJSONObject("sharingUser").getString("displayName");
            //String date = listfiles.getJSONObject(i).getString("modifiedDate");

            String type = listfiles.getJSONObject(i).getString(".tag");
            File newFile = new File(name, id, "DropBox", "", "", type);


            listeFiles.add(newFile);


        }

    }

    public static JSONObject generateJSONFiles(){

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

    public static String afficherFichiers(){
        String res="";
        for (int i=0; i<listeFiles.size();i++){
            res = res + listeFiles.get(i).toString() + "\n";
        }
        return res;
    }


}
