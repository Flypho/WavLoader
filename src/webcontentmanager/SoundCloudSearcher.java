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
public class SoundCloudSearcher implements LinkSearcher {
    
    private final String clientId = "7644027dea055cc1f488f8f9a334cb28";
    private final String secretId = "13b8d79a3a0807b96987260bb65d48ea";
    private final String BASE_URL = "https://api.soundcloud.com/tracks.json?client_id=";
    
    //https://api.soundcloud.com/tracks/281016990/download?client_id=7644027dea055cc1f488f8f9a334cb28
    

    @Override
    public ArrayList<String[]> searchLinks(String dataToSearch) throws IOException, JSONException {
        ArrayList<String[]> results = new ArrayList<String[]>();
        String params = RequestHelper.prepareGetParams(dataToSearch);
        String jsonString = RequestHelper.sendGetHttpsRequest(BASE_URL + clientId + "&q=" + params);
        System.out.println(BASE_URL + clientId + "&q=" + params);
        JSONArray response = new JSONArray(jsonString);
        int numberOfIterations = (response.length() < 5 ) ? response.length() : 5;
        for (int i = 0; i < numberOfIterations; i++){
            String [] temp = new String[3];
            JSONObject trackInfo = response.getJSONObject(i);
            if (trackInfo.isNull("download_url"))
                continue;
            temp[0] = trackInfo.getString("download_url") + "?client_id=" + clientId;
            temp[1] = trackInfo.getString("title").replaceAll("[^\\w.-]", "");
            temp[2] = "SoundCloud";
            results.add(temp);
            System.out.println(temp[0] + "    " + temp[1]);
            System.out.println(i);
        }
        return results;
    }
    
    
    
    
}
