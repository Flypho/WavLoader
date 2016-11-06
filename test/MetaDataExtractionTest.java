
import converters.MetaDataExtractor;
import java.io.IOException;
import models.MetaData;
import org.jaudiotagger.audio.exceptions.CannotReadException;
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
public class MetaDataExtractionTest {
    
    public MetaDataExtractionTest() {
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
     public void extractMetaData() throws CannotReadException, IOException {
         MetaDataExtractor.extractMetaData("C:\\Users\\Voltaire\\Downloads\\Bass-Drum-1.wav");
         
         
     }
}
