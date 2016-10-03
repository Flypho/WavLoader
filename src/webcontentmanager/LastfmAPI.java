package webcontentmanager;

import models.MetaData;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Mateusz Gołosz
 */
public class LastfmAPI implements MetadataRetriever {

    final private String API_KEY = "7ebf351689526fbdb66553c84f7ccfe8";

    @Override
    public String getArtist(String trackName) throws IOException, JSONException {
        String jsonString = RequestHelper.sendGetHttpRequest("http://ws.audioscrobbler.com/2.0/?method=track.search&track=" + trackName + "&api_key=" + API_KEY + "&format=json");
        JSONObject response = new JSONObject(jsonString);
        JSONArray trackInfoArray = (JSONArray) response.getJSONObject("results").getJSONObject("trackmatches").getJSONArray("track");
        JSONObject trackInfo = (JSONObject) trackInfoArray.get(0);
        return WordUtils.capitalize(trackInfo.get("artist").toString());
    }

    @Override
    public MetaData getTrackInfo(String trackName, String artist, MetaData metaData) throws IOException, JSONException {
        if (artist == null) {
            artist = getArtist(trackName);
        }
        Integer length = null;
        String[] tags = null;
        int tagsSize = 3;
        String album = null;

        String jsonString = RequestHelper.sendGetHttpRequest("http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=" + API_KEY + "&artist=" + artist + "&track=" + trackName + "&format=json");
        JSONObject response = new JSONObject(jsonString);
        JSONArray tagsArray = (JSONArray) response.getJSONObject("track").getJSONObject("toptags").getJSONArray("tag");
        if (tagsArray.length() < 3) {
            tagsSize = tagsArray.length();
        }
        tags = new String[tagsSize];
        for (int i = 0; i < tagsSize; i++) {
            tags[i] = tagsArray.getJSONObject(i).getString("name");
        }
        if (response.getJSONObject("track").has("album")) {
            album = WordUtils.capitalize(response.getJSONObject("track").getJSONObject("album").getString("title"));
        }
        artist = WordUtils.capitalize(response.getJSONObject("track").getJSONObject("artist").getString("name"));
        trackName = WordUtils.capitalize(response.getJSONObject("track").getString("name"));
        length = Integer.parseInt(response.getJSONObject("track").getString("duration"))/1000;
        metaData.setAlbum(album);
        metaData.setArtist(artist);
        metaData.setTags(tags);
        metaData.setTrackName(trackName);
        metaData.normalizeFields();
        metaData.setDurationInSec(length);
        return metaData;
    }

}
