package webcontentmanager;

import java.io.IOException;
import javax.swing.table.TableModel;
import org.json.JSONException;

/**
 *
 * @author MGolosz
 */
public class DirectDownloader implements SongDownloader {

    @Override
    public Long downloadSong(String path, String param, TableModel model, int row) throws IOException, JSONException {
        return SongDownloader.downloadUsingNIO(param, path, model, row);
    }
    
}
