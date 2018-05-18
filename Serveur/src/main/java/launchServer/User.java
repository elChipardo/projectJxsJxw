package launchServer;

import Files.TreatementFiles;
import HttpRequest.Request;
import org.json.JSONObject;

import java.io.File;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

@Path("")
public class User {

    private static String access_tokenGoogle;
    private static String access_tokenDrop;
    private static String codeGoogle;
    private static String codeDropBox;
    private static String AppKey ="84imrzb8n7lobyz";
    private static String AppSecret = "t7kt83ir955vgju";
    private static String clientID = "1052251515610-a6n80d93kvrffileji09br2ksbnvdoj3.apps.googleusercontent.com";


    String clientSecret = "mPeJJscfiXdNGN9jma3croXB";

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String afficher(){
        return "<a href=http://localhost:8080/ServeurDrive/OauthGoogleDrive"+">"+"click ici pour s'authentifier à google"+"</a>"+"<br>"+
                "<a href=http://localhost:8080/ServeurDrive/OauthDropBox"+">"+"click ici pour s'authentifier à dropbox"+"</a>" + "<br>" +
                "<a href=http://localhost:8080/ServeurDrive/Files" + ">" + "recupérer les fichiers en JSON"  +"</a>" + "<br>" +
                "<a href=http://localhost:8080/ServeurDrive/RenameGoogleDrive?fileId=kdsg&title=labiteADudule"+ ">" + "renommer fichier"  +"</a>" + "<br>";

    }


    @Path("/OauthGoogleDrive")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response authentificationGoogleDrive() throws IOException, URISyntaxException {
        return GoogleDrive.getAuthentification();


    }

    @Path("/ResponseGoogleDrive")
    @GET //A changer en POST
    public Response getResponseGoogle(@QueryParam("code") String codeURL) throws URISyntaxException, IOException {

        codeGoogle = codeURL;

        String url = "https://www.googleapis.com/oauth2/v4/token";

        // les propriétés
        HashMap<String,String> properties = new HashMap<>();
        properties.put("Host", "www.googleapis.com");
        properties.put("Content-Type","application/x-www-form-urlencoded");

        // les paramètres
        String urlParameters = "code="+codeGoogle + "&client_id="+clientID+ "&client_secret="+clientSecret + "&redirect_uri=http://localhost:8080/ServeurDrive/ResponseGoogleDrive" + "&grant_type=authorization_code";

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url,"POST",urlParameters, properties);
        System.out.println(response.toString());

        JSONObject myResponse = new JSONObject(response.toString());
        this.access_tokenGoogle = myResponse.getString("access_token");



