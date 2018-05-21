package Files;

import org.json.JSONArray;
import org.json.JSONObject;


import javax.json.JsonArray;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TreatementFiles {



    public static ArrayList<File> treatFilesGoogle(JSONObject files){

        ArrayList<File> listeFiles = new ArrayList<File>();


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
                type = "folder";
                //foldersId.add(id);
            }
            File newFile = new File(name,id, "GoogleDrive","",date, type);


            listeFiles.add(newFile);




        }

        return listeFiles;

    }

    public static List<File> treatFilesDropBox(JSONObject files) {
        List<File> listeFiles = new ArrayList<File>();

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

        return listeFiles;

    }

    public static JSONObject generateJSONFiles(ArrayList<File> listeFiles){

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
