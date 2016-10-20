
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.SwingUtilities;
import org.json.JSONException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import webcontentmanager.DirectDownloader;
import webcontentmanager.LinkSearcher;
import static webcontentmanager.LinkSearcher.USER_AGENT;
import webcontentmanager.SongDownloader;
import webcontentmanager.SoundCloudDownloader;
import webcontentmanager.SoundCloudSearcher;
import webcontentmanager.YouTubeDownloader;
import webcontentmanager.YouTubeSearcher;



/**
 *
 * @author Mateusz Gołosz
 */
public class NewEmptyJUnitTest {
    
    private static ArrayList<String> songs = new ArrayList<String>();

    public NewEmptyJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        songs.add("Gorillaz Dirty Harry");
        songs.add("The Prodigy Breathe");
        songs.add("Yes Roundabout");
        songs.add("Age of Consent Colours");
        songs.add("The Chain Gang of 1974 Sleepwalking");
        songs.add("Old love New Love Twin Shadow");
        
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /*
    @Test
    public void TestSoundCloudSearcher(){
        SoundCloudSearcher sc = new SoundCloudSearcher();
        try {
            sc.searchLinks("Salpica");
        } catch (IOException ex) {
            Logger.getLogger(MetadataAPITest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(MetadataAPITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    
    /*
    @Test 
    public void TestSoundCloudDownloader(){
        SoundCloudDownloader sc = new SoundCloudDownloader();
        try {
            sc.downloadSong(null, "https://api.soundcloud.com/tracks/188270150/download?client_id=7644027dea055cc1f488f8f9a334cb28", null);
        } catch (IOException ex) {
            Logger.getLogger(NewEmptyJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(NewEmptyJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    private HttpURLConnection openConnection(String urlStr) throws MalformedURLException, IOException{
       if (urlStr.contains("https"))
            return (HttpsURLConnection) new URL(urlStr).openConnection();
        else
            return (HttpURLConnection) new URL(urlStr).openConnection();
    }
    
    private boolean checkRedirection(int statusCode){
        if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP || statusCode == HttpURLConnection.HTTP_MOVED_TEMP || statusCode == HttpURLConnection.HTTP_SEE_OTHER)
            return true;
        else
            return false;
    }
    @Test
    public void TestDirectDownloader() throws MalformedURLException, IOException{
       HttpURLConnection connection;
       String cookies;
       String urlStr = "https://archive.org/download/Macro_Form_Who_We_Are_in_slow_time/04_outoftime.mp3";
       String directory = "C:\\Users\\MGolosz\\Documents\\WavLoader\\download_tests\\test2.mp3";
       
       connection = openConnection(urlStr);
        
        if (connection.getHeaderField("Content-Disposition") != null && !connection.getHeaderField("Content-Disposition").isEmpty()){
            System.out.println(connection.getHeaderField("Content-Disposition"));
        }
        
        int statusCode = connection.getResponseCode();
        System.out.println(connection.getHeaderField("Location"));
        while (checkRedirection(statusCode)) {
            urlStr = connection.getHeaderField("Location");
            cookies = connection.getHeaderField("Set-Cookie");
            System.out.println(urlStr);
            connection = openConnection(urlStr);
            connection.setRequestMethod("GET");
            if (cookies != null){
                connection.setRequestProperty("Cookie", cookies);
            }
            //connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"); 
            connection.addRequestProperty("Accept-Encoding", "gzip, deflate"); 
            connection.addRequestProperty("User-Agent", "runscope/0.1");
            statusCode = connection.getResponseCode();
            System.out.println(statusCode);

        }
        System.out.println(statusCode);
        
        long completeFileSize = connection.getContentLength();
        
        if (completeFileSize == -1) {
            try {
                System.out.println("BUG");
                TimeUnit.SECONDS.sleep(5);
                connection = openConnection(urlStr);
                completeFileSize = connection.getContentLength();
                System.out.println(completeFileSize);
            } catch (InterruptedException ex) {
                Logger.getLogger(DirectDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } if (completeFileSize == -1){
            return;
        }
        BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
        FileOutputStream fos = new FileOutputStream(directory);
        BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
        byte[] data = new byte[1024];
        int x = 0;
        while ((x = in.read(data, 0, 1024)) >= 0) {
            bout.write(data, 0, x);
        }
        bout.close();
        in.close();
    }
}
