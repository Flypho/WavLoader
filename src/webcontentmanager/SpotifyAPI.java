package webcontentmanager;

import java.io.FileWriter;
import models.MetaData;
import java.io.IOException;
import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Mateusz Gołosz
 */
public class SpotifyAPI implements MetadataRetriever {

    final private String requestBase = "https://api.spotify.com/v1/search?q=";
    
    @Override
    public String getArtist(String trackName) throws IOException, JSONException {
        String params = RequestHelper.prepareGetParams(trackName);
        String jsonString = RequestHelper.sendGetHttpRequest(requestBase + params + "&type=track");
        JSONObject response = new JSONObject(jsonString);
        String artist = response.getJSONObject("tracks").getJSONArray("items").getJSONObject(0).getJSONArray("artists").getJSONObject(0).getString("name");
        return artist;
    }
    
    @Override
    public MetaData getTrackInfo(String trackName, String artist, MetaData metaData) throws IOException, JSONException {
        if (artist == null) {
            artist = getArtist(trackName);
        }
        String params = RequestHelper.prepareGetParams(artist + " " + trackName);
        String album = null;

        String jsonString = RequestHelper.sendGetHttpRequest(requestBase + params + "&type=track,artist");
        JSONObject response = new JSONObject(jsonString);

        album = WordUtils.capitalize(response.getJSONObject("tracks").getJSONArray("items").getJSONObject(0).getJSONObject("album").getString("name"));
        artist = WordUtils.capitalize(response.getJSONObject("tracks").getJSONArray("items").getJSONObject(0).getJSONArray("artists").getJSONObject(0).getString("name"));
        trackName = WordUtils.capitalize(response.getJSONObject("tracks").getJSONArray("items").getJSONObject(0).getString("name"));
        
        metaData.setAlbum(album);
        metaData.setArtist(artist);
        metaData.setTrackName(trackName);
        metaData.normalizeFields();
        return metaData;
    }
    
    
}
