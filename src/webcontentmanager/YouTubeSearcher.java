package webcontentmanager;

import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Mateusz Gołosz
 */
public class YouTubeSearcher implements LinkSearcher {

    final private String API_KEY = "AIzaSyAejw8T5goiLoIMAEqEI9ZlYB3zjMwMKtg";
    final private String BASE_URL = "https://www.googleapis.com/youtube/v3";
    
    @Override
    public ArrayList<String []> searchLinks(String dataToSearch) throws IOException, JSONException {
        ArrayList<String []> results = new ArrayList<String []>();
        String params = RequestHelper.prepareGetParams(dataToSearch);
        
        
        String jsonString = RequestHelper.sendGetHttpsRequest(BASE_URL + "/search?part=snippet&q=" + params + "&type=video&key=" + API_KEY);
        JSONObject response = new JSONObject(jsonString);
        JSONArray trackInfoArray = response.getJSONArray("items");
        int numberOfIterations = (trackInfoArray.length() < 3 ) ? trackInfoArray.length() : 3;
        for (int i = 0; i < numberOfIterations; i++){
            String [] temp = new String[3];
            JSONObject trackInfo = trackInfoArray.getJSONObject(i);
            temp[0] = trackInfo.getJSONObject("id").getString("videoId");
            //temp[1] = trackInfo.getJSONObject("snippet").getString("title").replace("\\/", " ").replace("/", " ").replace("\"", "").replace("?", "");
            temp[1] = trackInfo.getJSONObject("snippet").getString("title").replaceAll("[^\\w.-]", "");
            temp[2] = "YouTube";
            results.add(temp);
        }
        return results;
    }
    
}
