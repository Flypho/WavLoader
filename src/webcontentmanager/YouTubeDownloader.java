package webcontentmanager;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Mateusz Gołosz
 */
public class YouTubeDownloader implements SongDownloader {

    final String BASE_YTINMP3_URL = "http://www.youtubeinmp3.com/fetch/?format=JSON&video=";
    final String BASE_YT_URL = "http://www.youtube.com/watch?v=";
    final String BASE_YTINMP3_DOWNLOAD = "http://www.youtubeinmp3.com/download/?video=";
    final String SIZE_PARAM = "&filesize=1";
    
    @Override
    public Long downloadSong(String directory, String videoId, TableModel model) throws IOException {
        String downloadURL = null;
        String jsonString = RequestHelper.sendGetHttpRequest(BASE_YTINMP3_URL + BASE_YT_URL + videoId + SIZE_PARAM);
        JSONObject response = null;
        Long fileSize = null;
        try {
            response = new JSONObject(jsonString);
            downloadURL = response.getString("link");
            fileSize = Long.parseLong(response.getString("filesize"));
        } catch (JSONException ex) {
            System.out.print("Nie pobrano JSONa");
            String sourceCode = RequestHelper.retrieveSourceCode(BASE_YTINMP3_DOWNLOAD + BASE_YT_URL + videoId);
            String sourceLink = "id=\"download\" href=\"";
            int beginIndex = sourceCode.indexOf(sourceLink) + sourceLink.length();
            int lastIndex = sourceCode.indexOf("\"", beginIndex);
            downloadURL = "http://www.youtubeinmp3.com" + sourceCode.substring(beginIndex, lastIndex);
            RequestHelper.sendGetHttpRequest(downloadURL);
        }
        System.out.println("Downloading SONG. Directory: " + directory + " videoId: " + videoId + " ReqURL: " + BASE_YTINMP3_URL + BASE_YT_URL + videoId);
        Long tempFileSize = SongDownloader.downloadUsingNIO(downloadURL, directory, model);
        /*for (int i = 0; i < 3; i++) {
            if ((fileSize == null || !Objects.equals(fileSize, tempFileSize)) || tempFileSize == -1) {
                System.out.println("FILESIZE: " + fileSize);
                System.out.println("TEMPFILESIZE: " + tempFileSize);
                try {
                    TimeUnit.SECONDS.sleep(5);
                    tempFileSize = SongDownloader.downloadUsingNIO(downloadURL, directory, model);
                } catch (InterruptedException ex) {
                    Logger.getLogger(YouTubeDownloader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }*/
        return tempFileSize;
    }

}
