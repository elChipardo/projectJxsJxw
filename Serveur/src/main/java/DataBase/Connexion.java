package DataBase;

import launchServer.User;
import org.glassfish.jersey.server.model.Suspendable;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.sql.*;

/**
 *
 * @author sqlitetutorial.net
 */
@Path("")
public class Connexion {

    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */

    public static String mailcourant="";
    private static Connection connect() throws ClassNotFoundException {
        // SQLite connection string
        Class.forName("org.sqlite.JDBC");

        String url = "jdbc:sqlite:C:/Users/VictorPC/Documents/GitHub/projectJxsJxw/Serveur/src/main/java/DataBase/DatabaseUser.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    @Path("/createAccount")
    @GET
    public Response insertUser(@QueryParam("mail") String mail, @QueryParam("password") String password) throws URISyntaxException {
        String sql ="INSERT INTO Users (Mail, Password) VALUES (?,?)";

        this.mailcourant=mail;

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mail);
            pstmt.setString(2, password);


            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        java.net.URI location = new java.net.URI("http://localhost:4200/createaccount");
        return Response.temporaryRedirect(location).build();

    }


    public static void insertTokenGoogle(String mail, String tokenGoogle) throws ClassNotFoundException, SQLException {
       // String sql = "INSERT INTO Users(tokenGoogle,tokenDropBox) WHERE Mail = '" +mail+"'";
        Connection conn = connect();
        PreparedStatement statement = conn.prepareStatement("update Users set tokenGoogle=?  where Mail = ?   ");

        statement.setString(1, tokenGoogle);
       // statement.setString(2, tokenDrop);
        statement.setString(2,mail);

        statement.executeUpdate();

    }

    public static void insertTokenDropBox(String mail, String tokenDropBox) throws ClassNotFoundException, SQLException {
        // String sql = "INSERT INTO Users(tokenGoogle,tokenDropBox) WHERE Mail = '" +mail+"'";
        Connection conn = connect();
        PreparedStatement statement = conn.prepareStatement("update Users set tokenDropBox=?  where Mail = ?  ");

        statement.setString(1, tokenDropBox);
        // statement.setString(2, tokenDrop);
        statement.setString(2,mail);

        statement.executeUpdate();

    }




    @Path("/UserExist")
    @GET
    public Response userExists(@QueryParam("mail") String mail,@QueryParam("password") String password) throws ClassNotFoundException, SQLException, URISyntaxException {
        Connection conn = this.connect();
        PreparedStatement statement = conn.prepareStatement("select * from Users where Mail = '"+mail+"' ");
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            System.out.println("je suis la");


            java.net.URI location = new java.net.URI("/ServeurDrive/Connexion?mail=" + mail + "&password=" + password);
            return Response.temporaryRedirect(location).build();
        }
        java.net.URI location = new java.net.URI("http://localhost:4200/createaccount");
        return Response.temporaryRedirect(location).build();
    }



    public String searchTokenGoogleUser(String mail, String password) throws ClassNotFoundException, SQLException {

        Connection conn = this.connect();
        PreparedStatement statement = conn.prepareStatement("select * from Users where Mail = '"+mail+"' ");
        ResultSet resultSet = statement.executeQuery();


        return resultSet.getObject(2).toString();

    }

    public String searchTokenDropBoxUser(String mail, String password) throws ClassNotFoundException, SQLException {

        Connection conn = this.connect();
        PreparedStatement statement = conn.prepareStatement("select * from Users where Mail = '"+mail+"' ");
        ResultSet resultSet = statement.executeQuery();


        return resultSet.getObject(3).toString();

    }


    @Path("/Connexion")
    @GET
    public Response AccountConnection(@QueryParam("mail") String mail,@QueryParam("password") String password) throws SQLException, ClassNotFoundException, URISyntaxException {

        String tokenGoogle = searchTokenGoogleUser(mail,password);
        String tokenDropBox = searchTokenDropBoxUser(mail, password);

        User.setUser(tokenGoogle, tokenDropBox);

        java.net.URI location = new java.net.URI("http://localhost:4200/explorer");
        return Response.temporaryRedirect(location).build();


    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Connexion app = new Connexion();
        // insert three new rows

      //  app.insertUser("victor", "testmdp");
      //  app.insertTokenGoogle("victor", "testmdp", "ya29.Gl3DBe_mgWCklOYRGXb8CUyYtZxPQ9MuE3WgowiKSsT5MKOd82tLNrcnPFVDrdEXXhmufryOQ_Lg0mTEz1cMnYxKE4i3ENNAxPKMRw0kotJwv3WJ222J2ZSNdBgLcZo");
        //app.insertTokenDropBox("victor", "testmdp","IN6UN3_mWlAAAAAAAAABOmawnHyO6ViBpNX1DNJtboBvx5rD4dHTqCl8OdejQZ57");
    }

}