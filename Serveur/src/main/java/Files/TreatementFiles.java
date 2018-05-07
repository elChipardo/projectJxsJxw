package Files;

import jdk.internal.cmm.SystemResourcePressureImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.json.JsonArray;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreatementFiles {

    static List<File> listeFiles = new ArrayList<File>();
    static List<String> foldersId = new ArrayList<>();

    public static void treatFilesGoogle(JSONObject files){


        System.out.println(files);
        JSONArray listfiles = files.getJSONArray("items");
        System.out.println("taille :"+listfiles.length());
        for (int i=0; i< listfiles.length();i++){
            String id = listfiles.getJSONObject(i).getString("id");

            String name = listfiles.getJSONObject(i).getString("title");
            // fonctionne pas : String sharePerson = listfiles.getJSONObject(i).getJSONObject("sharingUser").getString("displayName");
            String date = listfiles.getJSONObject(i).getString("modifiedDate");

            String type = "fichier";
            // detection des dossiers
            if (listfiles.getJSONObject(i).getString("mimeType").equals("application/vnd.google-apps.folder")){
                System.out.println("dossier " + name);
                type = "dossier";
                //foldersId.add(id);
            }
            File newFile = new File(name,id, "GoogleDrive","",date, type);


            listeFiles.add(newFile);




        }
        System.out.println(afficherFichiers());



    }

    public static JSONObject generateJSONFiles(){

        JSONObject json = new JSONObject();

        JSONArray array = new JSONArray();

        for (int i=0; i<listeFiles.size();i++){
            JSONObject item = new JSONObject();
            item.put("title", listeFiles.get(i).nameFile);
            item.put("type", listeFiles.get(i).type);
            item.put("plateforme", listeFiles.get(i).namePlateforme);
            item.put("dateModif", listeFiles.get(i).dateModif);

            array.put(item);
        }

        json.put("items",array);
        System.out.println(json.toString());

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
