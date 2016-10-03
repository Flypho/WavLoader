
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import webcontentmanager.DirectLinksSearcher;
import webcontentmanager.RequestHelper;

/**
 *
 * @author Mateusz Gołosz
 */
public class LinkSearcherTests {
    
    public LinkSearcherTests() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void linkSearcherTest(){
        DirectLinksSearcher dls = new DirectLinksSearcher();
        try {
            dls.searchLinks("http://mp3-center.net/download_mp3/va-jazz-funk.html");
            //dls.searchLinks("http://www.artistdirect.com/nad/window/media/page/listen/0,,3492471,00.html");
        } catch (IOException ex) {
            Logger.getLogger(LinkSearcherTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
