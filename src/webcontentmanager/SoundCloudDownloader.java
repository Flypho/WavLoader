package webcontentmanager;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.table.TableModel;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Mateusz Gołosz
 */
public class SoundCloudDownloader implements SongDownloader {
    
    private String BASE_URL = "http://9soundclouddownloader.com/download-sound-track";

    @Override
    public Long downloadSong(String directory, String downloadURL, TableModel model, int row) throws IOException, JSONException {
        return SongDownloader.downloadUsingNIO(downloadURL, directory, model, row);
    }
    
}
