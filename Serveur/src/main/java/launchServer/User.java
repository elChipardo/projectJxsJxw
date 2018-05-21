package launchServer;

import Files.TreatementFiles;
import HttpRequest.Request;
import org.json.JSONObject;

import java.io.File;
import javax.management.Query;
import javax.swing.plaf.synth.SynthTextAreaUI;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.io.FileInputStream;

@Path("")
public class User {

    private static String access_tokenGoogle="";
    private static String access_tokenDrop="";
    private static String codeGoogle;
    private static String codeDropBox;
    private static String AppKey = "84imrzb8n7lobyz";
    private static String AppSecret = "t7kt83ir955vgju";
    private static String clientID = "1052251515610-a6n80d93kvrffileji09br2ksbnvdoj3.apps.googleusercontent.com";


    String clientSecret = "mPeJJscfiXdNGN9jma3croXB";

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String afficher(){
        return "<a href=http://localhost:8080/ServeurDrive/OauthGoogleDrive"+">"+"click ici pour s'authentifier à google"+"</a>"+"<br>"+
                "<a href=http://localhost:8080/ServeurDrive/OauthDropBox"+">"+"click ici pour s'authentifier à dropbox"+"</a>" + "<br>" +
                "<a href=http://localhost:8080/ServeurDrive/Files" + ">" + "recupérer les fichiers en JSON"  +"</a>" + "<br>" +
                "<a href=http://localhost:8080/ServeurDrive/DeleteGoogleDrive?fileId=1F7fCDzdje9D0sXo-E2vH1h6Af_KQdt1S1oVubUl0oro"+ ">" + "supprimer fichier"  +"</a>" + "<br>"+
                "<a href=http://localhost:8080/ServeurDrive/UploadGoogleDrive?path=/home/hcnn/Documents/ESIR/ESIR2/S8/JXS/tp/project/project.pdf&extension=pdf" + ">" + "up fichier" + "</a>" + "<br>";

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
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Host", "www.googleapis.com");
        properties.put("Content-Type", "application/x-www-form-urlencoded");

        // les paramètres
        String urlParameters = "code=" + codeGoogle + "&client_id=" + clientID + "&client_secret=" + clientSecret + "&redirect_uri=http://localhost:8080/ServeurDrive/ResponseGoogleDrive" + "&grant_type=authorization_code";

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "POST", urlParameters, properties);
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
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/x-www-form-urlencoded");

