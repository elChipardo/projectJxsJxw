package launchServer;


import Files.TreatementFiles;
import HttpRequest.Request;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;



public class GoogleDrive  {


    private static String clientID = "1052251515610-a6n80d93kvrffileji09br2ksbnvdoj3.apps.googleusercontent.com";



    @GET
    @Produces(MediaType.TEXT_HTML)
    public static Response getAuthentification() throws URISyntaxException, IOException{
        String url = "https://accounts.google.com/o/oauth2/v2/auth?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fdrive&redirect_uri=http://localhost:8080/ServeurDrive/ResponseGoogleDrive&response_type=code&client_id="+clientID;

        java.net.URI location = new java.net.URI(url);
        return Response.temporaryRedirect(location).build();
    }



    @GET
    @Produces(MediaType.TEXT_HTML)
    public static void getFiles(String accessT) throws IOException {


    String url = "https://www.googleapis.com/drive/v2/files/";


    //les propiétés
    HashMap<String,String> properties = new HashMap<>();
    properties.put("Host", "www.googleapis.com");
    properties.put("Authorization", "Bearer " + accessT);
    properties.put("orderBy", "folder");
    properties.put("maxResults","1000");

        String urlParameters="";


        // maximum fichiers String urlParameters = "maxResults=1000";

    // on execute la requête
    String response = HttpRequest.Request.setRequest(url,"GET",urlParameters, properties);



}

}
