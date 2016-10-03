/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wavloader;

/**
 *
 * @author Mateusz Gołosz
 */
public class WavLoader {
    
    /**
     * @param args the command line arguments
     */
    /*
    public static void main(String[] args) {
        try {
          //  LastfmAPI lastfmAPI = new LastfmAPI();
            //MetaData metadata = lastfmAPI.getTrackInfo("old love/new love", null);
            
            //System.out.println(metadata.toString());
            //LinkSearcher.sendGetHttpsRequest("https://www.googleapis.com/youtube/v3/channels?part=contentDetails&mine=true");
            LinkSearcher linkSearcher = new YouTubeSearcher();
            ArrayList<String []> results = linkSearcher.searchLinks("Lorn Mount Kwaku");
            SongDownloader YTDownloader = new YouTubeDownloader();
            YTDownloader.downloadSong(System.getProperty("user.dir") + "\\" + results.get(0)[1] + ".mp3", results.get(0)[0]);
            for (String [] temp : results){
                System.out.println(temp[0]);
                System.out.println(temp[1]);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }*/
                

     /*   try {
        String source = LinkSearcher.retrieveSourceCode("http://onemusic.co/music/radiohead-optimistic/");
        File newHtmlFile = new File(System.getProperty("user.dir") + "\\test.html");
        FileUtils.writeStringToFile(newHtmlFile, source, (String) null);
        downloadUsingNIO("http://images.artistdirect.com/audio/music/hydrodahero/Dirty_South_Rock_-_Hyro_Da_Hero.mp3");
        } catch (IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
                
    } */
    
  // }
}
