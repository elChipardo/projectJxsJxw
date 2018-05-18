package launchServer;

import Files.TreatementFiles;
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

@Path("/User")
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
        return "<a href=http://localhost:8080/ServeurDrive/User/OauthGoogleDrive"+">"+"click ici pour s'authentifier à google"+"</a>"+"<br>"+ "<a href=http://localhost:8080/ServeurDrive/User/OauthDropBox"+">"+"click ici pour s'authentifier à dropbox"+"</a>" + "<br>" +"<a href=http://localhost:8080/ServeurDrive/User/Files" + ">" + "recupérer les fichiers en JSON"  +"</a>" + "<br>" ;


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
        String urlParameters = "code="+codeGoogle + "&client_id="+clientID+ "&client_secret="+clientSecret + "&redirect_uri=http://localhost:8080/ServeurDrive/User/ResponseGoogleDrive" + "&grant_type=authorization_code";

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url,"POST",urlParameters, properties);
        System.out.println(response.toString());

        JSONObject myResponse = new JSONObject(response.toString());
        this.access_tokenGoogle = myResponse.getString("access_token");



        // redirection vers le path Files pour executer la requete GET et ainsi recuperer la liste des fichiers
        java.net.URI location = new java.net.URI("http://localhost:8080/ServeurDrive/User");
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
        String urlParameters = "code="+codeDropBox + "&client_id="+AppKey+ "&client_secret="+AppSecret + "&redirect_uri=http://localhost:8080/ServeurDrive/User/ResponseDropBox" + "&grant_type=authorization_code";

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url,"POST",urlParameters, properties);
        //print result
        System.out.println(response);

        JSONObject myResponse = new JSONObject(response.toString());
        // on recupere et on stocke l'access token
        this.access_tokenDrop = myResponse.getString("access_token");

        // redirection vers le path Files pour executer la requete GET et ainsi recuperer la liste des fichiers
        java.net.URI location = new java.net.URI("http://localhost:8080/ServeurDrive/User");
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

}
