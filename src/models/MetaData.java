package models;

import java.text.Normalizer;
import java.util.Objects;

/**
 *
 * @author Mateusz Gołosz
 */
public class MetaData {
    String artist, album, trackName, path;
    String [] tags;
    Integer durationInSec;
    
    public void normalizeFields(){
        if (artist != null)
            artist = Normalizer.normalize(artist, Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        if (album != null)
            album = Normalizer.normalize(album, Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        if (trackName != null)
            trackName = Normalizer.normalize(trackName, Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        if (tags != null){
            for (String elem : tags){
                elem = Normalizer.normalize(elem, Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
            }
        }
    }
    

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
            this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
            this.album = album;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
            this.trackName = trackName;
    }

    public String[] getTags() {
        return tags;
    }

    public String getPath() {
        return path;
    }
    
    public void setPath(String path){
        this.path = path;
    }

    public void setTags(String[] tags) {
            this.tags = tags;
    }

    public Integer getDurationInSec() {
        return durationInSec;
    }

    public void setDurationInSec(Integer durationInSec) {
            this.durationInSec = durationInSec;
    }

    @Override
    public String toString() {
        if (tags != null)
            return "MetaData{" + "artist=" + artist + ", album=" + album + ", trackName=" + trackName + ", tags=" + tags[0] + " " + tags[1] + " " + tags[2] + ", durationInSec=" + durationInSec + " path=" + path + '}';
        else
            return "MetaData{" + "artist=" + artist + ", album=" + album + ", trackName=" + trackName + ", durationInSec=" + durationInSec + " path=" + path + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MetaData other = (MetaData) obj;
        if (!Objects.equals(this.artist, other.artist)) {
            return false;
        }
        if (!Objects.equals(this.album, other.album)) {
            return false;
        }
        if (!Objects.equals(this.trackName, other.trackName)) {
            return false;
        }
        return true;
    }
    
    public boolean isComplete(){
        if (artist == null || album == null || trackName == null || tags == null || durationInSec == null)
            return false;
        else
            return true;      
    }
    
}
