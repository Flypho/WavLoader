package database;

import models.MetaData;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mateusz Gołosz
 */
public class DBTest {
    
    public DBTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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
     public void insertTests() {
         DAOImpl dao = new DAOImpl();
         //System.out.println("ID_PERFORMER " + dao.insertPerformer("Radiohead"));
         //System.out.println("ID_TAG " + dao.insertGenre("alternative"));
         //System.out.println("ID_ALBUM " + dao.insertAlbum("Moon Shaped Pool", "Radiohead"));
         String [] tags = new String[3];
         tags[0] = "alternative";
         tags[1] = "electronic";
         tags[2] = "guitar";
         System.out.println("ID_TRACK " + dao.insertTrack("Glass Eyes", "D:/Costam", 50, "Moon Shaped Pool", "Radiohead", tags));
         
         MetaData metaData = new MetaData();
         metaData.setAlbum("A Moon Shaped Pool");
         metaData.setArtist("Radiohead");
         metaData.setPath("D:\\Projekty\\NetBeansProjects\\WavLoader\\download_tests\\Radiohead\\A Moon Shaped Pool\\Daydreaming.mp3");
         metaData.setDurationInSec(384);
         metaData.setTags(tags);
         metaData.setTrackName("Daydreaming");
         dao.insertTrack(metaData.getTrackName(), metaData.getPath(), metaData.getDurationInSec(), metaData.getAlbum(), metaData.getArtist(), metaData.getTags());
     }
}
