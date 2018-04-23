package launchServer;

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

@Path("/DropBox")
public class DropBox {



    private static String AppKey ="84imrzb8n7lobyz";
    private static String AppSecret = "t7kt83ir955vgju";
    private static String access_token;


    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getAuthentification(){

        String url = "https://www.dropbox.com/oauth2/authorize?response_type=code&client_id="+AppKey+"&redirect_uri=http://localhost:8080/ServeurDrive/DropBox/Response";
        return "<a href=" + url + ">"+"click ici pour s'authentifier"+"</a>" +"<br>"+ "<a href=http://localhost:8080/ServeurDrive/DropBox/Files" + ">" + "recupérer les fichiers en JSON"  +"</a>";

    }

    /**
     *  Requete POST pour récuperer l'access token
     * @param codeURL
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    @Path("/Response")
    @GET
    public Response getResponse(@QueryParam("code") String codeURL) throws URISyntaxException, IOException {

        // le code récupéré dans l'url
        String code = codeURL;

        String url = "https://api.dropboxapi.com/oauth2/token";

        // les propriétés
        HashMap<String,String> properties = new HashMap<>();
        properties.put("Content-Type","application/x-www-form-urlencoded");

        // les paramètres
        String urlParameters = "code="+code + "&client_id="+AppKey+ "&client_secret="+AppSecret + "&redirect_uri=http://localhost:8080/ServeurDrive/DropBox/Response" + "&grant_type=authorization_code";

        // on execute la requête
        String response = HttpRequest.Request.setRequest(url,"POST",urlParameters, properties);
        //print result
        System.out.println(response);

        JSONObject myResponse = new JSONObject(response.toString());
        // on recupere et on stocke l'access token
        this.access_token = myResponse.getString("access_token");

        // redirection vers le path Files pour executer la requete GET et ainsi recuperer la liste des fichiers
        java.net.URI location = new java.net.URI("http://localhost:8080/ServeurDrive/DropBox");
        return Response.temporaryRedirect(location).build();

    }

    /**
     * Recuperer la liste des fichiers
     * @return
     * @throws IOException
     */
    @Path("/Files")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getFiles() throws IOException {

        String url = "https://api.dropboxapi.com/2/files/list_folder";

        // les propriétés
        HashMap<String,String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json");
        properties.put("Authorization", "Bearer " + this.access_token);

        // les paramètres
        String urlParameters ="{\"path\": \"\",\"recursive\": false,\"include_media_info\": false,\"include_deleted\": false,\"include_has_explicit_shared_members\": false,\"include_mounted_folders\": true}";

        // on execute la requete
        String response = HttpRequest.Request.setRequest(url,"POST",urlParameters, properties);



        return "<p> "+ response.toString() + "</p>";



    }
}
