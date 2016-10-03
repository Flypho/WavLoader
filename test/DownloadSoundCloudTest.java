
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.MalformedInputException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import webcontentmanager.SongDownloader;

/**
 *
 * @author Mateusz Gołosz
 */
public class DownloadSoundCloudTest {
    
    public DownloadSoundCloudTest() {
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
    public void DownloadSoundCloudTest() {
        
        String urlStr = "https://api.soundcloud.com/tracks/272738449/download?client_id=7644027dea055cc1f488f8f9a334cb28";
        String directory = System.getProperty("user.dir") + "\\" + "download_tests" + "\\" + "test.wav";
        /*
        URLConnection connection;
        URL url = new URL(urlStr);
        //HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
        connection = url.openConnection();
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
        */
                
/*
        URL url = null;
        URLConnection con = null;
        int i;
        try {
            url = new URL(urlStr);
            con = url.openConnection();
            File file = new File("D:\\Projekty\\NetBeansProjects\\WavLoader\\download_tests\\" + "cos.wav");
            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file.getName()));
            while ((i = bis.read()) != -1) {
                bos.write(i);
            }
            //bos.flush();
            bis.close();
            bos.close();
        } catch (MalformedInputException malformedInputException) {
            malformedInputException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }*/
        
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

// Activate the new trust manager

        try {
        FileUtils.copyURLToFile(new URL(urlStr), new File(directory));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
        
}
