package webcontentmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;
import static webcontentmanager.LinkSearcher.USER_AGENT;

/**
 *
 * @author Mateusz Gołosz
 */
public class RequestHelper {
    
    public static String prepareGetParams(String params){
        String [] searchTags = params.replace("+", "%2B").split(" ");
        StringBuilder dataQuery = new StringBuilder();
        for (int i = 0; i < searchTags.length; i++){
             dataQuery.append(searchTags[i]);
            if (i < searchTags.length - 1)
                dataQuery.append("+"); 
        }
        return dataQuery.toString();
    }
    
    public static String retrieveSourceCode(String webaddress) throws IOException {
        URL url = new URL(webaddress);
        URLConnection con = url.openConnection();
        con.setRequestProperty("User-Agent", USER_AGENT);
        StringBuilder a;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
            String inputLine;
            a = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                a.append(inputLine);
            }
        }
        return a.toString();
    }

    public static String sendPostHttpRequest(String request, ArrayList<String[]> params) throws IOException {

        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty( "Content-Length", "97");
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        for (String[] elem : params) {
            connection.setRequestProperty(elem[0], elem[1]);
            System.out.println(elem[0] + "  " + elem[1]);
        }
        connection.connect();
        StringBuilder a;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
            String inputLine;
            a = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                a.append(inputLine);
            }
        }
        return a.toString();
    }
    
    public static String sendGetHttpsRequest(String request) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(request);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "text/plain");
        conn.setRequestProperty("charset", "utf-8");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
    
    public static String sendGetHttpRequest(String request) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(request);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "text/plain");
        conn.setRequestProperty("charset", "utf-8");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
    
}
