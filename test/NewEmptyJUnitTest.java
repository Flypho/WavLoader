
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
    
    @Test
    public void TestDirectDownloader() throws MalformedURLException, IOException{
       URLConnection connection;
       //String urlStr = "http://images.artistdirect.com/audio/oldmusic/slogans_marley.wma";
       String urlStr = "http://mp3-center.net/download.php?name=o+rocchi+f+godi+jungle+birds+italian+library+jazz+funk+holy+grail+w+killer+drum+breaks&url=aHR0cHM6Ly9hcGkuc291bmRjbG91ZC5jb20vdHJhY2tzLzIzMDMxNjMwMy9zdHJlYW0/Y2xpZW50X2lkPTNlYmFiZjkwMzllODM5YTkzN2JkMWY1M2Y5ZDcxYWYz";
       //String urlStr = "http://srv.tonefuse.com/showads/track/srvclk.php?aid=39383411&search=22%20(VA%20%27Sampled%20vol.3%20-%2040%20Original%20Soul%20Jazz%20Funk%20&%20Disco%20Tracks%27%20cd1%20(Virgin)%20-%2015%20-%20Quincy%20Jones%20%E2%80%93%20Summer%20In%20the%20City%20[Nightmares%20on%20Wax%20-%20Les%20Nuits])";
       String directory = "D:\\Projekty\\NetBeansProjects\\WavLoader\\download_tests\\test2.mp3";
        URL url = new URL(urlStr);
        //HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
        if (urlStr.contains("https"))
            connection = (HttpsURLConnection) url.openConnection();
        else
            connection = (HttpURLConnection) (url.openConnection());
        long completeFileSize = connection.getContentLength();
        System.out.println(connection.getHeaderField("Content-Disposition"));
        if (completeFileSize == -1) {
            try {
                System.out.println("BUG");
                TimeUnit.SECONDS.sleep(5);
                connection = url.openConnection();
                completeFileSize = connection.getContentLength();
                System.out.println(completeFileSize);
            } catch (InterruptedException ex) {
                Logger.getLogger(YouTubeDownloader.class.getName()).log(Level.SEVERE, null, ex);
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
