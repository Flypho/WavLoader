package webcontentmanager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Mateusz Gołosz
 */
public class DirectLinksSearcher implements LinkSearcher {

    @Override
    public ArrayList<String[]> searchLinks(String dataToSearch) throws IOException {
        ArrayList <String[]> directLinks = new ArrayList<String[]>();
        ArrayList <String[]> tempLink = removeNonMatchingLinks(findAllLinks(dataToSearch));
        for (String [] elem : tempLink){
            System.out.println(elem[0] + " " + elem[1]);
        }
        return directLinks;
    }
    
    
    private ArrayList<String[]> removeNonMatchingLinks(ArrayList<String> linksArray) throws IOException{
        ArrayList<String[]> removed = new ArrayList<String[]>();
        String regex = "([^\\s]+(\\.(?i)(jpg|JPG|gif|GIF|doc|DOC|pdf|PDF|html|HTML|js|JS|png|PNG|css|CSS))$)";
        for (String elem : linksArray){
            String fileName;
            if (!elem.matches(regex) && (fileName = getFileName(elem)) != null){
                String [] temp = new String[2];
                temp[0] = elem;
                temp[1] = fileName;
                removed.add(temp);
            }
        }
        return removed;
    }
    
    private String getFileName(String link) throws MalformedURLException, IOException{
        String contentKey = "filename=";
        URLConnection connection;
        URL url = new URL(link);
        String fileName = "";
        connection = url.openConnection();
        if (connection.getContentType() != null && connection.getContentType().contains("audio")) {
            if ((fileName = connection.getHeaderField("Content-Disposition")) != null) {
                int beginning = fileName.lastIndexOf(contentKey) + contentKey.length();
                fileName = fileName.substring(beginning);
            }
        }
        
        return fileName;
    }

    private ArrayList<String> findAllLinks(String url) throws IOException{
        ArrayList<String> linksArray = new ArrayList<String>();
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        for (Element elem : links){
            linksArray.add(elem.attr("abs:href"));
        }
        return linksArray;
    }
    
}
