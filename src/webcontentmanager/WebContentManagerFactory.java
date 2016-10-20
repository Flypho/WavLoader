package webcontentmanager;

import javax.swing.JFrame;
import view.MainView;
import view.WindowManager;
import webcontentmanager.*;

/**
 *
 * @author Mateusz Gołosz
 */
public class WebContentManagerFactory {
    private static WebContentManagerFactory instance = null;
    private LastfmAPI lastfmAPI;
    private SpotifyAPI spotifyAPI;
    private YouTubeDownloader youTubeDownloader;
    private YouTubeSearcher youTubeSearcher;
    private WindowManager windowManager;
    private SoundCloudSearcher soundCloudSearcher;
    private SoundCloudDownloader soundCloudDownloader;
    private DirectLinksSearcher directLinksSearcher;
    private MainView mainView;
    
    
    
    private WebContentManagerFactory(){
        lastfmAPI = new LastfmAPI();
        spotifyAPI = new SpotifyAPI();
        youTubeDownloader = new YouTubeDownloader();
        youTubeSearcher = new YouTubeSearcher(); 
        directLinksSearcher = new DirectLinksSearcher();
        windowManager = new WindowManager();
        soundCloudSearcher = new SoundCloudSearcher();
        soundCloudDownloader = new SoundCloudDownloader();
    }

    public LastfmAPI getLastfmAPI() {
        return lastfmAPI;
    }

    public SpotifyAPI getSpotifyAPI() {
        return spotifyAPI;
    }

    public YouTubeDownloader getYouTubeDownloader() {
        return youTubeDownloader;
    }

    public YouTubeSearcher getYouTubeSearcher() {
        return youTubeSearcher;
    }
    
     public static WebContentManagerFactory getInstance(){
        if (instance == null){
            instance = new WebContentManagerFactory();
        }
        return instance;
    }
     
     public WindowManager getWindowManager(){
         return windowManager;
     }

    public SoundCloudSearcher getSoundCloudSearcher() {
        return soundCloudSearcher;
    }

    public SoundCloudDownloader getSoundCloudDownloader() {
        return soundCloudDownloader;
    }
    
    public DirectLinksSearcher getDirectLinksSearcher(){
        return directLinksSearcher;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }
    
    
}
