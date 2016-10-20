package tasks;

import converters.WavConverter;
import database.DAOImpl;
import database.DBHandler;
import it.sauronsoftware.jave.EncoderException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableModel;
import models.MetaData;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import webcontentmanager.DirectDownloader;
import webcontentmanager.LastfmAPI;
import webcontentmanager.SongDownloader;
import webcontentmanager.SoundCloudDownloader;
import webcontentmanager.SpotifyAPI;
import webcontentmanager.WebContentManagerFactory;
import webcontentmanager.YouTubeDownloader;

/**
 *
 * @author Mateusz Gołosz
 */
public class DownloadTask implements Runnable {

    private SongDownloader songDownloader;
    private LastfmAPI lastfmAPI = WebContentManagerFactory.getInstance().getLastfmAPI();
    private SpotifyAPI spotifyAPI = WebContentManagerFactory.getInstance().getSpotifyAPI();
    private String path;
    private String param;
    private TableModel model;
    private String title;
    private String artist;
    private MetaData metaData;
    private String source;
    private int row;
    
    private String createPath(MetaData metaData, String basepath) throws IOException{
        if (basepath == null) {
            throw new IOException();
        }
        String dir = basepath + metaData.getArtist() + "\\" + metaData.getAlbum();
        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                throw new IOException();
            }
        }
        int i = 1;
        File file = new File(dir + "\\" + metaData.getTrackName() + ".mp3");
        while (file.exists()){
            file = new File(dir + "\\" + metaData.getTrackName() + "(" + i + ")" + ".mp3");
            i++;
        }
        
        return file.getAbsolutePath();
    }
    
    private MetaData setTrackInfo(String title, String artist, MetaData metaData) {
        MetaData temp = metaData;
        try {
            metaData = lastfmAPI.getTrackInfo(title, artist, metaData);
            if (!metaData.isComplete()) {
                metaData = spotifyAPI.getTrackInfo(title, artist, metaData);
            }
        } catch (IOException | JSONException e) {
            try {
                metaData = spotifyAPI.getTrackInfo(title, artist, metaData);
            } catch (IOException | JSONException ex) {
                if (title == null || title.isEmpty()) {
                    metaData.setTrackName("unknown");
                } else {
                    metaData.setTrackName(title);
                }
                if (artist == null || artist.isEmpty()) {
                    metaData.setArtist("unknown");
                } else {
                    metaData.setArtist(artist);
                }
            }
        }
        return temp;
    }
    
    public DownloadTask(String path, String param, String artist, String title, TableModel model, int row, String source, boolean saveOriginalFormat, boolean createAlbumFolder) throws IOException, JSONException{
        this.path = path;
        this.param = param;
        this.model = model;
        this.artist = artist;
        this.title = title;
        this.source = source;
        this.row = row;
        if (source == "YouTube")
            songDownloader = new YouTubeDownloader();
        else if (source == "SoundCloud")
            songDownloader = new SoundCloudDownloader();
        else if (source == "direct")
            songDownloader = new DirectDownloader();
        model.setValueAt("Proc metadata", model.getRowCount()-1, 3);
    }
    
    
    @Override
    public void run() {
        final int row = this.row - 1;//model.getRowCount() - 1;
        MetaData metaData = new MetaData();
        if (source == "direct"){
            metaData.setArtist("test");
            metaData.setAlbum("test");
            metaData.setDurationInSec(10);
            metaData.setTrackName("directDownloadTest" + ThreadLocalRandom.current().nextInt(0, 1000000+1));
            metaData.setPath(path + metaData.getTrackName() + ".mp3");
        } else {
            try {
            metaData = setTrackInfo(title, artist, metaData);
            metaData.setPath(createPath(metaData, path));
            } catch (IOException ex) {
                Logger.getLogger(DownloadTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            model.setValueAt("Downloading", row, 3);
            System.out.println(param);
            if (this.songDownloader.downloadSong(metaData.getPath(), param, model, row) == -1){
                //new File(metaData.getPath()).delete();
                //model.setValueAt("Failed", row, 3);
                //return;
            }
            model.setValueAt("Wav converting", row, 3);
            WavConverter.mp3ToWav(metaData.getPath(), FilenameUtils.removeExtension(metaData.getPath()) + ".wav");
            model.setValueAt("Deleting temp", row, 3);
            new File(metaData.getPath()).delete();
            metaData.setPath(FilenameUtils.removeExtension(metaData.getPath()) + ".wav");
            //model.setValueAt("Saving to DB", row, 3);
            //DBHandler.getInstance().InsertTrack(metaData);
            model.setValueAt("Finished", row, 3);
        } catch (IOException | IllegalArgumentException | EncoderException| JSONException ex) { //| IllegalArgumentException | EncoderException ex) {
            Logger.getLogger(DownloadTask.class.getName()).log(Level.SEVERE, null, ex);
            model.setValueAt("Failed", row, 3);
        }
    }

}
