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
import view.MyLogger;
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
    private boolean createAlbumFolder;
    private boolean saveOriginalFormat;
    private boolean direct = false;
    
    private String createSearchedPath(MetaData metaData, String basepath, boolean createAlbumFolder) throws IOException {
        if (basepath == null) {
            throw new IOException();
        }
        String dir;
        if (createAlbumFolder){
            dir = basepath + metaData.getArtist() + "\\" + metaData.getAlbum();
        } else {
            dir = basepath;
        }
        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                throw new IOException();
            }
        }
        int i = 1;
        
        
        File fileMP3 = new File(dir + "\\" + metaData.getTrackName() + ".mp3");
        File fileWav = new File(dir + "\\" + metaData.getTrackName() + ".wav");
        while (fileMP3.exists() || fileWav.exists()){
            fileMP3 = new File(dir + "\\" + metaData.getTrackName() + "(" + i + ")" + ".mp3");
            fileWav = new File(dir + "\\" + metaData.getTrackName() + "(" + i + ")" + ".wav");
            i++;
        }
        return fileMP3.getAbsolutePath();
    }
    
    private String createDirectPath(MetaData metaData, String title, String basepath, String source, boolean createAlbumFolder) throws IOException {
        if (basepath == null) {
            throw new IOException();
        }
        String dir;
        
        if (createAlbumFolder) {
            source = source.replaceFirst("^(http://www\\.|http://|www\\.)", "");
            source = source.replaceAll("^[.\\\\/:*?\"<>|]?[\\\\/:*?\"<>|]*", "");
            source = source.substring(0, source.indexOf("/"));
            dir = basepath + source;
            File fileDir = new File(dir);
            if (!fileDir.exists()) {
                if (!fileDir.mkdirs()) {
                    throw new IOException();
                }
            }
        } else {
            dir = path;
        }
        
        int i = 1;
        String tempTitle;
        File fileSource = new File(dir + "\\" + title);
        File fileWav = new File(dir + "\\" + FilenameUtils.removeExtension(title) + ".wav");
        while (fileSource.exists() || fileWav.exists()){
            int j = title.lastIndexOf('.');
            tempTitle = title.substring(0, j) + "(" + i + ")" + title.substring(j);
            fileSource = new File(dir + tempTitle);
            fileWav = new File(dir + FilenameUtils.removeExtension(tempTitle) + ".wav");
            i++;
        }
        return fileSource.getAbsolutePath();
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
                    MyLogger.log("No metadata found. Setting to unknown.");
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
        this.createAlbumFolder = createAlbumFolder;
        this.saveOriginalFormat = saveOriginalFormat;
        if (source == "YouTube")
            songDownloader = new YouTubeDownloader();
        else if (source == "SoundCloud")
            songDownloader = new SoundCloudDownloader();
        else {
            direct = true;
            songDownloader = new DirectDownloader();
        }
        model.setValueAt("Proc metadata", model.getRowCount()-1, 3);
    }
    
    
    @Override
    public void run() {
        final int row = this.row - 1;//model.getRowCount() - 1;
        MetaData metaData = new MetaData();
        if (direct){
            metaData.setArtist(param);
            metaData.setAlbum(param);
            metaData.setDurationInSec(10);
            metaData.setTrackName(artist + FilenameUtils.removeExtension(title));
            try {
                metaData.setPath(createDirectPath(metaData, title, path, source, createAlbumFolder));
            } catch (IOException ex) {
                MyLogger.log("Wrong path: " + metaData.getPath());
                return;
            }
        } else {
            try {
            metaData = setTrackInfo(title, artist, metaData);
            metaData.setPath(createSearchedPath(metaData, path, createAlbumFolder));
            } catch (IOException ex) {
                MyLogger.log("Error while creating folders for: " + metaData.getPath());
            }
        }
        try {
            model.setValueAt("Downloading", row, 3);
            if (this.songDownloader.downloadSong(metaData.getPath(), param, model, row) == -1){
                new File(metaData.getPath()).delete();
                model.setValueAt("Failed", row, 3);
                MyLogger.log("Connection error for file: " + metaData.getPath());
                return;
            }
            model.setValueAt("Wav converting", row, 3);
            WavConverter.mp3ToWav(metaData.getPath(), FilenameUtils.removeExtension(metaData.getPath()) + ".wav");
            if (!saveOriginalFormat) {
                model.setValueAt("Deleting temp", row, 3);
                new File(metaData.getPath()).delete();
            }
            metaData.setPath(FilenameUtils.removeExtension(metaData.getPath()) + ".wav");
            //model.setValueAt("Saving to DB", row, 3);
            //DBHandler.getInstance().InsertTrack(metaData);
            model.setValueAt("Finished", row, 3);
        } catch (JSONException ex) {
            MyLogger.log("Metadata error for file: " + metaData.getPath());
            model.setValueAt("Failed", row, 3);
        } catch (EncoderException ex) {
            MyLogger.log("Conversion error for file: " + metaData.getPath());
            model.setValueAt("Failed", row, 3);
        } catch (IOException ex) {
            MyLogger.log("Connection error for file: " + metaData.getPath());
            model.setValueAt("Failed", row, 3);
        } 
    }

}
