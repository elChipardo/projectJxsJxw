package HttpRequest;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.FileInputStream;

public class Request {

    public static String requestFile;



    public static String setRequest(String url, String typeRequest, String urlParameters, HashMap<String, String> properties) throws IOException {

        URL obj = new URL(url);

        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        // set POST or GET
        con.setRequestMethod(typeRequest);

        if (!properties.isEmpty()){
            Set entrySet = properties.entrySet();
            Iterator it = entrySet.iterator();

            while(it.hasNext()){
                Map.Entry me = (Map.Entry)it.next();
                con.setRequestProperty(me.getKey().toString(), me.getValue().toString());
            }

        }

        if(!urlParameters.isEmpty()){
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
        }


        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();


        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        requestFile=response.toString();

        return response.toString();



    }


    public static String setRequestUpload(String url, String typeRequest, String urlParameters, HashMap<String, String> properties, FileInputStream file) throws IOException {

        URL obj = new URL(url);

        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        // set POST or GET
        con.setRequestMethod(typeRequest);

        if (!properties.isEmpty()){
            Set entrySet = properties.entrySet();
            Iterator it = entrySet.iterator();

            while(it.hasNext()){
                Map.Entry me = (Map.Entry)it.next();
                con.setRequestProperty(me.getKey().toString(), me.getValue().toString());
            }

        }

        if(!urlParameters.isEmpty()){
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.writeBytes("\r\n");
        }

        int maxBufferSize = 1*1024*1024;
        int bytesAvailable = file.available();
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());

        int bytesRead = file.read(buffer, 0, bufferSize);
        while(bytesRead > 0) {
            wr.write(buffer, 0, bufferSize);
            bytesAvailable = file.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = file.read(buffer, 0, bufferSize);
        }

        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();


        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        requestFile=response.toString();

        return response.toString();


    }
}
