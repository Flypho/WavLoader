package converters;

import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;
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
public class WavConverterTest {
    
    final String folder = "D:\\Projekty\\NetBeansProjects\\WavLoader\\download_tests\\";
    static private ArrayList <String> sources = new ArrayList<String>();
    static private ArrayList <String> destanations = new ArrayList<String>();
    
    public WavConverterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        sources.add("Age Of Consent - Colours.mp3");
        sources.add("Gorillaz - Dirty Harry (Official Video).mp3");
        sources.add("The Chain Gang of 1974 - Sleepwalking.mp3");
        sources.add("The Prodigy - 'Breathe'.mp3");
        sources.add("Twin Shadow - Old Love   New Love.mp3");
        sources.add("Yes - Roundabout.mp3");
        
        for (String elem : sources){
            destanations.add(FilenameUtils.removeExtension(elem) + ".wav");
        }       
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

    /**
     * Test of mp3ToWav method, of class WavConverter.
     */
    @Test
    public void testMp3ToWav() {
        WavConverter converter = new WavConverter();
        for (int i = 0; i < sources.size(); i++) {
            try {
            converter.mp3ToWav(folder + sources.get(i), folder + destanations.get(i));
            } catch (Exception e){
                System.out.println(folder + sources.get(i));
                System.out.println(folder + destanations.get(i));
                e.printStackTrace();
            }
        }
    }
    
}
