package webcontentmanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.json.JSONException;

/**
 *
 * @author Mateusz Gołosz
 */
public interface SongDownloader {
    
    static Long downloadUsingNIO(String urlStr, String directory, TableModel model) throws IOException {
        URLConnection connection;
        URL url = new URL(urlStr);
        //HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
        if (urlStr.contains("https"))
            connection = (HttpsURLConnection) url.openConnection();
        else
            connection = (HttpURLConnection) (url.openConnection());
        long completeFileSize = connection.getContentLength();
        if (completeFileSize == -1) {
            try {
                System.out.println("BUG");
                TimeUnit.SECONDS.sleep(5);
                connection = url.openConnection();
                completeFileSize = connection.getContentLength();
            } catch (InterruptedException ex) {
                Logger.getLogger(YouTubeDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } if (completeFileSize == -1){
            return -1L;
        }
        BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
        FileOutputStream fos = new FileOutputStream(directory);
        BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
        byte[] data = new byte[1024];
        long downloadedFileSize = 0;
        int x = 0;
        final int row = model.getRowCount() - 1;
        while ((x = in.read(data, 0, 1024)) >= 0) {
            downloadedFileSize += x;
            final double currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100d);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (currentProgress > 0)
                        model.setValueAt(currentProgress, row, 0);
                }
            });

            bout.write(data, 0, x);
        }
        bout.close();
        in.close();
        return (Long)completeFileSize;
    }
    
    public static Long oldMethodDownload(String urlStr, String directory) throws MalformedURLException, FileNotFoundException, IOException {
        Long fileSize;
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(directory);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fileSize = fos.getChannel().size();
        fos.close();
        rbc.close();
        return fileSize;
     }
    
    Long downloadSong(String path, String param, TableModel model) throws IOException, JSONException;

    public static void alternativeDownloadSong(String directory, String videoId, TableModel model) throws IOException, InterruptedException {
        final String BASE_YT_URL = "http://www.youtube.com/watch?v=";
        final String BASE_DWNLD_URL = "http://youtubeconverter.me/downloadz/api.php?output=";
        String request = "http://youtubeconverter.me/downloadz/api.php?url=" + BASE_YT_URL + videoId + "&ftype=mp3&quality=64";
        RequestHelper.sendGetHttpRequest(request);
        Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        String sourceCode = RequestHelper.sendGetHttpRequest(request);
        String sourceLink = "\"yt/";
        int beginIndex = sourceCode.indexOf(sourceLink) + 1;
        int lastIndex = sourceCode.indexOf(".mp3", beginIndex) + ".mp3".length();
        String downloadLink = BASE_DWNLD_URL + sourceCode.substring(beginIndex, lastIndex);
        System.out.println(downloadLink);
        //oldMethodDownload(downloadLink, directory);
        downloadUsingNIO(downloadLink, directory, model);
    }  //http://youtubeconverter.me/downloadz/api.php?output=yt/iOKV9Stri_M/64~~256~~Tool-Vicarious_uuid-57d6e7ff47b58.mp3
}


