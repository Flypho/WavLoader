package webcontentmanager;

import models.MetaData;
import java.io.IOException;
import org.json.JSONException;

/**
 *
 * @author Mateusz Gołosz
 */
public interface MetadataRetriever {
 
    String getArtist(String trackName) throws IOException, JSONException;
    MetaData getTrackInfo(String trackName, String bandName, MetaData metaData) throws IOException, JSONException;
    
}
