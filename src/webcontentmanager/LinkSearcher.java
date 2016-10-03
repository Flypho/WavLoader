package webcontentmanager;

import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONException;

/**
 *
 * @author Mateusz Gołosz
 */
public interface LinkSearcher {
    
    String USER_AGENT = "Mozilla/5.0";
    ArrayList<String []> searchLinks(String dataToSearch) throws IOException, JSONException;

}
