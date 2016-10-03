
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.MetaData;
import org.json.JSONException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import webcontentmanager.LastfmAPI;
import webcontentmanager.SoundCloudSearcher;
import webcontentmanager.SpotifyAPI;

/**
 *
 * @author Mateusz Gołosz
 */
public class MetadataAPITest {
    
    
    private static class TestData{
        private String artist, song;
        public TestData(String artist, String song){
            this.artist = artist;
            this.song = song;
        }
    }
    
    private static ArrayList<TestData> testArray = new ArrayList<TestData>();
    
    public MetadataAPITest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        testArray.add(new TestData("TV On The Radio", "DLZ"));
        testArray.add(new TestData("Clan Of Xymox", "Cry in the Wind"));
        testArray.add(new TestData("Manu Chao", "Me Gustas Tu"));
        testArray.add(new TestData("Dvar", "Hishmaliin"));
        testArray.add(new TestData("Otto Von Schirach", "Salpica"));
        

        
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

    @Test
    public void spotifyAPITest(){
        SpotifyAPI spotify = new SpotifyAPI();
        try {
            for (TestData data : testArray) {
                assertEquals(data.artist, spotify.getArtist(data.song));
                //System.out.println("SPOTIFY: " + spotify.getTrackInfo(data.song, data.artist).toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(MetadataAPITest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(MetadataAPITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void lastfmAPITest() {
        LastfmAPI lastfm = new LastfmAPI();
        try {
            for (TestData data : testArray) {
                assertEquals(data.artist, lastfm.getArtist(data.song));
               // System.out.println(lastfm.getTrackInfo(data.song, data.artist).toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(MetadataAPITest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(MetadataAPITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void bothAPITest(){
        SpotifyAPI spotify = new SpotifyAPI();
        LastfmAPI lastfm = new LastfmAPI();
     
        
        
        
    }
    
    @Test
    public void TestSoundCloudSearcher(){
        SoundCloudSearcher sc = new SoundCloudSearcher();
        try {
            sc.searchLinks("cosculluela black ops");
        } catch (IOException ex) {
            Logger.getLogger(MetadataAPITest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(MetadataAPITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
