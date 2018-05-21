package launchServer;

import Files.TreatementFiles;
import HttpRequest.Request;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;


public class DropBox {

    private static String AppKey ="84imrzb8n7lobyz";
    private static String AppSecret = "t7kt83ir955vgju";




    @GET
    @Produces(MediaType.TEXT_HTML)
    public static Response getAuthentification() throws URISyntaxException, IOException{

        String url = "https://www.dropbox.com/oauth2/authorize?response_type=code&client_id="+AppKey+"&redirect_uri=http://localhost:8080/ServeurDrive/ResponseDropBox";

        java.net.URI location = new java.net.URI(url);
        return Response.temporaryRedirect(location).build();


    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    public static String getFiles(String accessT) throws IOException {

        String url = "https://api.dropboxapi.com/2/files/list_folder";

        // les propriétés
        HashMap<String,String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json");
        properties.put("Authorization", "Bearer " + accessT);

        // les paramètres
        String urlParameters ="{\"path\": \"\",\"recursive\": false,\"include_media_info\": false,\"include_deleted\": false,\"include_has_explicit_shared_members\": false,\"include_mounted_folders\": true}";

        // on execute la requete
        String response = HttpRequest.Request.setRequest(url,"POST",urlParameters, properties);

        System.out.println(response);

        try {
            TreatementFiles.treatFilesDropBox(new JSONObject(Request.requestFile));
        }
        catch ( Exception e){
            System.out.println(e);
        }

        return TreatementFiles.generateJSONFiles().toString();



    }


}