        // les paramètres
        String urlParameters = "code=" + codeDropBox + "&client_id=" + AppKey + "&client_secret=" + AppSecret + "&redirect_uri=http://localhost:8080/ServeurDrive/ResponseDropBox" + "&grant_type=authorization_code";

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "POST", urlParameters, properties);
        //print result
        System.out.println(response);

        JSONObject myResponse = new JSONObject(response.toString());
        // on recupere et on stocke l'access token
        this.access_tokenDrop = myResponse.getString("access_token");

        // redirection vers le path Files pour executer la requete GET et ainsi recuperer la liste des fichiers
        java.net.URI location = new java.net.URI("http://localhost:4200/explorer");
        return Response.temporaryRedirect(location).build();

    }

    @Path("/SpaceGoogleDrive")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String spaceGoogleDrive() throws IOException {

        String url = "https://www.googleapis.com/drive/v2/about";

        // propiétés
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Host", "www.googleapis.com");
        properties.put("Authorization", "Bearer " + this.access_tokenGoogle);

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "GET", "", properties);
        System.out.println(response.toString());

        JSONObject jsonInfos = new JSONObject(response.toString());

        long usedSpace = jsonInfos.getLong("quotaBytesUsed");
        long totalAllocation = jsonInfos.getLong("quotaBytesTotal");

        double espaceRestant = (totalAllocation - usedSpace) * 9.31 * Math.pow(10, -10);
        double newEspaceRestant = (double) Math.round(espaceRestant * 100) / 100;

        JSONObject json = new JSONObject();

        json.put("espaceLibreGoogleDrive", newEspaceRestant);
        json.put("espaceTotalGoogleDrive", 15);


        return json.toString() ;

    }

    @Path("/SpaceDropBox")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String spaceDropBox() throws IOException {


        String url = "https://api.dropboxapi.com/2/users/get_space_usage";

        //les propiétés
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Authorization", "Bearer " + this.access_tokenDrop);

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "POST", "", properties);


        JSONObject jsonInfos = new JSONObject(response);
        int usedSpace = jsonInfos.getInt("used");
        int totalAllocation = jsonInfos.getJSONObject("allocation").getInt("allocated");

        double espaceRestant = (totalAllocation - usedSpace) * 9.31 * Math.pow(10, -10);
        double newEspaceRestant = (double) Math.round(espaceRestant * 100) / 100;

        JSONObject json = new JSONObject();

        json.put("espaceLibreDropBox", newEspaceRestant);
        json.put("espaceTotalDropBox", 2);


        return json.toString() ;

    }

    @Path("/Files")
    @Produces(MediaType.TEXT_HTML)
    @GET
    public String getAllFiles() throws IOException {

        ArrayList<Files.File> listeFilesGoogle = new ArrayList<Files.File>();

        if(! access_tokenGoogle.equals("")) {
            GoogleDrive.getFiles(this.access_tokenGoogle);
           listeFilesGoogle= TreatementFiles.treatFilesGoogle(new JSONObject(Request.requestFile), false, access_tokenGoogle);

        }
        ArrayList<Files.File> listeFilesDropBox = new ArrayList<Files.File>();

        if(!access_tokenDrop.equals("")) {
            DropBox.getFiles(this.access_tokenDrop);

            listeFilesDropBox=TreatementFiles.treatFilesDropBox(new JSONObject(Request.requestFile));


        }

        listeFilesGoogle.addAll(listeFilesDropBox);

        return TreatementFiles.generateJSONFiles(listeFilesGoogle).toString();

    }

    @Path("/ChildrensGoogleDrive")
    @Produces(MediaType.TEXT_HTML)
    @GET
    public String getChildrens(@QueryParam("folderId") String folderIdParam ) throws IOException {

        String folderId=folderIdParam;

        String url ="https://www.googleapis.com/drive/v2/files/"+folderId+"/children";

        //les propiétés
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Host", "www.googleapis.com");
        properties.put("Authorization", "Bearer " + this.access_tokenGoogle);

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "GET", "", properties);

        ArrayList<Files.File> listeFilesGoogle= new ArrayList<Files.File>();
        try {
            listeFilesGoogle = TreatementFiles.treatFilesGoogle(new JSONObject(Request.requestFile),true, access_tokenGoogle);
        } catch (Exception e){
            System.out.println(e);
        }

        return TreatementFiles.generateJSONFiles(listeFilesGoogle).toString();
    }

    @Path("/DeleteGoogleDrive")
    // @GET //A changer en DELETE
    @Produces(MediaType.TEXT_HTML)
    public String deleteFileGoogle(@QueryParam("fileId") String fileIdParam) throws IOException {

        String fileID = fileIdParam;
        String url = "https://www.googleapis.com/drive/v2/files/" + fileID;

        //les propiétés
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Host", "www.googleapis.com");
        properties.put("Content-Type","application/x-www-form-urlencoded");
        properties.put("X-HTTP-Method-Override", "DELETE");
        properties.put("Authorization", "Bearer " + this.access_tokenGoogle);

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "DELETE", "", properties);

        return "<p>" + response + "</p>";
    }

    @Path("/RenameGoogleDrive")
    // @GET //A changer en PUT
    @Produces(MediaType.TEXT_HTML)
    public String renameFile(@QueryParam("fileId") String fileIdParam, @QueryParam("title") String titleParam) throws IOException {

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
    // @GET //A changer en DELETE
    @Produces(MediaType.TEXT_HTML)
    public String deleteFileDropBox(@QueryParam("fileId") String fileIdParam) throws IOException {


        String fileID = fileIdParam;


        // requete annexe qui recupere le chemin du fichier
        String urlRequeteannexe = "https://api.dropboxapi.com/2/files/get_metadata";

        HashMap<String, String> propertiesRequeteAnnexe = new HashMap<>();
        propertiesRequeteAnnexe.put("Content-Type", "application/json");
        propertiesRequeteAnnexe.put("Authorization", "Bearer " + this.access_tokenDrop);

        String urlParametersrequeteAnnexe = "{\"path\": \"" + fileID + "\",\"include_media_info\": false,\"include_deleted\": false,\"include_has_explicit_shared_members\": false}";

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


        String urlParameters = "{\"path\": \"" + path + "\"}";


        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "POST", urlParameters, properties);
        System.out.println(response);

        return "<p>" + responseRequeteannexe + "</p>";

    }


    @Path("/RenameDropBox")
    //   @GET //A changer en PUT
    @Produces(MediaType.TEXT_HTML)
    public String renameFileDropBox(@QueryParam("fileId") String fileIdParam, @QueryParam("title") String newTitleParam) throws IOException {

        String fileId = fileIdParam;
        String url = "https://api.dropboxapi.com/2/files/move_v2";
        String newTitle = newTitleParam;

        // requete annexe qui recupere le chemin du fichier
        String urlRequeteannexe = "https://api.dropboxapi.com/2/files/get_metadata";

        HashMap<String, String> propertiesRequeteAnnexe = new HashMap<>();
        propertiesRequeteAnnexe.put("Content-Type", "application/json");
        propertiesRequeteAnnexe.put("Authorization", "Bearer " + this.access_tokenDrop);

        String urlParametersrequeteAnnexe = "{\"path\": \"" + fileId + "\",\"include_media_info\": false,\"include_deleted\": false,\"include_has_explicit_shared_members\": false}";

        String responseRequeteannexe = HttpRequest.Request.setRequest(urlRequeteannexe, "POST", urlParametersrequeteAnnexe, propertiesRequeteAnnexe);

        JSONObject jsonRequeteAnnexe = new JSONObject(Request.requestFile);

        String path = jsonRequeteAnnexe.getString("path_lower");

        // fin requete annexe

        HashMap<String, String> properties = new HashMap<>();
        properties.put("Authorization", "Bearer " + this.access_tokenDrop);
        properties.put("Content-Type", "application/json");

        String urlParameters = "{\"from_path\": \"" + path + "\",\"to_path\": \"/" + newTitle + "\",\"allow_shared_folder\": false,\"autorename\": false,\"allow_ownership_transfer\": false}";

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "POST", urlParameters, properties);

        return "<p>" + response + "</p>";

    }

    @Path("/MoveGoogleDrive")
    @Produces(MediaType.TEXT_HTML)
    public String moveFileGoogleDrive(@QueryParam("fileId") String fileIdParam, @QueryParam("path") String newPathParam) throws IOException{

        String fileId=fileIdParam;
        String newPath = newPathParam;

        // déplacer le fichier
        String url = "https://www.googleapis.com/drive/v2/files/"+fileId+"/parents";

        HashMap<String, String> properties = new HashMap<>();
        properties.put("Host", "www.googleapis.com");
        properties.put("Authorization", "Bearer " + this.access_tokenGoogle);
        properties.put("Content-Type", "application/json");


        String urlParameters="{ \"id\" : \"" + newPath + "\" }";

        String response = HttpRequest.Request.setRequest(url, "POST", urlParameters, properties);



        return "<p>" + response + "</p>";



    }

    // le path doit être de la forme "newpath/"
    @Path("/MoveDropBox")
    @Produces(MediaType.TEXT_HTML)
    public String moveFileDropBox(@QueryParam("fileId") String fileIdParam, @QueryParam("path") String newPathParam) throws IOException {

        String fileId = fileIdParam;
        String url = "https://api.dropboxapi.com/2/files/move_v2";
        String newPath  = newPathParam;

        // requete annexe qui recupere le chemin du fichier
        String urlRequeteannexe = "https://api.dropboxapi.com/2/files/get_metadata";

        HashMap<String, String> propertiesRequeteAnnexe = new HashMap<>();
        propertiesRequeteAnnexe.put("Content-Type", "application/json");
        propertiesRequeteAnnexe.put("Authorization", "Bearer " + this.access_tokenDrop);

        String urlParametersrequeteAnnexe = "{\"path\": \"" + fileId + "\",\"include_media_info\": false,\"include_deleted\": false,\"include_has_explicit_shared_members\": false}";

        String responseRequeteannexe = HttpRequest.Request.setRequest(urlRequeteannexe, "POST", urlParametersrequeteAnnexe, propertiesRequeteAnnexe);

        JSONObject jsonRequeteAnnexe = new JSONObject(Request.requestFile);

        String path = jsonRequeteAnnexe.getString("path_lower");
        String nameFile = jsonRequeteAnnexe.getString("name");

        // fin requete annexe

        HashMap<String, String> properties = new HashMap<>();
        properties.put("Authorization", "Bearer " + this.access_tokenDrop);
        properties.put("Content-Type", "application/json");

        String urlParameters = "{\"from_path\": \"" + path + "\",\"to_path\": \"/" + newPath +"/" + nameFile + "\",\"allow_shared_folder\": false,\"autorename\": false,\"allow_ownership_transfer\": false}";

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "POST", urlParameters, properties);

        return "<p>" + response + "</p>";

    }
    @Path("/UploadDropBox")
    @GET //A changer en PUT
    @Produces(MediaType.TEXT_HTML)
    public String renameFileDropBox(File fileUpload) throws IOException {


        String url = "https://content.dropboxapi.com/2/files/upload";

        //les propiétés
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/octet-stream");
        properties.put("Authorization", "Bearer " + this.access_tokenDrop);


        String urlParameters = "{\"path\": \"/testupload\",\"mode\": \"add\",\"autorename\": true,\"mute\": false}";


        // on execute la requête
        String response = HttpRequest.Request.setRequest(url, "POST", urlParameters, properties);
        System.out.println(response);

        return "<p>" + response + "</p>";

    }

    @Path("/UploadGoogleDrive")
    @Produces(MediaType.TEXT_HTML)
    public String uploadGoogleDrive(@QueryParam("path") String pathParam, @QueryParam("extension") String typeParam) throws IOException {

        String url = "https://www.googleapis.com/upload/drive/v2/files?uploadType=multipart";

        String finalContentType = "";
        switch (typeParam){
            case "pdf":
                finalContentType = "application/" + typeParam;
                break;
            case "xml":
                finalContentType = "application/" + typeParam;
                break;
            case "zip":
                finalContentType = "application/" + typeParam;
                break;
            case "json":
                finalContentType = "application/" + typeParam;
                break;
            case "mpeg":
                finalContentType = "audio/" + typeParam;
                break;
            case "gif":
                finalContentType = "image/" + typeParam;
                break;
            case "jpeg":
                finalContentType = "image/" + typeParam;
                break;
            case "png":
                finalContentType = "image/" + typeParam;
                break;
            case "css":
                finalContentType = "text/" + typeParam;
                break;
            case "csv":
                finalContentType = "text/" + typeParam;
                break;
            case "html":
                finalContentType = "text/" + typeParam;
                break;
            case "js":
                finalContentType = "text/javascript";
                break;
            case "mp4":
                finalContentType = "video/" + typeParam;
                break;
            default:
                System.out.println("impossible");
        }
       // File fileToInsert = new File("/home/hcnn/Documents/ESIR/ESIR2/S8/JXS/tp/project/triso.JPG");
        File fileToInsert = new File(pathParam);
        FileInputStream fileInputStream = new FileInputStream(fileToInsert);
        //les propiétés
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Content-Type", finalContentType);
        properties.put("Content-Length", String.valueOf(fileToInsert.length()));
        properties.put("Authorization", "Bearer " + this.access_tokenGoogle);
        properties.put("uploadType", "multipart");

        // on execute la requête
        //String response = HttpRequest.RequestUpdate.setRequestUpload(url, "POST", "", properties, fileToInsert);
       // String response = HttpRequest.Request.setRequest(url, "POST", "{ \"title\" : \"" +  fileToInsert.getName() + "\" }", properties);
        String response = HttpRequest.Request.setRequestUpload(url, "POST", "" , properties, fileInputStream);
        System.out.println(response);

        return "<p>" + response + "</p>";

    }

}