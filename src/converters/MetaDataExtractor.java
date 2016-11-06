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
import static jdk.nashorn.internal.objects.NativeRegExp.source;
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
    
    public static void extractMetaData(String pathToFile) throws CannotReadException, IOException{
        File file = new File(pathToFile);
        AudioFile audioFile;
        try {
            audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            System.out.println(tag.getFirst(FieldKey.TITLE));
            System.out.println(tag.getFirst(FieldKey.ARTIST));
            System.out.println(tag.getFirst(FieldKey.ALBUM));
            System.out.println(getDuration(file));
        } catch (TagException ex) {
            MyLogger.log("Error reading metadata of file: " + pathToFile);
        } catch (ReadOnlyFileException ex) {
            MyLogger.log("Can't read file: " + pathToFile);
        } catch (InvalidAudioFrameException ex) {
            MyLogger.log("Unsupported format, can't read metaData");
        }
    }
    
    
    private static int getDuration(File file) throws IOException {
        AudioInputStream audioInputStream;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            double durationInSeconds = (frames + 0.0) / format.getFrameRate();
            return (int) durationInSeconds;
        } catch (UnsupportedAudioFileException ex) {
            MyLogger.log("Cant pull duration from this file format: " + file.getAbsolutePath());
            return 0;
        }
    }


    
    
}
