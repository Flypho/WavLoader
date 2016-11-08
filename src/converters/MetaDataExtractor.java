package converters;

import it.sauronsoftware.jave.AudioInfo;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.MultimediaInfo;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import models.MetaData;
import org.jaudiotagger.audio.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import view.MyLogger;

/**
 *
 * @author Mateusz Gołosz
 */
public class MetaDataExtractor {
    
    public static MetaData extractMetaData(MetaData metaData) throws CannotReadException, IOException{
        File file = new File(metaData.getPath());
        AudioFile audioFile;
        try {
            audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            metaData.setTrackName(tag.getFirst(FieldKey.TITLE));
            metaData.setArtist(tag.getFirst(FieldKey.ARTIST));
            metaData.setAlbum(tag.getFirst(FieldKey.ALBUM));
            metaData.setDurationInSec(getDuration(file));
            String[] tags = {tag.getFirst(FieldKey.GENRE)};
            metaData.setTags(tags);
        } catch (TagException ex) {
            //MyLogger.log("Error reading metadata of file: " + pathToFile);
            ex.printStackTrace();
        } catch (ReadOnlyFileException ex) {
            //MyLogger.log("Can't read file: " + pathToFile);
            ex.printStackTrace();
        } catch (InvalidAudioFrameException ex) {
            //MyLogger.log("Unsupported format, can't read metaData");
            ex.printStackTrace();
        }
        return metaData;
    }
    
    private static Integer getDuration(File file) throws IOException {
        Integer duration = null;
        try {
            AudioFile audioFile;
            audioFile = AudioFileIO.read(file);
            duration = audioFile.getAudioHeader().getTrackLength();
        } catch (CannotReadException ex) {
            Logger.getLogger(MetaDataExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TagException ex) {
            Logger.getLogger(MetaDataExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ReadOnlyFileException ex) {
            Logger.getLogger(MetaDataExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAudioFrameException ex) {
            Logger.getLogger(MetaDataExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return duration;
    }

    
}