        // redirection vers le path Files pour executer la requete GET et ainsi recuperer la liste des fichiers
        java.net.URI location = new java.net.URI("http://localhost:4200/explorer");
        //java.net.URI location = new java.net.URI("http://localhost:8080/ServeurDrive/");
        return Response.temporaryRedirect(location).build();

    }

    @Path("/OauthDropBox")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response authentificationDropBox() throws IOException, URISyntaxException {

            return DropBox.getAuthentification();


    }

    @Path("/ResponseDropBox")
    @GET
    public Response getResponseDrop(@QueryParam("code") String codeURL) throws URISyntaxException, IOException {

        // le code récupéré dans l'url
        codeDropBox = codeURL;

        String url = "https://api.dropboxapi.com/oauth2/token";

        // les propriétés
        HashMap<String,String> properties = new HashMap<>();
        properties.put("Content-Type","application/x-www-form-urlencoded");

        // les paramètres
        String urlParameters = "code="+codeDropBox + "&client_id="+AppKey+ "&client_secret="+AppSecret + "&redirect_uri=http://localhost:8080/ServeurDrive/ResponseDropBox" + "&grant_type=authorization_code";

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url,"POST",urlParameters, properties);
        //print result
        System.out.println(response);

        JSONObject myResponse = new JSONObject(response.toString());
        // on recupere et on stocke l'access token
        this.access_tokenDrop = myResponse.getString("access_token");

        // redirection vers le path Files pour executer la requete GET et ainsi recuperer la liste des fichiers
        java.net.URI location = new java.net.URI("http://localhost:4200/explorer");
        return Response.temporaryRedirect(location).build();

    }

    @Path("/Files")
    @Produces(MediaType.TEXT_HTML)
    @GET
    public String getAllFiles(){

        try {
            GoogleDrive.getFiles(this.access_tokenGoogle);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            DropBox.getFiles(this.access_tokenDrop);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return TreatementFiles.generateJSONFiles().toString();

    }

    @Path("/DeleteGoogleDrive")
    @GET //A changer en DELETE
    @Produces(MediaType.TEXT_HTML)
    public String deleteFileGoogle(@QueryParam("fileId") String fileIdParam) throws IOException {
    	
        String fileID = fileIdParam;
        String url = "https://www.googleapis.com/drive/v2/files/" + fileID;

        //les propiétés
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Host", "www.googleapis.com");
        properties.put("Authorization", "Bearer " + this.access_tokenGoogle);

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "DELETE", "", properties);

        return "<p>" + response + "</p>";
    }

    @Path("/RenameGoogleDrive")
    @GET //A changer en PUT
    @Produces(MediaType.TEXT_HTML)
    public String renameFile (@QueryParam("fileId") String fileIdParam, @QueryParam("title") String titleParam) throws IOException {
        
        //String fileId = "1KJbMVyGmFykfxllgDqqzg5sV0XWyEBOsKtCwRQhn4RI";
        String fileId = fileIdParam;
        String url = "https://www.googleapis.com/drive/v2/files/" + fileId;
        String titler = titleParam;
        //les propiétés
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Host", "www.googleapis.com");
        properties.put("Authorization", "Bearer " + this.access_tokenGoogle);
        properties.put("uploadType", "multipart");
        properties.put("Content-Type", "application/json");

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "PUT", "{ \"title\" : \"" + titleParam + "\" }", properties);

        return "<p>" + response + "</p>";
        
}

    @Path("/DeleteDropBox")
    @GET //A changer en DELETE
    @Produces(MediaType.TEXT_HTML)
    public String deleteFileDropBox(@QueryParam("fileId") String fileIdParam) throws IOException {


        String fileID = fileIdParam;



        // requete annexe qui recupere le chemin du fichier
        String urlRequeteannexe = "https://api.dropboxapi.com/2/files/get_metadata";

        HashMap<String, String> propertiesRequeteAnnexe = new HashMap<>();
        propertiesRequeteAnnexe.put("Content-Type", "application/json");
        propertiesRequeteAnnexe.put("Authorization", "Bearer " + this.access_tokenDrop);

        String urlParametersrequeteAnnexe ="{\"path\": \""+fileID+"\",\"include_media_info\": false,\"include_deleted\": false,\"include_has_explicit_shared_members\": false}";

        String responseRequeteannexe = HttpRequest.Request.setRequest(urlRequeteannexe, "POST", urlParametersrequeteAnnexe, propertiesRequeteAnnexe);

        JSONObject jsonRequeteAnnexe = new JSONObject(Request.requestFile);

        String path = jsonRequeteAnnexe.getString("path_lower");

        // fin requete annexe
        System.out.println(responseRequeteannexe);



        String url = "https://api.dropboxapi.com/2/files/delete_v2";

        //les propiétés
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json");
        properties.put("Authorization", "Bearer " + this.access_tokenDrop);


        String urlParameters ="{\"path\": \""+path+"\"}";


        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "POST", urlParameters, properties);
        System.out.println(response);

        return "<p>" + responseRequeteannexe     + "</p>";

    }

        
    @Path("/RenameDropBox")
    @GET //A changer en PUT
    @Produces(MediaType.TEXT_HTML)
    public String renameFileDropBox (@QueryParam("fileId") String fileIdParam, @QueryParam("title") String newTitleParam) throws IOException {

        String fileId = fileIdParam;
        String url = "https://api.dropboxapi.com/2/files/move_v2";
        String newTitle = newTitleParam;

        // requete annexe qui recupere le chemin du fichier
        String urlRequeteannexe = "https://api.dropboxapi.com/2/files/get_metadata";

        HashMap<String, String> propertiesRequeteAnnexe = new HashMap<>();
        propertiesRequeteAnnexe.put("Content-Type", "application/json");
        propertiesRequeteAnnexe.put("Authorization", "Bearer " + this.access_tokenDrop);

        String urlParametersrequeteAnnexe ="{\"path\": \""+fileId+"\",\"include_media_info\": false,\"include_deleted\": false,\"include_has_explicit_shared_members\": false}";

        String responseRequeteannexe = HttpRequest.Request.setRequest(urlRequeteannexe, "POST", urlParametersrequeteAnnexe, propertiesRequeteAnnexe);

        JSONObject jsonRequeteAnnexe = new JSONObject(Request.requestFile);

        String path = jsonRequeteAnnexe.getString("path_lower");

        // fin requete annexe

        HashMap<String, String> properties = new HashMap<>();
        properties.put("Authorization", "Bearer " + this.access_tokenDrop);
        properties.put("Content-Type", "application/json");

        String urlParameters = "{\"from_path\": \""+path+"\",\"to_path\": \"/"+newTitle+"\",\"allow_shared_folder\": false,\"autorename\": false,\"allow_ownership_transfer\": false}";

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "POST", urlParameters,properties);

        return "<p>" + response + "</p>";

    }



}
