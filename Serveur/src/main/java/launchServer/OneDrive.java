package launchServer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;

@Path("/OneDrive")
public class OneDrive {
    String clientID="e4106227-a22b-488d-a748-9bc12cde10da";


    @GET
    @Produces(MediaType.TEXT_HTML)
    public String afficher(){
        return "<a href=http://localhost:8080/ServeurDrive/OneDrive/Oauth"+">"+"click ici pour s'authentifier"+"</a>"+"<br>"+ "<a href=http://localhost:8080/ServeurDrive/OneDrive/Files" + ">" + "recupérer les fichiers en JSON"  +"</a>";


    }

    @Path("/Oauth")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getAuthentification() throws URISyntaxException, IOException {


        String url = "https://login.live.com/oauth20_authorize.srf?client_id="+clientID+"&scope=onedrive.readwrite&response_type=token&redirect_uri=http://localhost:8080/ServeurDrive/OneDrive/Response";

        java.net.URI location = new java.net.URI(url);
        return Response.temporaryRedirect(location).build();
    }
}
