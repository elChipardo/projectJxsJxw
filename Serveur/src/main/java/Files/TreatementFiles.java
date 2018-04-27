package Files;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.json.JsonArray;
import java.util.ArrayList;
import java.util.List;

public class TreatementFiles {

    static List<File> listeFiles = new ArrayList<File>();

    public static void treatFilesGoogle(JSONObject files){


        JSONArray listfiles = files.getJSONArray("items");
        System.out.println("yoooo"+listfiles.length());
        for (int i=0; i< listfiles.length();i++){
            String id = listfiles.getJSONObject(i).getString("id");

            String name = listfiles.getJSONObject(i).getString("title");
            String sharePerson = listfiles.getJSONObject(i).getJSONObject("sharingUser").getString("displayName");
            String date = listfiles.getJSONObject(i).getString("modifiedDate");

            File newFile = new File(name,id, "GoogleDrive",sharePerson,date);
            listeFiles.add(newFile);
            System.out.println("coucou");




        }
        System.out.println(afficherFichiers());



    }

    public static String afficherFichiers(){
        String res="";
        for (int i=0; i<listeFiles.size();i++){
            res = res + listeFiles.get(i).toString() + "\n";
        }
        return res;
    }
}
